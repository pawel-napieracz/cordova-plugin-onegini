/*
 * Copyright (c) 2017 Onegini B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#import "OGCDVUserAuthenticationClient.h"
#import "OGCDVUserClientHelper.h"
#import "OGCDVConstants.h"

@implementation OGCDVUserAuthenticationClient {
}

- (void)getAuthenticatedUserProfile:(CDVInvokedUrlCommand *)command
{
    ONGUserProfile *authenticatedUserProfile = [[ONGUserClient sharedInstance] authenticatedUserProfile];
    if (authenticatedUserProfile == nil) {
        [self sendErrorResultForCallbackId:command.callbackId withErrorCode:OGCDVPluginErrCodeNoUserAuthenticated andMessage:OGCDVPluginErrDescriptionNoUserAuthenticated];
    } else {
        NSDictionary *result = @{OGCDVPluginKeyProfileId: authenticatedUserProfile.profileId};
        [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:result] callbackId:command.callbackId];
    }
}

- (void)authenticateImplicitly:(CDVInvokedUrlCommand *)command
{
    [self.commandDelegate runInBackground:^{
        NSDictionary *options = command.arguments[0];
        NSString *profileId = options[OGCDVPluginKeyProfileId];
        NSArray *scopes = options[OGCDVPluginKeyScopes];

        ONGUserProfile *user = [OGCDVUserClientHelper getRegisteredUserProfile:profileId];
        if (user == nil) {
            [self sendErrorResultForCallbackId:command.callbackId withErrorCode:OGCDVPluginErrCodeProfileNotRegistered
                                    andMessage:OGCDVPluginErrDescriptionProfileNotRegistered];
            return;
        }

        [[ONGUserClient sharedInstance] implicitlyAuthenticateUser:user scopes:scopes completion:^(BOOL success, NSError * _Nonnull error){
            if (error != nil || !success) {
                [self sendErrorResultForCallbackId:command.callbackId withError:error];
            } else {
                [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK] callbackId:command.callbackId];
            }
        }];
    }];
}

- (void)start:(CDVInvokedUrlCommand *)command
{
    [self.commandDelegate runInBackground:^{
        NSDictionary *options = command.arguments[0];
        NSString *profileId = options[OGCDVPluginKeyProfileId];

        ONGUserProfile *authenticatedUserProfile = [[ONGUserClient sharedInstance] authenticatedUserProfile];
        if (authenticatedUserProfile && [authenticatedUserProfile.profileId isEqualToString:profileId]) {
            [self sendErrorResultForCallbackId:command.callbackId withErrorCode:OGCDVPluginErrCodeNoSuchAuthenticator
                                    andMessage:OGCDVPluginErrDescriptionUserAlreadyAuthenticated];
            return;
        }

        self.authenticationCallbackId = command.callbackId;

        ONGUserProfile *user = [OGCDVUserClientHelper getRegisteredUserProfile:profileId];
        if (user == nil) {
            [self sendErrorResultForCallbackId:command.callbackId withErrorCode:OGCDVPluginErrCodeProfileNotRegistered
                                    andMessage:OGCDVPluginErrDescriptionProfileNotRegistered];
        } else {
            [[ONGUserClient sharedInstance] authenticateUser:user delegate:self];
        }
    }];
}

- (void)providePin:(CDVInvokedUrlCommand *)command
{
    if (!self.pinChallenge) {
        [self sendErrorResultForCallbackId:command.callbackId withErrorCode:OGCDVPluginErrCodeProvidePinNoAuthenticationInProgress
                                andMessage:OGCDVPluginErrDescriptionProvidePinNoAuthenticationInProgress];
        return;
    }
    [self.commandDelegate runInBackground:^{
        NSDictionary *options = command.arguments[0];
        NSString *pin = options[OGCDVPluginKeyPin];
        [self.pinChallenge.sender respondWithPin:pin challenge:self.pinChallenge];
    }];
}

- (void)respondToFingerprintRequest:(CDVInvokedUrlCommand *)command
{
    if (!self.fingerprintChallenge) {
        [self sendErrorResultForCallbackId:command.callbackId withErrorCode:OGCDVPluginErrCodeFingerprintNoAuthenticationInProgress
                                andMessage:OGCDVPluginErrDescriptionFingerprintNoAuthenticationInProgress];
        return;
    }

    [self.commandDelegate runInBackground:^{
        NSDictionary *options = command.arguments[0];
        BOOL shouldNotAccept = ![options[OGCDVPluginKeyAccept] boolValue];
        NSString *prompt = options[OGCDVPluginKeyPrompt];

        if (shouldNotAccept) {
            [self.fingerprintChallenge.sender cancelChallenge:self.fingerprintChallenge];
            return;
        }

        if (prompt == nil) {
            [self.fingerprintChallenge.sender respondWithDefaultPromptForChallenge:self.fingerprintChallenge];
        } else {
            [self.fingerprintChallenge.sender respondWithPrompt:prompt challenge:self.fingerprintChallenge];
        }
    }];
}

- (void)respondToFidoRequest:(CDVInvokedUrlCommand *)command
{
    if (!self.fidoChallenge) {
        [self sendErrorResultForCallbackId:command.callbackId withErrorCode:OGCDVPluginErrCodeFidoNoAuthenticationInProgress
                                andMessage:OGCDVPluginErrDescriptionFidoNoAuthenticationInProgress];
        return;
    }

    [self.commandDelegate runInBackground:^{
        NSDictionary *options = command.arguments[0];
        BOOL shouldNotAccept = ![options[OGCDVPluginKeyAccept] boolValue];

        if (shouldNotAccept) {
            [self.fidoChallenge.sender cancelChallenge:self.fidoChallenge];
            return;
        }

        [self.fidoChallenge.sender respondWithFIDOForChallenge:self.fidoChallenge];
    }];
}

- (void)fallbackToPin:(CDVInvokedUrlCommand *)command
{
    if (self.fingerprintChallenge) {
        [self.commandDelegate runInBackground:^{
            [self.fingerprintChallenge.sender respondWithPinFallbackForChallenge:self.fingerprintChallenge];
        }];
    } else if (self.fidoChallenge) {
        [self.commandDelegate runInBackground:^{
            [self.fidoChallenge.sender respondWithPinFallbackForChallenge:self.fidoChallenge];
        }];
    }
}

- (void)logout:(CDVInvokedUrlCommand *)command
{
    [self.commandDelegate runInBackground:^{
        [[ONGUserClient sharedInstance] logoutUser:^(ONGUserProfile *_Nonnull userProfile, NSError *_Nullable error) {
            if (error == nil) {
                [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK] callbackId:command.callbackId];
            } else {
                [self sendErrorResultForCallbackId:command.callbackId withError:error];
            }
        }];
    }];
}

- (void)cancelFlow:(CDVInvokedUrlCommand *)command
{
    if (self.fingerprintChallenge) {
        [self.fingerprintChallenge.sender cancelChallenge:self.fingerprintChallenge];
    } else if (self.pinChallenge) {
        [self.pinChallenge.sender cancelChallenge:self.pinChallenge];
    }
}

@end

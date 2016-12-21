/*
 * Copyright (c) 2016 Onegini B.V.
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
        BOOL shouldAccept = [options[OGCDVPluginKeyAccept] boolValue];

        if (shouldAccept) {
            NSString *prompt = options[OGCDVPluginKeyPrompt];
            [self.fingerprintChallenge.sender respondWithPrompt:prompt challenge:self.fingerprintChallenge];
        } else {
            [self.fingerprintChallenge.sender cancelChallenge:self.fingerprintChallenge];
        }
    }];
}

- (void)fallbackToPin:(CDVInvokedUrlCommand *)command
{
    if (!self.fingerprintChallenge) {
        [self sendErrorResultForCallbackId:command.callbackId withErrorCode:OGCDVPluginErrCodeFingerprintNoAuthenticationInProgress
                                andMessage:OGCDVPluginErrDescriptionFingerprintNoAuthenticationInProgress];
        return;
    }

    [self.commandDelegate runInBackground:^{
        [self.fingerprintChallenge.sender respondWithPinFallbackForChallenge:self.fingerprintChallenge];
    }];
}

- (void)reauthenticate:(CDVInvokedUrlCommand *)command
{
    [self.commandDelegate runInBackground:^{
        NSDictionary *options = command.arguments[0];
        NSString *profileId = options[OGCDVPluginKeyProfileId];

        self.authenticationCallbackId = command.callbackId;

        ONGUserProfile *user = [OGCDVUserClientHelper getRegisteredUserProfile:profileId];
        if (user == nil) {
            [self sendErrorResultForCallbackId:command.callbackId withErrorCode:OGCDVPluginErrCodeProfileNotRegistered andMessage:OGCDVPluginErrDescriptionProfileNotRegistered];
        } else {
            [[ONGUserClient sharedInstance] reauthenticateUser:user delegate:self];
        }
    }];
}

- (void)logout:(CDVInvokedUrlCommand *)command
{
    [self.commandDelegate runInBackground:^{
        [[ONGUserClient sharedInstance] logoutUser:^(ONGUserProfile *_Nonnull userProfile, NSError *_Nullable error) {
            if (error != nil) {
                [self sendErrorResultForCallbackId:command.callbackId withError:error];
            } else {
                [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK] callbackId:command.callbackId];
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

//  Copyright © 2016 Onegini. All rights reserved.

#import "OGCDVAuthenticatorRegistrationClient.h"
#import "OGCDVConstants.h"
#import "OGCDVAuthenticatorsClientHelper.h"

@implementation OGCDVAuthenticatorRegistrationClient {
}

- (void)start:(CDVInvokedUrlCommand *)command
{
    [self.commandDelegate runInBackground:^{
        ONGUserProfile *user = [[ONGUserClient sharedInstance] authenticatedUserProfile];
        if (user == nil) {
            [self sendErrorResultForCallbackId:command.callbackId withErrorCode:OGCDVPluginErrCodeNoUserAuthenticated andMessage:OGCDVPluginErrDescriptionNoUserAuthenticated];
            return;
        }

        self.authenticationCallbackId = command.callbackId;

        NSDictionary *options = command.arguments[0];
        NSSet<ONGAuthenticator *> *nonRegisteredAuthenticators = [[ONGUserClient sharedInstance] nonRegisteredAuthenticatorsForUser:user];
        ONGAuthenticator *authenticator = [OGCDVAuthenticatorsClientHelper authenticatorFromArguments:nonRegisteredAuthenticators options:options];

        if (authenticator == nil) {
            [self sendErrorResultForCallbackId:command.callbackId withErrorCode:OGCDVPluginErrCodeNoSuchAuthenticator andMessage:OGCDVPluginErrDescriptionNoSuchAuthenticator];
            return;
        }

        [[ONGUserClient sharedInstance] registerAuthenticator:authenticator delegate:self];
    }];
}

- (void)providePin:(CDVInvokedUrlCommand *)command
{
    if (!self.pinChallenge) {
        [self sendErrorResultForCallbackId:command.callbackId withErrorCode:OGCDVPluginErrCodeNoPinChallenge andMessage:OGCDVPluginErrDescriptionNoPinChallenge];
        return;
    }

    NSDictionary *options = command.arguments[0];
    NSString *pin = options[OGCDVPluginKeyPin];
    [self.pinChallenge.sender respondWithPin:pin challenge:self.pinChallenge];
}

- (void)deregister:(CDVInvokedUrlCommand *)command
{
    [self.commandDelegate runInBackground:^{
        ONGUserProfile *user = [[ONGUserClient sharedInstance] authenticatedUserProfile];
        if (user == nil) {
            [self sendErrorResultForCallbackId:command.callbackId withMessage:OGCDVPluginErrDescriptionNoUserAuthenticated];
            return;
        }

        NSDictionary *options = command.arguments[0];
        NSSet<ONGAuthenticator *> *registeredAuthenticators = [[ONGUserClient sharedInstance] registeredAuthenticatorsForUser:user];
        ONGAuthenticator *authenticator = [OGCDVAuthenticatorsClientHelper authenticatorFromArguments:registeredAuthenticators options:options];

        if (authenticator == nil) {
            [self sendErrorResultForCallbackId:command.callbackId withErrorCode:OGCDVPluginErrCodeNoSuchAuthenticator andMessage:OGCDVPluginErrDescriptionNoSuchAuthenticator];
            return;
        }

        [[ONGUserClient sharedInstance] deregisterAuthenticator:authenticator completion:^(BOOL deregistered, NSError *_Nullable error) {
            if (error || !deregistered) {
                [self sendErrorResultForCallbackId:command.callbackId withError:error];
            } else {
                [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK] callbackId:command.callbackId];
            }
        }];
    }];
}

@end

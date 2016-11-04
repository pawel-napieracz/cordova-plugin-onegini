//  Copyright © 2016 Onegini. All rights reserved.

#import "OGCDVAuthenticatorsClient.h"
#import "OGCDVConstants.h"

@implementation OGCDVAuthenticatorsClient {
}

- (void)getRegistered:(CDVInvokedUrlCommand *)command
{
    [self.commandDelegate runInBackground:^{
        ONGUserProfile *user = [[ONGUserClient sharedInstance] authenticatedUserProfile];
        if (user == nil) {
            [self sendErrorResultForCallbackId:command.callbackId withMessage:OGCDVPluginErrorKeyNoUserAuthenticated];
            return;
        }

        NSSet<ONGAuthenticator *> *registeredAuthenticators = [[ONGUserClient sharedInstance] registeredAuthenticatorsForUser:user];
        NSMutableArray *result = [[NSMutableArray alloc] initWithCapacity:registeredAuthenticators.count];
        for (ONGAuthenticator *authenticator in registeredAuthenticators) {
            [result addObject:@{OGCDVPluginKeyAuthenticatorId: authenticator.identifier}];
        }
        [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsArray:result] callbackId:command.callbackId];
    }];
}

- (void)getNotRegistered:(CDVInvokedUrlCommand *)command
{
    [self.commandDelegate runInBackground:^{
        ONGUserProfile *user = [[ONGUserClient sharedInstance] authenticatedUserProfile];
        if (user == nil) {
            [self sendErrorResultForCallbackId:command.callbackId withMessage:OGCDVPluginErrorKeyNoUserAuthenticated];
            return;
        }

        NSSet<ONGAuthenticator *> *nonRegisteredAuthenticators = [[ONGUserClient sharedInstance] nonRegisteredAuthenticatorsForUser:user];
        NSMutableArray *result = [[NSMutableArray alloc] initWithCapacity:nonRegisteredAuthenticators.count];
        for (ONGAuthenticator *authenticator in nonRegisteredAuthenticators) {
            [result addObject:@{OGCDVPluginKeyAuthenticatorId: authenticator.identifier}];
        }
        [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsArray:result] callbackId:command.callbackId];
    }];
}

- (void)getPreferred:(CDVInvokedUrlCommand *)command
{
    [self.commandDelegate runInBackground:^{
        ONGUserProfile *user = [[ONGUserClient sharedInstance] authenticatedUserProfile];
        if (user == nil) {
            [self sendErrorResultForCallbackId:command.callbackId withMessage:OGCDVPluginErrorKeyNoUserAuthenticated];
            return;
        }

        ONGAuthenticator *authenticator = [[ONGUserClient sharedInstance] preferredAuthenticator];
        NSMutableDictionary *result = [[NSMutableDictionary alloc] init];
        if (authenticator != nil) {
            result[OGCDVPluginKeyAuthenticatorId] = authenticator.identifier;
        }

        [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK
                                                             messageAsDictionary:result]
                                    callbackId:command.callbackId];
    }];
}

- (void)setPreferred:(CDVInvokedUrlCommand *)command
{
    [self.commandDelegate runInBackground:^{
        ONGUserProfile *user = [[ONGUserClient sharedInstance] authenticatedUserProfile];
        if (user == nil) {
            [self sendErrorResultForCallbackId:command.callbackId withMessage:OGCDVPluginErrorKeyNoUserAuthenticated];
            return;
        }

        NSDictionary *options = command.arguments[0];
        NSString *authenticatorId = options[OGCDVPluginKeyAuthenticatorId];

        NSSet<ONGAuthenticator *> *registeredAuthenticators = [[ONGUserClient sharedInstance] registeredAuthenticatorsForUser:user];
        for (ONGAuthenticator *authenticator in registeredAuthenticators) {
            if ([authenticator.identifier isEqualToString:authenticatorId]) {
                [[ONGUserClient sharedInstance] setPreferredAuthenticator:authenticator];
                [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK] callbackId:command.callbackId];
                return;
            }
        }

        [self sendErrorResultForCallbackId:command.callbackId withMessage:@"Onegini: No such authenticator found"];
    }];
}

@end

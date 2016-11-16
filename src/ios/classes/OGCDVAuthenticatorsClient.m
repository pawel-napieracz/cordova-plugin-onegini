//  Copyright © 2016 Onegini. All rights reserved.

#import "OGCDVAuthenticatorsClient.h"
#import "OGCDVConstants.h"
#import "OGCDVAuthenticatorsClientHelper.h"
#import "OGCDVUserClientHelper.h"

@implementation OGCDVAuthenticatorsClient {
}

- (void)getAll:(CDVInvokedUrlCommand *)command
{
    [self.commandDelegate runInBackground:^{
        ONGUserProfile *user = [OGCDVAuthenticatorsClient userProfileFromCommand:command];
        if (user == nil) {
            [self sendErrorResultForCallbackId:command.callbackId withErrorCode:OGCDVPluginErrCodeNoUserAuthenticated andMessage:OGCDVPluginErrDescriptionNoUserAuthenticated];
            return;
        }

        NSSet<ONGAuthenticator *> *allAuthenticators = [[ONGUserClient sharedInstance] allAuthenticatorsForUser:user];
        NSMutableArray *result = [[NSMutableArray alloc] initWithCapacity:allAuthenticators.count];
        for (ONGAuthenticator *authenticator in allAuthenticators) {
            [result addObject:[OGCDVAuthenticatorsClientHelper dictionaryFromAuthenticator:authenticator]];
        }
        [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsArray:result] callbackId:command.callbackId];
    }];
}

- (void)getRegistered:(CDVInvokedUrlCommand *)command
{
    [self.commandDelegate runInBackground:^{
        ONGUserProfile *user = [OGCDVAuthenticatorsClient userProfileFromCommand:command];
        if (user == nil) {
            [self sendErrorResultForCallbackId:command.callbackId withErrorCode:OGCDVPluginErrCodeNoUserAuthenticated andMessage:OGCDVPluginErrDescriptionNoUserAuthenticated];
            return;
        }

        NSSet<ONGAuthenticator *> *registeredAuthenticators = [[ONGUserClient sharedInstance] registeredAuthenticatorsForUser:user];
        NSMutableArray *result = [[NSMutableArray alloc] initWithCapacity:registeredAuthenticators.count];
        for (ONGAuthenticator *authenticator in registeredAuthenticators) {
            [result addObject:[OGCDVAuthenticatorsClientHelper dictionaryFromAuthenticator:authenticator]];
        }
        [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsArray:result] callbackId:command.callbackId];
    }];
}

- (void)getNotRegistered:(CDVInvokedUrlCommand *)command
{
    [self.commandDelegate runInBackground:^{
        ONGUserProfile *user = [OGCDVAuthenticatorsClient userProfileFromCommand:command];
        if (user == nil) {
            [self sendErrorResultForCallbackId:command.callbackId withErrorCode:OGCDVPluginErrCodeNoUserAuthenticated andMessage:OGCDVPluginErrDescriptionNoUserAuthenticated];
            return;
        }

        NSSet<ONGAuthenticator *> *nonRegisteredAuthenticators = [[ONGUserClient sharedInstance] nonRegisteredAuthenticatorsForUser:user];
        NSMutableArray *result = [[NSMutableArray alloc] initWithCapacity:nonRegisteredAuthenticators.count];
        for (ONGAuthenticator *authenticator in nonRegisteredAuthenticators) {
            [result addObject:[OGCDVAuthenticatorsClientHelper dictionaryFromAuthenticator:authenticator]];
        }
        [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsArray:result] callbackId:command.callbackId];
    }];
}

- (void)getPreferred:(CDVInvokedUrlCommand *)command
{
    [self.commandDelegate runInBackground:^{
        ONGUserProfile *user = [[ONGUserClient sharedInstance] authenticatedUserProfile];
        if (user == nil) {
            [self sendErrorResultForCallbackId:command.callbackId withErrorCode:OGCDVPluginErrCodeNoUserAuthenticated andMessage:OGCDVPluginErrDescriptionNoUserAuthenticated];
            return;
        }

        ONGAuthenticator *authenticator = [[ONGUserClient sharedInstance] preferredAuthenticator];
        NSDictionary *result = [OGCDVAuthenticatorsClientHelper dictionaryFromAuthenticator:authenticator];

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
            [self sendErrorResultForCallbackId:command.callbackId withErrorCode:OGCDVPluginErrCodeNoUserAuthenticated andMessage:OGCDVPluginErrDescriptionNoUserAuthenticated];
            return;
        }

        NSDictionary *options = command.arguments[0];
        NSSet<ONGAuthenticator *> *registeredAuthenticators = [[ONGUserClient sharedInstance] registeredAuthenticatorsForUser:user];
        ONGAuthenticator *authenticator = [OGCDVAuthenticatorsClientHelper authenticatorFromArguments:registeredAuthenticators options:options];

        if (authenticator == nil) {
            [self sendErrorResultForCallbackId:command.callbackId withErrorCode:OGCDVPluginErrCodeNoSuchAuthenticator
                                    andMessage:OGCDVPluginErrDescriptionNoSuchAuthenticator];
            return;
        }

        [[ONGUserClient sharedInstance] setPreferredAuthenticator:authenticator];
        [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK] callbackId:command.callbackId];
    }];
}

+ (ONGUserProfile *)userProfileFromCommand:(CDVInvokedUrlCommand *)command
{
    NSDictionary *options = command.arguments[0];
    NSString *profileId = options[OGCDVPluginKeyProfileId];
    return [OGCDVUserClientHelper getRegisteredUserProfile:profileId];
}

@end

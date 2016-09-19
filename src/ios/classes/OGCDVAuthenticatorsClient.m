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
        [self sendErrorResultForCallbackId:command.callbackId withMessage:@"Onegini: No user authenticated."];
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
        [self sendErrorResultForCallbackId:command.callbackId withMessage:@"Onegini: No user authenticated."];
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

@end

//  Copyright © 2016 Onegini. All rights reserved.

#import "OGCDVAuthenticatorsClient.h"

NSString *const OGCDVPluginKeyAuthenticatorId = @"id";

@implementation OGCDVAuthenticatorsClient {
}

- (void)getRegistered:(CDVInvokedUrlCommand *)command
{
  [self.commandDelegate runInBackground:^{
      if ([[ONGUserClient sharedInstance] authenticatedUserProfile] == nil) {
        [self sendErrorResultForCallbackId:command.callbackId withMessage:@"Onegini: No user authenticated."];
        return;
      }

      NSSet<ONGAuthenticator *> *registeredAuthenticators = [[ONGUserClient sharedInstance] registeredAuthenticators];
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
      if ([[ONGUserClient sharedInstance] authenticatedUserProfile] == nil) {
        [self sendErrorResultForCallbackId:command.callbackId withMessage:@"Onegini: No user authenticated."];
        return;
      }

      [[ONGUserClient sharedInstance] fetchNonRegisteredAuthenticators:^(NSSet<ONGAuthenticator *> *authenticators, NSError *error) {
          if (error != nil) {
            [self sendErrorResultForCallbackId:command.callbackId withError:error];
          } else {
            NSMutableArray *result = [[NSMutableArray alloc] initWithCapacity:authenticators.count];
            for (ONGAuthenticator *authenticator in authenticators) {
              [result addObject:@{OGCDVPluginKeyAuthenticatorId: authenticator.identifier}];
            }
            [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsArray:result] callbackId:command.callbackId];
          }
      }];
  }];
}

@end

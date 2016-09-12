//  Copyright © 2016 Onegini. All rights reserved.

#import "OGCDVUserClient.h"
#import "OGCDVConstants.h"

NSString *const OGCDVPluginKeyAuthenticatorId = @"id";

@implementation OGCDVUserClient {
}

- (void)validatePinWithPolicy:(CDVInvokedUrlCommand *)command
{
  [self.commandDelegate runInBackground:^{
      NSDictionary *options = command.arguments[0];
      NSString *pin = options[OGCDVPluginKeyPin];
      [[ONGUserClient sharedInstance] validatePinWithPolicy:pin completion:^(BOOL valid, NSError *_Nullable error) {
          if (error != nil || !valid) {
            [self sendErrorResultForCallbackId:command.callbackId withError:error];
          } else {
            [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK] callbackId:command.callbackId];
          }
      }];
  }];
}

- (void)getRegisteredAuthenticators:(CDVInvokedUrlCommand *)command
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

- (void)getNotRegisteredAuthenticators:(CDVInvokedUrlCommand *)command
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

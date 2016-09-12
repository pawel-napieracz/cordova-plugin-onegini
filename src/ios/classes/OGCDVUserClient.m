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
      NSSet<ONGAuthenticator *> *registeredAuthenticators = [[ONGUserClient sharedInstance] registeredAuthenticators];
      NSMutableArray *result = [[NSMutableArray alloc] initWithCapacity:registeredAuthenticators.count];
      for (ONGAuthenticator *authenticator in registeredAuthenticators) {
        [result addObject:@{OGCDVPluginKeyAuthenticatorId: authenticator.identifier}];
      }
      [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsArray:result] callbackId:command.callbackId];
  }];
}

@end

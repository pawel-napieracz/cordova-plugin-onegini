//  Copyright © 2016 Onegini. All rights reserved.

#import "OGCDVUserDeregistrationClient.h"
#import "OGCDVUserClientHelper.h"
#import "OGCDVConstants.h"

@implementation OGCDVUserDeregistrationClient {}

- (void)deregister:(CDVInvokedUrlCommand*)command
{
  [self.commandDelegate runInBackground:^{
      NSDictionary *options = command.arguments[0];
      NSString *profileId = options[OGCDVPluginKeyProfileId];
      ONGUserProfile *user = [OGCDVUserClientHelper getRegisteredUserProfile:profileId];

      if (user == nil) {
        [self sendErrorResultForCallbackId:command.callbackId withErrorCode:OGCDVPluginErrCodeProfileNotRegistered
                                andMessage:OGCDVPluginErrProfileNotRegistered];
      } else {
        [[ONGUserClient sharedInstance] deregisterUser:user completion:^(BOOL deregistered, NSError *_Nullable error) {
            if (error != nil || !deregistered) {
              [self sendErrorResultForCallbackId:command.callbackId withError:error];
            } else {
              [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK] callbackId:command.callbackId];
            }
        }];
      }
  }];
}

@end

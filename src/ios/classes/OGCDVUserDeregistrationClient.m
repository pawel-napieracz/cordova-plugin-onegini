//  Copyright © 2016 Onegini. All rights reserved.

#import "OGCDVUserDeregistrationClient.h"
#import "OGCDVUserClientHelper.h"
#import "OGCDVConstants.h"

@implementation OGCDVUserDeregistrationClient {}

- (void)deregister:(CDVInvokedUrlCommand*)command
{
  NSDictionary *options = [command.arguments objectAtIndex:0];
  NSString *profileId = options[OGCDVPluginKeyProfileId];
  ONGUserProfile *user = [OGCDVUserClientHelper getRegisteredUserProfile:profileId];

  if (user == nil) {
    [self sendErrorResultForCallbackId:command.callbackId withMessage:@"Onegini: No registered user found."];
  } else {
    [[ONGUserClient sharedInstance] deregisterUser:user completion:^(BOOL deregistered, NSError * _Nullable error) {
      if (error != nil || !deregistered) {
        [self sendErrorResultForCallbackId:command.callbackId withError:error];
      } else {
        // TODO there's a bug in the iOS SDK where deregistration doesn't log out the user, so until that's fixed we need this line
        [[ONGUserClient sharedInstance] logoutUser:nil];

        [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK] callbackId:command.callbackId];
      }
    }];
  }
}

@end

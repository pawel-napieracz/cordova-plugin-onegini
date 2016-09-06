//  Copyright © 2016 Onegini. All rights reserved.

#import "OGCDVMobileAuthenticationClient.h"

@implementation OGCDVMobileAuthenticationClient {}

- (void)registerForRemoteNotifications:(CDVInvokedUrlCommand*)command
{

}

// if not yet registered for notifications call registerForRemote.. here, then wait for the appdelegate's didregsiterforremote.. which sets the pushtoken in the SDK, then call enrollFor..
- (void)enroll:(CDVInvokedUrlCommand*)command
{

  // TODO 'enroll' is probably a one-off..  if so, we need to create a sep method to call registerForRemote (from 'start')?
  // or perhaps it doesn't hurt to enroll multiple times?

  // TODO add iOS8 check, as 7 is the minimum
  UIUserNotificationSettings *settings = [UIUserNotificationSettings settingsForTypes:(UIUserNotificationTypeAlert | UIUserNotificationTypeBadge | UIUserNotificationTypeSound) categories:nil];
  [[UIApplication sharedApplication] registerUserNotificationSettings:settings];
  [[UIApplication sharedApplication] registerForRemoteNotifications];

  [[ONGUserClient sharedInstance] enrollForMobileAuthentication:^(BOOL enrolled, NSError * _Nullable error) {
    if (error != nil || !enrolled) {
      [self sendErrorResultForCallbackId:command.callbackId withError:error];
    } else {
      [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK] callbackId:command.callbackId];
    }
  }];
}

@end

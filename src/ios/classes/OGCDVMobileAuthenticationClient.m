//  Copyright © 2016 Onegini. All rights reserved.

#import "OGCDVMobileAuthenticationClient.h"

@implementation OGCDVMobileAuthenticationClient {}

// if not yet registered for notifications call registerForRemote.. here, then wait for the appdelegate's didregsiterforremote.. which sets the pushtoken in the SDK, then call enrollFor..
- (void)enroll:(CDVInvokedUrlCommand*)command
{
  self.enrollCallbackId = command.callbackId;

  if (![self isRegisteredForRemoteNotifications]) {
    [self registerForRemoteNotifications];
  } else {
    [self doEnroll];
  }
}

- (void)doEnroll
{
  [[ONGUserClient sharedInstance] enrollForMobileAuthentication:^(BOOL enrolled, NSError * _Nullable error) {
      if (error != nil || !enrolled) {
        [self sendErrorResultForCallbackId:self.enrollCallbackId withError:error];
      } else {
        [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK] callbackId:self.enrollCallbackId];
      }
  }];
}

- (void)registerForRemoteNotifications
{
  // TODO constants
  [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(registrationOK:) name:@"registrationOK" object:nil];
  [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(registrationNOK:) name:@"registrationNOK" object:nil];


  // TODO add iOS8 check, as 7 is the minimum supported by the Onegini SDK..
  UIUserNotificationSettings *settings = [UIUserNotificationSettings settingsForTypes:(UIUserNotificationTypeAlert | UIUserNotificationTypeBadge | UIUserNotificationTypeSound) categories:nil];
  [[UIApplication sharedApplication] registerUserNotificationSettings:settings];
  [[UIApplication sharedApplication] registerForRemoteNotifications];
}

- (BOOL)isRegisteredForRemoteNotifications
{
  // TODO this is iOS 8+
  return [[UIApplication sharedApplication] isRegisteredForRemoteNotifications];
}

- (void)registrationOK:(NSNotification *) result
{
  // TODO constant
  NSData *deviceToken = result.userInfo[@"deviceToken"];
  [[ONGUserClient sharedInstance] storeDevicePushTokenInSession:deviceToken];
  [self doEnroll];
}

- (void)registrationNOK:(NSNotification *) result
{
  // TODO constant
  NSError *error = result.userInfo[@"error"];
  [self sendErrorResultForCallbackId:self.enrollCallbackId withError:error];
}

@end

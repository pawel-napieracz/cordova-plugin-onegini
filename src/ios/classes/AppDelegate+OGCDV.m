//  Copyright © 2016 Onegini. All rights reserved.

#import "AppDelegate+OGCDV.h"

@implementation AppDelegate (OGCDV)

- (void)application:(UIApplication *)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken
{
  // TODO constants
  [[NSNotificationCenter defaultCenter] postNotificationName:@"registrationOK" object:self userInfo:@{@"deviceToken": deviceToken}];
}

- (void)application:(UIApplication *)application didFailToRegisterForRemoteNotificationsWithError:(NSError *)error
{
  // TODO constants
  [[NSNotificationCenter defaultCenter] postNotificationName:@"registrationNOK" object:self userInfo:@{@"error": error}];
}

@end

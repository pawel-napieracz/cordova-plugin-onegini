//  Copyright © 2016 Onegini. All rights reserved.

#import "AppDelegate.h"

@interface AppDelegate (OGCDV)

- (void)application:(UIApplication *)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken;
- (void)application:(UIApplication *)application didFailToRegisterForRemoteNotificationsWithError:(NSError *)error;

@end

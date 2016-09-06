//  Copyright © 2016 Onegini. All rights reserved.

#import "AppDelegate+OGCDV.h"

@implementation AppDelegate (OGCDV)

- (void)application:(UIApplication *)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken
{
  [[ONGUserClient sharedInstance] storeDevicePushTokenInSession:deviceToken];
}

@end

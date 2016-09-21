//  Copyright © 2016 Onegini. All rights reserved.

#import "AppDelegate+OGCDV.h"
#import "OGCDVMobileAuthenticationClient.h"
#import "OGCDVConstants.h"

@implementation AppDelegate (OGCDV)

- (void)application:(UIApplication *)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken
{
  [[self.viewController getCommandInstance:OGCDVPluginClassMobileAuthentication]
      didRegisterForRemoteNotificationsWithDeviceToken:deviceToken];
}

- (void)application:(UIApplication *)application didFailToRegisterForRemoteNotificationsWithError:(NSError *)error
{
  [[self.viewController getCommandInstance:OGCDVPluginClassMobileAuthentication]
      didFailToRegisterForRemoteNotificationsWithError:error];
}

@end
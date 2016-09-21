//  Copyright © 2016 Onegini. All rights reserved.

#import "AppDelegate+OGCDV.h"
#import "OGCDVConstants.h"
#import "OGCDVMobileAuthenticationClient.h"
#import "OGCDVHandleMobileAuthenticationRequestClient.h"

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

- (void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo
{
  [[self.viewController getCommandInstance:OGCDVPluginClassHandleMobileAuthenticationRequest]
      handleMobileAuthenticationRequest:userInfo];
}

@end
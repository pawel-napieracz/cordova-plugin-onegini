//  Copyright © 2016 Onegini. All rights reserved.

#import "AppDelegate+OGCDV.h"
#import "OGCDVConstants.h"
#import "OGCDVMobileAuthenticationClient.h"
#import "OGCDVMobileAuthenticationRequestClient.h"

@implementation AppDelegate (OGCDV)

- (void)application:(UIApplication *)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken
{
    [[self.viewController getCommandInstance:OGCDVPluginClassMobileAuthenticationClient]
        didRegisterForRemoteNotificationsWithDeviceToken:deviceToken];
}

- (void)application:(UIApplication *)application didFailToRegisterForRemoteNotificationsWithError:(NSError *)error
{
    [[self.viewController getCommandInstance:OGCDVPluginClassMobileAuthenticationClient]
        didFailToRegisterForRemoteNotificationsWithError:error];
}

- (void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo
{
    [[self.viewController getCommandInstance:OGCDVPluginClassMobileAuthenticationRequestClient]
        didReceiveRemoteNotification:userInfo];
}

@end
/*
 * Copyright (c) 2017 Onegini B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#import "AppDelegate+OGCDV.h"
#import "OGCDVConstants.h"
#import "OGCDVMobileAuthenticationClient.h"
#import "OGCDVPushMobileAuthRequestClient.h"

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

- (BOOL)application:(UIApplication *)app openURL:(NSURL *)url options:(NSDictionary<UIApplicationOpenURLOptionsKey, id> *)options
{
    [[NSNotificationCenter defaultCenter] postNotificationName:OGCDVDidReceiveRegistrationCallbackURLNotification object:url];
}

@end
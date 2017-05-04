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

#import "OGCDVMobileAuthenticationClient.h"
#import "OGCDVConstants.h"

@implementation OGCDVMobileAuthenticationClient {
}

NSString *const OGCDVPluginKeyMarkedAsEnrolled = @"EnrolledForMobileAuthentication";

- (void)pluginInitialize
{
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(applicationDidFinishLaunching) name:UIApplicationDidFinishLaunchingNotification object:nil];
}

- (void)applicationDidFinishLaunching
{
    if ([self isEnrolled]) {
        [self registerForRemoteNotifications];
    }
}

- (void)enroll:(CDVInvokedUrlCommand *)command
{
    [self.commandDelegate runInBackground:^{
        ONGUserProfile *user = [[ONGUserClient sharedInstance] authenticatedUserProfile];
        if (user == nil) {
            [self sendErrorResultForCallbackId:command.callbackId withErrorCode:OGCDVPluginErrCodeNoUserAuthenticated andMessage:OGCDVPluginErrDescriptionNoUserAuthenticated];
            return;
        }

        self.enrollCallbackId = command.callbackId;

        [[ONGUserClient sharedInstance] enrollForMobileAuth:^(BOOL enrolled, NSError *_Nullable error) {
            if (error != nil || !enrolled) {
                [self sendErrorResultForCallbackId:self.enrollCallbackId withError:error];
            } else {
                [self markAsEnrolled];
                [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK] callbackId:self.enrollCallbackId];
            }
            self.enrollCallbackId = nil;
        }];
    }];
}

- (void)enrollForPush:(CDVInvokedUrlCommand *)command
{
    [self.commandDelegate runInBackground:^{
        ONGUserProfile *user = [[ONGUserClient sharedInstance] authenticatedUserProfile];
        if (user == nil) {
            [self sendErrorResultForCallbackId:command.callbackId withErrorCode:OGCDVPluginErrCodeNoUserAuthenticated andMessage:OGCDVPluginErrDescriptionNoUserAuthenticated];
            return;
        }

        self.enrollCallbackId = command.callbackId;

        if (![self isRegisteredForRemoteNotifications]) {
            [self registerForRemoteNotifications];
        } else {
            [self doEnrollForPush];
        }
    }];
}

- (void)doEnrollForPush
{
    [[ONGUserClient sharedInstance] enrollForPushMobileAuthWithDeviceToken:self.deviceToken
                                                                completion:^(BOOL enrolled, NSError *_Nullable error) {
                                                                    if (error != nil || !enrolled) {
                                                                        [self sendErrorResultForCallbackId:self.enrollCallbackId withError:error];
                                                                    } else {
                                                                        [self markAsEnrolled];
                                                                        [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK] callbackId:self.enrollCallbackId];
                                                                    }
                                                                    self.enrollCallbackId = nil;
                                                                }];
}

- (void)registerForRemoteNotifications
{
    if ([[UIApplication sharedApplication] respondsToSelector:@selector(registerUserNotificationSettings:)]) {
#ifdef __IPHONE_8_0
        UIUserNotificationSettings *settings = [UIUserNotificationSettings settingsForTypes:(UIUserNotificationTypeAlert | UIUserNotificationTypeBadge | UIUserNotificationTypeSound) categories:nil];
        [[UIApplication sharedApplication] registerUserNotificationSettings:settings];
        [[UIApplication sharedApplication] registerForRemoteNotifications];
#endif
    } else {
        UIRemoteNotificationType myTypes = UIRemoteNotificationTypeBadge | UIRemoteNotificationTypeAlert | UIRemoteNotificationTypeSound;
        [[UIApplication sharedApplication] registerForRemoteNotificationTypes:myTypes];
    }
}

- (BOOL)isRegisteredForRemoteNotifications
{
    if ([[UIApplication sharedApplication] respondsToSelector:@selector(isRegisteredForRemoteNotifications)]) {
#ifdef __IPHONE_8_0
        return [[UIApplication sharedApplication] isRegisteredForRemoteNotifications];
#endif
    } else {
        return [[UIApplication sharedApplication] enabledRemoteNotificationTypes] != UIRemoteNotificationTypeNone;
    }
}

- (void)didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken
{
    if (self.enrollCallbackId == nil) {
        self.deviceToken = deviceToken;
    } else {
        [self doEnrollForPush];
    }
}

- (void)didFailToRegisterForRemoteNotificationsWithError:(NSError *)error
{
    [self sendErrorResultForCallbackId:self.enrollCallbackId withError:error];
}

- (void)markAsEnrolled
{
    [[NSUserDefaults standardUserDefaults] setBool:YES forKey:OGCDVPluginKeyMarkedAsEnrolled];
}

- (BOOL)isEnrolled
{
    return [[NSUserDefaults standardUserDefaults] boolForKey:OGCDVPluginKeyMarkedAsEnrolled];
}
@end

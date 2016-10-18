//  Copyright © 2016 Onegini. All rights reserved.

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
            [self sendErrorResultForCallbackId:command.callbackId withMessage:OGCDVPluginErrorKeyNoUserAuthenticated];
            return;
        }

        self.enrollCallbackId = command.callbackId;

        if (![self isRegisteredForRemoteNotifications]) {
            [self registerForRemoteNotifications];
        } else {
            [self doEnroll];
        }
    }];
}

- (void)doEnroll
{
    [[ONGUserClient sharedInstance] enrollForMobileAuthentication:^(BOOL enrolled, NSError *_Nullable error) {
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
        // remember until 'OGCDVClient start' completes
        self.pendingDeviceToken = deviceToken;
    } else {
        [[ONGUserClient sharedInstance] storeDevicePushTokenInSession:deviceToken];
        [self doEnroll];
    }
}

- (void)didFailToRegisterForRemoteNotificationsWithError:(NSError *)error
{
    [[ONGUserClient sharedInstance] storeDevicePushTokenInSession:nil];
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

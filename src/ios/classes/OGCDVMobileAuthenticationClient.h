//  Copyright © 2016 Onegini. All rights reserved.

#import "CDVPlugin+OGCDV.h"
#import "OneginiSDK.h"

@interface OGCDVMobileAuthenticationClient : CDVPlugin

@property (nonatomic, copy) NSString *enrollCallbackId;
@property (nonatomic, copy) NSData *pendingDeviceToken;

- (void)enroll:(CDVInvokedUrlCommand *)command;

- (BOOL)isEnrolled;

- (void)registerForRemoteNotifications;
- (void)didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken;
- (void)didFailToRegisterForRemoteNotificationsWithError:(NSError *)error;

@end

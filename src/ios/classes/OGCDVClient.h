//  Copyright © 2016 Onegini. All rights reserved.

#import "CDVPlugin+OGCDV.h"
#import "OneginiSDK.h"

@interface OGCDVClient : CDVPlugin

@property (nonatomic, copy) NSDictionary *launchNotificationUserInfo;

- (void)start:(CDVInvokedUrlCommand *)command;
- (void)applicationDidFinishLaunchingNotification:(NSNotification *)notification;
- (void)handleLaunchNotification;

@end

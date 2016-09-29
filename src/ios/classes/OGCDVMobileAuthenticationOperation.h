//  Copyright © 2016 Onegini. All rights reserved.

#import "OneginiSDK.h"
#import "OGCDVMobileAuthenticationRequestClient.h"

@interface OGCDVMobileAuthenticationOperation : NSOperation<ONGMobileAuthenticationRequestDelegate, OGCDVPluginMobileAuthenticationRequestDelegate> {
    NSDictionary *remoteNotificationUserInfo;
}

@property (nonatomic, copy) void (^confirmationChallengeConfirmationBlock)(BOOL confirmRequest);
@property (retain) NSDictionary *remoteNotificationUserInfo;

-(id)initWithRemoteNotificationUserInfo:(NSDictionary *)userInfo;

@end
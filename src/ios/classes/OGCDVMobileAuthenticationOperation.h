//  Copyright © 2016 Onegini. All rights reserved.

#import "OneginiSDK.h"
#import "OGCDVMobileAuthenticationRequestClient.h"

@interface OGCDVMobileAuthenticationOperation : NSOperation<ONGMobileAuthenticationRequestDelegate, OGCDVPluginMobileAuthenticationRequestDelegate> {
    NSDictionary *remoteNotificationUserInfo;
    NSString *didCompleteOperationCallbackId;
}

@property (nonatomic, copy) void (^confirmationChallengeConfirmationBlock)(BOOL confirmRequest);
@property (atomic, retain) NSDictionary *remoteNotificationUserInfo;
@property (atomic, retain) NSString *didCompleteOperationCallbackId;
@property (atomic, assign) BOOL _executing;
@property (atomic, assign) BOOL _finished;

-(id)initWithRemoteNotificationUserInfo:(NSDictionary *)userInfo;
-(void)sendOperationPluginResult:(CDVPluginResult *)result;
-(void)completeOperation;

@end
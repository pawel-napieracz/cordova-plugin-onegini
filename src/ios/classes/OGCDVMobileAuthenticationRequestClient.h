//  Copyright © 2016 Onegini. All rights reserved.

#import "CDVPlugin+OGCDV.h"
#import "OneginiSDK.h"

@interface OGCDVMobileAuthenticationRequestClient : CDVPlugin {
    NSOperationQueue *operationQueue;
    NSString *confirmationChallengeCallbackId;
    id delegate;
}

@property (nonatomic, retain) NSOperationQueue *operationQueue;
@property (nonatomic, retain) NSString *confirmationChallengeCallbackId;

+ (id)sharedInstance;
- (void)setDelegate:(id)newDelegate;
- (void)didReceiveRemoteNotification:(NSDictionary *)userInfo;
- (void)registerConfirmationChallengeReceiver:(CDVInvokedUrlCommand *)command;
- (void)replyToConfirmationChallenge:(CDVInvokedUrlCommand *)command;
- (void)sendConfirmationChallengePluginResult:(CDVPluginResult *)pluginResult;
- (void)sendMobileAuthenticationPluginResult:(NSDictionary *)result;

@end

@protocol OGCDVPluginMobileAuthenticationRequestDelegate
- (void)mobileAuthenticationRequestClient:(OGCDVMobileAuthenticationRequestClient *)mobileAuthenticationRequestClient
  didReceiveConfirmationChallengeResponse:(BOOL)response withCallbackId:(NSString *)callbackId;
@end
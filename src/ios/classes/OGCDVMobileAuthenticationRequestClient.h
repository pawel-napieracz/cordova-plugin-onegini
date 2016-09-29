//  Copyright © 2016 Onegini. All rights reserved.

#import "CDVPlugin+OGCDV.h"
#import "OneginiSDK.h"

@interface OGCDVMobileAuthenticationRequestClient : CDVPlugin {
    NSOperationQueue *queue;
    id delegate;
}

@property (nonatomic, copy) NSString *confirmationChallengeCallbackId;

+ (id)shared;
- (void)setDelegate:(id)aDelegate;
- (void)didReceiveRemoteNotification:(NSDictionary *)userInfo;
- (void)registerConfirmationChallengeReceiver:(CDVInvokedUrlCommand *)command;
- (void)replyToConfirmationChallenge:(CDVInvokedUrlCommand *)command;
- (void)sendConfirmationChallengePluginResult:(CDVPluginResult *)pluginResult;

@end

@protocol OGCDVPluginMobileAuthenticationRequestDelegate
- (void)mobileAuthenticationRequestClient:(OGCDVMobileAuthenticationRequestClient *)mobileAuthenticationRequestClient didReceiveConfirmationChallengeResponse:(BOOL)response;
@end
//  Copyright © 2016 Onegini. All rights reserved.

#import "CDVPlugin+OGCDV.h"
#import "OneginiSDK.h"

@interface OGCDVMobileAuthenticationRequestClient : CDVPlugin<ONGMobileAuthenticationRequestDelegate> {
    NSOperationQueue *operationQueue;
    NSMutableDictionary *challengeReceiversCallbackIds;
    NSDictionary *authenticationEventsForMethods;
    id delegate;
}

@property (nonatomic, retain) NSOperationQueue *operationQueue;
@property (nonatomic, retain) NSMutableDictionary *challengeReceiversCallbackIds;
@property (nonatomic, retain) NSDictionary *authenticationEventsForMethods;

+ (id)sharedInstance;
- (void)setDelegate:(id)newDelegate;
- (void)didReceiveRemoteNotification:(NSDictionary *)userInfo;
- (void)registerChallengeReceiver:(CDVInvokedUrlCommand *)command;
- (void)replyToChallenge:(CDVInvokedUrlCommand *)command;

@end

@protocol OGCDVPluginMobileAuthenticationRequestDelegate
- (void)mobileAuthenticationRequestClient:(OGCDVMobileAuthenticationRequestClient *)mobileAuthenticationRequestClient
  didReceiveConfirmationChallengeResponse:(BOOL)accept withCallbackId:(NSString *)callbackId;
- (void)mobileAuthenticationRequestClient:(OGCDVMobileAuthenticationRequestClient *)mobileAuthenticationRequestClient
           didReceivePinChallengeResponse:(BOOL)accept withPin:(NSString *)pin withCallbackId:(NSString *)callbackId;
- (void)mobileAuthenticationRequestClient:(OGCDVMobileAuthenticationRequestClient *)mobileAuthenticationRequestClient
   didReceiveFingerprintChallengeResponse:(BOOL)accept withCallbackId:(NSString *)callbackId;
- (void)completeOperation;
@end
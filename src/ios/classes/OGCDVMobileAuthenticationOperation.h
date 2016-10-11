//  Copyright © 2016 Onegini. All rights reserved.

#import "OneginiSDK.h"
#import "OGCDVMobileAuthenticationRequestClient.h"

@interface OGCDVMobileAuthenticationOperation : NSOperation<OGCDVPluginMobileAuthenticationRequestDelegate> {
}

@property (nonatomic, copy) void (^confirmationChallengeConfirmationBlock)(BOOL confirmRequest);
@property (atomic, retain) NSString *completeOperationCallbackId;
@property (atomic, retain) ONGMobileAuthenticationRequest *mobileAuthenticationRequest;
@property (atomic, retain) NSString *mobileAuthenticationMethod;
@property (atomic, retain) ONGPinChallenge *pinChallenge;
@property (atomic, assign) BOOL _executing;
@property (atomic, assign) BOOL _finished;

- (id)initWithConfirmationChallenge:(void (^)(BOOL confirmRequest))confirmation
                         forRequest:(ONGMobileAuthenticationRequest *)request
                          forMethod: (NSString *)method;
- (id)initWithPinChallenge:(ONGPinChallenge *)challenge
                forRequest:(ONGMobileAuthenticationRequest *)request
                 forMethod: (NSString *)method;
- (void)sendChallenge:(NSString *)callbackId;

@end
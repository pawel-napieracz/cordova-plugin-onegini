//  Copyright © 2016 Onegini. All rights reserved.

#import "OGCDVMobileAuthenticationOperation.h"
#import "OGCDVConstants.h"

@implementation OGCDVMobileAuthenticationOperation {
}

@synthesize mobileAuthenticationRequest;
@synthesize mobileAuthenticationMethod;
@synthesize completeOperationCallbackId;
@synthesize pinChallenge;

- (id)initWithConfirmationChallenge:(void (^)(BOOL confirmRequest))confirmation
                         forRequest:(ONGMobileAuthenticationRequest *)request
                          forMethod:(NSString *)method
{
    if (![super init]) {
        return nil;
    }

    [self setConfirmationChallengeConfirmationBlock:confirmation];
    [self setMobileAuthenticationMethod:method];
    [self setMobileAuthenticationRequest:request];

    return self;
}

- (id)initWithPinChallenge:(ONGPinChallenge *)challenge
                forRequest:(ONGMobileAuthenticationRequest *)request
                 forMethod:(NSString *)method;
{
    if (![super init]) {
        return nil;
    }

    [self setPinChallenge:challenge];
    [self setMobileAuthenticationMethod:method];
    [self setMobileAuthenticationRequest:request];

    return self;
}

- (void)start
{
    if ([self isCancelled]) {
        [self willChangeValueForKey:@"isFinished"];
        self._finished = YES;
        [self didChangeValueForKey:@"isFinished"];
        return;
    }

    [self willChangeValueForKey:@"isExecuting"];
    self._executing = YES;
    [self didChangeValueForKey:@"isExecuting"];

    [[OGCDVMobileAuthenticationRequestClient sharedInstance] performSelectorOnMainThread:@selector(setDelegate:)
                                                                              withObject:self
                                                                           waitUntilDone:YES];

    NSDictionary *challengeReceiversCallbackIds = [[OGCDVMobileAuthenticationRequestClient sharedInstance] challengeReceiversCallbackIds];
    NSString *challengeReceiverCallbackId = challengeReceiversCallbackIds[mobileAuthenticationMethod];

    if (challengeReceiverCallbackId) {
        [self sendChallenge:challengeReceiverCallbackId];
    } else {
        [[[OGCDVMobileAuthenticationRequestClient sharedInstance] challengeReceiversCallbackIds] addObserver:self
                                                                                                  forKeyPath:mobileAuthenticationMethod
                                                                                                     options:NSKeyValueObservingOptionNew
                                                                                                     context:NULL];
    }
}

- (void)observeValueForKeyPath:(NSString *)keyPath
                      ofObject:(id)object
                        change:(NSDictionary *)change
                       context:(void *)context
{

    if ([keyPath isEqualToString:mobileAuthenticationMethod]) {
        [[[OGCDVMobileAuthenticationRequestClient sharedInstance] challengeReceiversCallbackIds] removeObserver:self forKeyPath:mobileAuthenticationMethod];
        NSString *challengeReceiverCallbackId = change[NSKeyValueChangeNewKey];
        [self sendChallenge:challengeReceiverCallbackId];
    }
}

- (void)sendChallenge:(NSString *)callbackId
{
    NSDictionary *message = @{
        OGCDVPluginKeyType : mobileAuthenticationRequest.type,
        OGCDVPluginKeyMessage : mobileAuthenticationRequest.message,
        OGCDVPluginKeyProfileId : mobileAuthenticationRequest.userProfile.profileId
    };
    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:message];
    [pluginResult setKeepCallbackAsBool:YES];

    [[[OGCDVMobileAuthenticationRequestClient sharedInstance] commandDelegate] sendPluginResult:pluginResult callbackId:callbackId];
}

- (void)mobileAuthenticationRequestClient:(OGCDVMobileAuthenticationRequestClient *)mobileAuthenticationRequestClient didReceiveConfirmationChallengeResponse:(BOOL)response withCallbackId:(NSString *)callbackId
{
    [self setCompleteOperationCallbackId:callbackId];
    [self confirmationChallengeConfirmationBlock](response);
}

- (NSString *)getCompleteOperationCallbackId:(OGCDVMobileAuthenticationRequestClient *)mobileAuthenticationRequestClient
{
    return self.completeOperationCallbackId;
}

- (BOOL)isAsynchronous
{
    return YES;
}

- (BOOL)isExecuting
{
    return self._executing;
}

- (BOOL)isFinished
{
    return self._finished;
}

- (void)completeOperation
{
    [self willChangeValueForKey:@"isFinished"];
    [self willChangeValueForKey:@"isExecuting"];

    self._executing = NO;
    self._finished = YES;

    [self didChangeValueForKey:@"isFinished"];
    [self didChangeValueForKey:@"isExecuting"];
}

@end
//  Copyright © 2016 Onegini. All rights reserved.

#import "OGCDVMobileAuthenticationOperation.h"
#import "OGCDVConstants.h"

NSString *const OGCDVPluginKeyType = @"type";
NSString *const OGCDVPluginKeyMessage = @"message";

@implementation OGCDVMobileAuthenticationOperation {
}

@synthesize remoteNotificationUserInfo;
@synthesize didCompleteOperationCallbackId;

- (id)initWithRemoteNotificationUserInfo:(NSDictionary *)userInfo
{
    if (![super init])
        return nil;
    [self setRemoteNotificationUserInfo:userInfo];

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
    [[ONGUserClient sharedInstance] handleMobileAuthenticationRequest:remoteNotificationUserInfo delegate:self];
}

- (void)userClient:(ONGUserClient *)userClient didReceiveConfirmationChallenge:(void (^)(BOOL confirmRequest))confirmation forRequest:(ONGMobileAuthenticationRequest *)request
{
    self.confirmationChallengeConfirmationBlock = confirmation;

    NSDictionary *resultMessage = @{
        OGCDVPluginKeyType : request.type,
        OGCDVPluginKeyProfileId : request.userProfile.profileId,
        OGCDVPluginKeyMessage : request.message
    };

    CDVPluginResult *result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:resultMessage];

    [[OGCDVMobileAuthenticationRequestClient sharedInstance] performSelectorOnMainThread:@selector(sendConfirmationChallengePluginResult:)
                                                                              withObject:result
                                                                           waitUntilDone:YES];
}

- (void)userClient:(ONGUserClient *)userClient didReceivePinChallenge:(ONGPinChallenge *)challenge forRequest:(ONGMobileAuthenticationRequest *)request
{
}

- (void)mobileAuthenticationRequestClient:(OGCDVMobileAuthenticationRequestClient *)mobileAuthenticationRequestClient didReceiveConfirmationChallengeResponse:(BOOL)response withCallbackId:(NSString *)callbackId
{
    [self setDidCompleteOperationCallbackId:callbackId];
    [self confirmationChallengeConfirmationBlock](response);
}

- (void)userClient:(ONGUserClient *)userClient didFailToHandleMobileAuthenticationRequest:(ONGMobileAuthenticationRequest *)request error:(NSError *)error
{
    [self sendOperationPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR]];
    [self completeOperation];
}

- (void)userClient:(ONGUserClient *)userClient didHandleMobileAuthenticationRequest:(ONGMobileAuthenticationRequest *)request
{
    [self sendOperationPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK]];
    [self completeOperation];
}

- (void)sendOperationPluginResult:(CDVPluginResult *)result
{
    NSDictionary *arguments = @{
        @"pluginResult" : result,
        @"callbackId" : didCompleteOperationCallbackId,
    };

    [[OGCDVMobileAuthenticationRequestClient sharedInstance] performSelectorOnMainThread:@selector(sendMobileAuthenticationPluginResult:)
                                                                              withObject:arguments
                                                                           waitUntilDone:YES];
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
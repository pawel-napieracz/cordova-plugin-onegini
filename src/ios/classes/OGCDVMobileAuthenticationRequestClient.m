//  Copyright © 2016 Onegini. All rights reserved.

#import "OGCDVMobileAuthenticationRequestClient.h"
#import "OGCDVMobileAuthenticationOperation.h"
#import "OGCDVConstants.h"

NSString *const OGCDVPluginKeyAccept = @"accept";
NSString *const OGCDVPluginMobileAuthenticationMethodConfirmation = @"confirmation";
NSString *const OGCDVPluginMobileAuthenticationMethodPin = @"pin";
static OGCDVMobileAuthenticationRequestClient *sharedInstance;

@implementation OGCDVMobileAuthenticationRequestClient {
}

@synthesize operationQueue;
@synthesize challengeReceiversCallbackIds;

+ (id)sharedInstance
{
    return sharedInstance;
}

- (void)pluginInitialize
{
    operationQueue = [[NSOperationQueue alloc] init];
    [operationQueue setMaxConcurrentOperationCount:1];
    challengeReceiversCallbackIds = [[NSMutableDictionary alloc] init];
    sharedInstance = self;
}

- (void)setDelegate:(id)newDelegate
{
    delegate = newDelegate;
}

- (void)didReceiveRemoteNotification:(NSDictionary *)userInfo
{
    [[ONGUserClient sharedInstance] handleMobileAuthenticationRequest:userInfo delegate:self];
}

- (void)userClient:(ONGUserClient *)userClient didReceiveConfirmationChallenge:(void (^)(BOOL confirmRequest))confirmation forRequest:(ONGMobileAuthenticationRequest *)request
{
    OGCDVMobileAuthenticationOperation *operation = [[OGCDVMobileAuthenticationOperation alloc]
        initWithConfirmationChallenge:confirmation
                           forRequest:request
                            forMethod:OGCDVPluginMobileAuthenticationMethodConfirmation];
    [operationQueue addOperation:operation];
}

- (void)userClient:(ONGUserClient *)userClient didReceivePinChallenge:(ONGPinChallenge *)challenge forRequest:(ONGMobileAuthenticationRequest *)request
{
    OGCDVMobileAuthenticationOperation *operation = [[OGCDVMobileAuthenticationOperation alloc]
        initWithPinChallenge:challenge
                  forRequest:request
                   forMethod:OGCDVPluginMobileAuthenticationMethodPin];
    [operationQueue addOperation:operation];
}

- (void)userClient:(ONGUserClient *)userClient didFailToHandleMobileAuthenticationRequest:(ONGMobileAuthenticationRequest *)request error:(NSError *)error
{
    [self sendErrorResultForCallbackId:[delegate getCompleteOperationCallbackId:self] withError:error];
    [delegate completeOperation];
}

- (void)userClient:(ONGUserClient *)userClient didHandleMobileAuthenticationRequest:(ONGMobileAuthenticationRequest *)request
{
    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:[delegate getCompleteOperationCallbackId:self]];
    [delegate completeOperation];
}

- (void)registerChallengeReceiver:(CDVInvokedUrlCommand *)command
{
    NSDictionary *options = command.arguments[0];
    NSString *method = options[OGCDVPluginKeyMethod];
    challengeReceiversCallbackIds[method] = command.callbackId;
}

- (void)replyToChallenge:(CDVInvokedUrlCommand *)command
{
    NSDictionary *options = command.arguments[0];
    NSString *method = options[OGCDVPluginKeyMethod];

    if ([OGCDVPluginMobileAuthenticationMethodConfirmation isEqualToString:method]) {
        BOOL result = [options[OGCDVPluginKeyAccept] boolValue];
        [delegate mobileAuthenticationRequestClient:self didReceiveConfirmationChallengeResponse:result withCallbackId:command.callbackId];
    }
}

@end
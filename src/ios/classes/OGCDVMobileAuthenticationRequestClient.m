//  Copyright © 2016 Onegini. All rights reserved.

#import "OGCDVMobileAuthenticationRequestClient.h"
#import "OGCDVMobileAuthenticationOperation.h"

NSString *const OGCDVPluginKeyAccept = @"accept";
static OGCDVMobileAuthenticationRequestClient *sharedInstance;

@implementation OGCDVMobileAuthenticationRequestClient {
}

@synthesize operationQueue;
@synthesize confirmationChallengeCallbackId;

+ (id)sharedInstance
{
    return sharedInstance;
}

- (void)pluginInitialize
{
    operationQueue = [[NSOperationQueue alloc] init];
    [operationQueue setMaxConcurrentOperationCount:1];
    confirmationChallengeCallbackId = [[NSString alloc] init];
    sharedInstance = self;
}

- (void)setDelegate:(id)newDelegate
{
    delegate = newDelegate;
}

- (void)didReceiveRemoteNotification:(NSDictionary *)userInfo
{
    OGCDVMobileAuthenticationOperation *mobileAuthenticationOperation = [[OGCDVMobileAuthenticationOperation alloc] initWithRemoteNotificationUserInfo:userInfo];
    [operationQueue addOperation:mobileAuthenticationOperation];
}

- (void)registerConfirmationChallengeReceiver:(CDVInvokedUrlCommand *)command
{
    confirmationChallengeCallbackId = command.callbackId;
}

- (void)replyToConfirmationChallenge:(CDVInvokedUrlCommand *)command
{
    NSDictionary *options = command.arguments[0];
    BOOL result = [options[OGCDVPluginKeyAccept] boolValue];
    [delegate mobileAuthenticationRequestClient:self didReceiveConfirmationChallengeResponse:result withCallbackId:command.callbackId];
}

- (void)sendConfirmationChallengePluginResult:(CDVPluginResult *)pluginResult
{
    [pluginResult setKeepCallbackAsBool:YES];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:confirmationChallengeCallbackId];
}

-(void)sendMobileAuthenticationPluginResult:(NSDictionary *)result
{
    [self.commandDelegate sendPluginResult:result[@"pluginResult"] callbackId:result[@"callbackId"]];
}

@end
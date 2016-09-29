//  Copyright © 2016 Onegini. All rights reserved.

#import "OGCDVMobileAuthenticationRequestClient.h"
#import "OGCDVMobileAuthenticationOperation.h"

NSString *const OGCDVPluginKeyAccept = @"accept";

@implementation OGCDVMobileAuthenticationRequestClient {
}

static OGCDVMobileAuthenticationRequestClient *shared;

- (instancetype)init
{
    if (shared) {
        return shared;
    }
    if (![super init]) {
        return nil;
    }

    queue = [[NSOperationQueue alloc] init];
    queue.maxConcurrentOperationCount = 1;
    shared = self;
    return self;
}

+ (id)shared
{
    if (!shared) {
        [[OGCDVMobileAuthenticationRequestClient alloc] init];
    }

    return shared;
}

- (void)setDelegate:(id)aDelegate
{
    delegate = aDelegate;
}


- (void)didReceiveRemoteNotification:(NSDictionary *)userInfo
{
    OGCDVMobileAuthenticationOperation *operation = [[OGCDVMobileAuthenticationOperation alloc] initWithRemoteNotificationUserInfo:userInfo];
    [queue addOperation:operation];
}

- (void)registerConfirmationChallengeReceiver:(CDVInvokedUrlCommand *)command
{
    [self setConfirmationChallengeCallbackId:command.callbackId];
}

- (void)replyToConfirmationChallenge:(CDVInvokedUrlCommand *)command
{
    NSDictionary *options = command.arguments[0];
    BOOL result = [options[OGCDVPluginKeyAccept] boolValue];
    [delegate mobileAuthenticationRequestClient:self didReceiveConfirmationChallengeResponse:result];
}

- (void)sendConfirmationChallengePluginResult:(CDVPluginResult *)pluginResult
{
    [self.commandDelegate sendPluginResult:pluginResult callbackId:self.confirmationChallengeCallbackId];
}

@end
//  Copyright © 2016 Onegini. All rights reserved.

#import "OGCDVMobileAuthenticationOperation.h"
#import "OGCDVConstants.h"

NSString *const OGCDVPluginKeyType = @"type";
NSString *const OGCDVPluginKeyMessage = @"message";

@implementation OGCDVMobileAuthenticationOperation {
}

@synthesize remoteNotificationUserInfo;

- (id)initWithRemoteNotificationUserInfo:(NSDictionary *)userInfo
{
    if (![super init])
        return nil;
    [self setRemoteNotificationUserInfo:userInfo];

    return self;
}

- (void)main
{
    [[OGCDVMobileAuthenticationRequestClient shared] performSelectorOnMainThread:@selector(setDelegate:)
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

    [[OGCDVMobileAuthenticationRequestClient shared] performSelectorOnMainThread:@selector(sendConfirmationChallengePluginResult:)
                                                                      withObject:result
                                                                   waitUntilDone:YES];
}

- (void)userClient:(ONGUserClient *)userClient didReceivePinChallenge:(ONGPinChallenge *)challenge forRequest:(ONGMobileAuthenticationRequest *)request
{
}

- (void)mobileAuthenticationRequestClient:(OGCDVMobileAuthenticationRequestClient *)mobileAuthenticationRequestClient didReceiveConfirmationChallengeResponse:(BOOL)response
{
    self.confirmationChallengeConfirmationBlock(response);
}

@end
//  Copyright © 2016 Onegini. All rights reserved.

#import "CDVPlugin+OGCDV.h"
#import "OneginiSDK.h"

@interface OGCDVHandleMobileAuthenticationRequestClient : CDVPlugin<ONGMobileAuthenticationRequestDelegate>

@property (nonatomic, strong) NSString *confirmationListenerCallbackId;
@property(nonatomic, strong) void (^confirmationChallenge)(BOOL);
@property(nonatomic, strong) ONGMobileAuthenticationRequest *confirmationRequest;

- (void)registerConfirmationListener:(CDVInvokedUrlCommand *)command;
- (void)confirm:(CDVInvokedUrlCommand *)command;

- (void)handleMobileAuthenticationRequest:(NSDictionary *)userInfo;
@end

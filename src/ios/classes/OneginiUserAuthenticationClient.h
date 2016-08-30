//  Copyright © 2016 Onegini. All rights reserved.

#import "CDVPlugin+Onegini.h"
#import "OneginiSDK.h"

@interface OneginiUserAuthenticationClient : CDVPlugin<ONGAuthenticationDelegate>

@property (nonatomic, copy) NSString *startAuthenticationCallbackId;
@property (nonatomic, copy) NSString *checkPinCallbackId;
@property (nonatomic) ONGPinChallenge *pinChallenge;

- (void)startAuthentication:(CDVInvokedUrlCommand *)command;
- (void)checkPin:(CDVInvokedUrlCommand *)command;

@end

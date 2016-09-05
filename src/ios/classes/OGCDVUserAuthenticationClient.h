//  Copyright © 2016 Onegini. All rights reserved.

#import "CDVPlugin+OGCDV.h"
#import "OneginiSDK.h"

@interface OGCDVUserAuthenticationClient : CDVPlugin<ONGAuthenticationDelegate>

@property (nonatomic, copy) NSString *authenticationCallbackId;
@property (nonatomic, copy) NSString *checkPinCallbackId;
@property (nonatomic) ONGPinChallenge *pinChallenge;

- (void)start:(CDVInvokedUrlCommand *)command;
- (void)providePin:(CDVInvokedUrlCommand *)command;
- (void)reauthenticate:(CDVInvokedUrlCommand *)command;

@end

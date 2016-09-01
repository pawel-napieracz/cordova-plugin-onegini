//  Copyright © 2016 Onegini. All rights reserved.

#import "CDVPlugin+OGCDV.h"
#import "OneginiSDK.h"

@interface OGCDVUserRegistrationClient : CDVPlugin<ONGRegistrationDelegate, ONGPinValidationDelegate>

@property (nonatomic, copy) NSString *callbackId;
@property (nonatomic) ONGCreatePinChallenge *createPinChallenge;

- (void)start:(CDVInvokedUrlCommand *)command;
- (void)createPin:(CDVInvokedUrlCommand *)command;
- (void)getUserProfiles:(CDVInvokedUrlCommand *)command;

@end

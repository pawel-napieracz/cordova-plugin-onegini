//  Copyright © 2016 Onegini. All rights reserved.

#import "CDVPlugin+Onegini.h"
#import "OneginiSDK.h"

@interface OneginiUserRegistrationClient : CDVPlugin<ONGRegistrationDelegate, ONGPinValidationDelegate>

@property (nonatomic, copy) NSString *callbackId;
@property (nonatomic) ONGCreatePinChallenge *challenge;

- (void)startRegistration:(CDVInvokedUrlCommand *)command;
- (void)createPin:(CDVInvokedUrlCommand *)command;
- (void)getUserProfiles:(CDVInvokedUrlCommand *)command;

@end

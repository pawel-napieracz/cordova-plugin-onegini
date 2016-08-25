//  Copyright © 2016 Onegini. All rights reserved.

#import <Cordova/CDVPlugin.h>
#import "OneginiSDK.h"

@interface OneginiUserRegistrationClient : CDVPlugin<ONGRegistrationDelegate, ONGPinValidationDelegate>

@property (nonatomic, copy) NSString *callbackId;
@property (nonatomic) ONGCreatePinChallenge *challenge;

- (void)generateChallenge:(CDVInvokedUrlCommand *)command;
- (void)setPIN:(CDVInvokedUrlCommand *)command;
// TODO this would be a nice spot for 'isRegistered'

@end

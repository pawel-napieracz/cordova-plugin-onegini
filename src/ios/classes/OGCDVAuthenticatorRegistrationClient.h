//  Copyright © 2016 Onegini. All rights reserved.

#import "CDVPlugin+OGCDV.h"
#import "OneginiSDK.h"

// TODO see if we can extract the ONGAuthenticationDelegate & providePin bits (shared with OGCDVUserAuthenticationClient)
@interface OGCDVAuthenticatorRegistrationClient : CDVPlugin<ONGAuthenticationDelegate>

@property (nonatomic, copy) NSString *authenticationCallbackId;
@property (nonatomic, copy) NSString *checkPinCallbackId;
@property (nonatomic) ONGPinChallenge *pinChallenge;

- (void)start:(CDVInvokedUrlCommand *)command;
- (void)providePin:(CDVInvokedUrlCommand *)command;

@end

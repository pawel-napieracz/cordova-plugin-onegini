//  Copyright © 2016 Onegini. All rights reserved.

#import "CDVPlugin+OGCDV.h"
#import "OneginiSDK.h"

@interface OGCDVAuthenticationDelegateHandler : CDVPlugin<ONGAuthenticationDelegate>

@property (nonatomic, copy) NSString *authenticationCallbackId;
@property (nonatomic, copy) NSString *checkPinCallbackId;
@property (nonatomic) ONGPinChallenge *pinChallenge;

@end
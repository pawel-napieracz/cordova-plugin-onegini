//  Copyright © 2016 Onegini. All rights reserved.

#import "CDVPlugin+OGCDV.h"
#import "OneginiSDK.h"

@interface OGCDVAuthenticationDelegateHandler : CDVPlugin<ONGAuthenticationDelegate>

@property (nonatomic, copy) NSString *authenticationCallbackId;
@property (nonatomic) ONGPinChallenge *pinChallenge;
@property (nonatomic) ONGFingerprintChallenge *fingerprintChallenge;

@end
//  Copyright © 2016 Onegini. All rights reserved.

#import "CDVPlugin+OGCDV.h"
#import "OneginiSDK.h"

@interface OGCDVChangePinClient : CDVPlugin<ONGChangePinDelegate>

@property (nonatomic, copy) NSString *startCallbackId;
@property (nonatomic, copy) NSString *createPinCallbackId;

@property (nonatomic, copy) NSString *currentPin;
@property (nonatomic) ONGPinChallenge *currentPinChallenge;

@property (nonatomic) ONGCreatePinChallenge *createPinChallenge;

- (void)start:(CDVInvokedUrlCommand *)command;
- (void)createPin:(CDVInvokedUrlCommand *)command;

@end

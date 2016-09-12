//  Copyright © 2016 Onegini. All rights reserved.

#import "CDVPlugin+OGCDV.h"
#import "OneginiSDK.h"

@interface OGCDVUserClient : CDVPlugin

- (void)validatePinWithPolicy:(CDVInvokedUrlCommand *)command;
- (void)getRegisteredAuthenticators:(CDVInvokedUrlCommand *)command;
- (void)getNotRegisteredAuthenticators:(CDVInvokedUrlCommand *)command;

@end

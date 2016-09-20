//  Copyright © 2016 Onegini. All rights reserved.

#import "CDVPlugin+OGCDV.h"
#import "OneginiSDK.h"

@interface OGCDVAuthenticatorsClient : CDVPlugin

- (void)getRegistered:(CDVInvokedUrlCommand *)command;
- (void)getNotRegistered:(CDVInvokedUrlCommand *)command;
- (void)setPreferred:(CDVInvokedUrlCommand *)command;

@end

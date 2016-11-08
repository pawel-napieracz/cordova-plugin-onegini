//  Copyright © 2016 Onegini. All rights reserved.

#import "CDVPlugin+OGCDV.h"
#import "OneginiSDK.h"

@interface OGCDVAuthenticatorsClient : CDVPlugin

- (void)getAll:(CDVInvokedUrlCommand *)command;
- (void)getRegistered:(CDVInvokedUrlCommand *)command;
- (void)getNotRegistered:(CDVInvokedUrlCommand *)command;
- (void)getPreferred:(CDVInvokedUrlCommand *)command;
- (void)setPreferred:(CDVInvokedUrlCommand *)command;

@end

//  Copyright © 2016 Onegini. All rights reserved.

#import "CDVPlugin+OGCDV.h"
#import "OneginiSDK.h"

@interface OGCDVDeviceAuthenticationClient : CDVPlugin

- (void)authenticate:(CDVInvokedUrlCommand *)command;

@end

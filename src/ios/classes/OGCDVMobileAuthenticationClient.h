//  Copyright © 2016 Onegini. All rights reserved.

#import "CDVPlugin+OGCDV.h"
#import "OneginiSDK.h"

@interface OGCDVMobileAuthenticationClient : CDVPlugin

@property (nonatomic, copy) NSString *enrollCallbackId;

- (void)enroll:(CDVInvokedUrlCommand *)command;

@end

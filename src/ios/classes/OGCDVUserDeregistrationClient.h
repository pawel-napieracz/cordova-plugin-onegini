//  Copyright © 2016 Onegini. All rights reserved.

#import "CDVPlugin+OGCDV.h"
#import "OneginiSDK.h"

@interface OGCDVUserDeregistrationClient : CDVPlugin<ONGDeregistrationDelegate>

@property (nonatomic, copy) NSString *callbackId;

- (void)deregister:(CDVInvokedUrlCommand *)command;

@end

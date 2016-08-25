//  Copyright © 2016 Onegini. All rights reserved.

#import <Cordova/CDVPlugin.h>
#import "OneginiSDK.h"

@interface OneginiClient : CDVPlugin

- (void)start:(CDVInvokedUrlCommand *)command;

@end

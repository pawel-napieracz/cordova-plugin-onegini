//  Copyright © 2016 Onegini. All rights reserved.

#import <Cordova/CDVPlugin.h>

@interface CDVPlugin (OGCDV)

- (void) sendErrorResultForCallbackId:(NSString *)callbackId withErrorCode:(int)code andMessage:(NSString *)errorMessage;
- (void) sendErrorResultForCallbackId:(NSString *)callbackId withError:(NSError *)error;
- (void) sendErrorResultForCallbackId:(NSString *)callbackId withMessage:(NSString *)errorMessage;

@end

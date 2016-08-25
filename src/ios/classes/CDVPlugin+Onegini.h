//  Copyright © 2016 Onegini. All rights reserved.

#import <Cordova/CDVPlugin.h>

@interface CDVPlugin (Onegini)

- (void) sendErrorResultForCallbackId:(NSString *)callbackId withError:(NSError *)error;
- (void) sendErrorResultForCallbackId:(NSString *)callbackId withMessage:(NSString *)errorMessage;

@end

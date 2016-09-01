//  Copyright © 2016 Onegini. All rights reserved.

#import "CDVPlugin+OGCDV.h"

@implementation CDVPlugin (Onegini)

- (void) sendErrorResultForCallbackId:(NSString *)callbackId withMessage:(NSString *)errorMessage
{
  [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsDictionary:@{@"description": errorMessage}] callbackId:callbackId];
}

- (void) sendErrorResultForCallbackId:(NSString *)callbackId withError:(NSError *)error
{
  NSString *errorMessage = [NSString stringWithFormat: @"%@\n%@", error.localizedDescription, error.localizedRecoverySuggestion];
  [self sendErrorResultForCallbackId:callbackId withMessage:errorMessage];
}

@end

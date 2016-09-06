//  Copyright © 2016 Onegini. All rights reserved.

#import "CDVPlugin+OGCDV.h"

@implementation CDVPlugin (OGCDV)

- (void) sendErrorResultForCallbackId:(NSString *)callbackId withMessage:(NSString *)errorMessage
{
  [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsDictionary:@{@"description": errorMessage}] callbackId:callbackId];
}

- (void) sendErrorResultForCallbackId:(NSString *)callbackId withError:(NSError *)error
{
  NSString *errorMessage;
  if (error == nil) {
    errorMessage = @"An unknown error occurred.";
  } else {
    errorMessage = [NSString stringWithFormat: @"%@\n%@", error.localizedDescription, error.localizedRecoverySuggestion];
  }
  [self sendErrorResultForCallbackId:callbackId withMessage:errorMessage];
}

@end

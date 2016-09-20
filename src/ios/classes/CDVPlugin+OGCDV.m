//  Copyright © 2016 Onegini. All rights reserved.

#import "CDVPlugin+OGCDV.h"

@implementation CDVPlugin (OGCDV)

- (void) sendErrorResultForCallbackId:(NSString *)callbackId withMessage:(NSString *)errorMessage
{
  [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsDictionary:@{@"description": errorMessage}] callbackId:callbackId];
}

- (void) sendErrorResultForCallbackId:(NSString *)callbackId withErrorCode:(long)code andMessage:(NSString *)errorMessage
{
  NSDictionary *result = @{
      @"code": @(code),
      @"description": errorMessage
  };
  [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsDictionary:result] callbackId:callbackId];
}

- (void) sendErrorResultForCallbackId:(NSString *)callbackId withError:(NSError *)error
{
  NSString *errorMessage;
  long errorCode;
  if (error == nil) {
    errorMessage = @"An unknown error occurred.";
    errorCode = -1;
  } else {
    errorMessage = [NSString stringWithFormat: @"%@\n%@", error.localizedDescription, error.localizedRecoverySuggestion];
    errorCode = (long)error.code;
  }
  [self sendErrorResultForCallbackId:callbackId withErrorCode:errorCode andMessage:errorMessage];
}

@end

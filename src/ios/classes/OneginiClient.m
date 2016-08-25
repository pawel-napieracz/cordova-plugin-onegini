//  Copyright © 2016 Onegini. All rights reserved.

#import "OneginiClient.h"

@implementation OneginiClient {}

- (void)start:(CDVInvokedUrlCommand*)command
{
  [[ONGClientBuilder new] build];

  [[ONGClient sharedInstance] start:^(BOOL result, NSError *error) {
    if (error != nil) {
      if (ONGGenericErrorOutdatedApplication == error.code) {
        [self sendErrorResultForCallbackId:command.callbackId withMessage:@"The application version is no longer valid, please visit the app store to update your application."];
      }

      if (ONGGenericErrorOutdatedOS == error.code) {
        [self sendErrorResultForCallbackId:command.callbackId withMessage:@"The operating system that you use is no longer valid, please update your OS."];
      }
    } else {
      [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK] callbackId:command.callbackId];
    }
  }];
}

@end

//  Copyright © 2016 Onegini. All rights reserved.

#import "OneginiClient.h"

@implementation OneginiClient {}

- (void)start:(CDVInvokedUrlCommand*)command
{
  [[ONGClientBuilder new] build];

  [[ONGClient sharedInstance] start:^(BOOL result, NSError *error) {
    if (error != nil) {
      if (ONGGenericErrorOutdatedApplication == error.code) {
        [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsDictionary:@{@"description":@"The application version is no longer valid, please visit the app store to update your application."}] callbackId:command.callbackId];
      }

      if (ONGGenericErrorOutdatedOS == error.code) {
        [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsDictionary:@{@"description":@"The operating system that you use is no longer valid, please update your OS."}] callbackId:command.callbackId];
      }
    } else {
      [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK] callbackId:command.callbackId];
    }
  }];
}

@end

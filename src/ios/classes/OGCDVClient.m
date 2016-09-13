//  Copyright © 2016 Onegini. All rights reserved.

#import "OGCDVClient.h"
#import "OGCDVConstants.h"
#import "OneginiConfigModel.h"

@implementation OGCDVClient {}

- (void)start:(CDVInvokedUrlCommand*)command
{
  [self.commandDelegate runInBackground:^{

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
        NSDictionary *result = @{OGCDVPluginKeyResourceBaseURL: OneginiConfigModel.configuration[ONGResourceBaseURL]};
        [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:result] callbackId:command.callbackId];
      }
    }];
  }];
}

@end

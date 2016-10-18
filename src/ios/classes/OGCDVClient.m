//  Copyright © 2016 Onegini. All rights reserved.

#import <Cordova/CDVViewController.h>
#import "OGCDVClient.h"
#import "OGCDVConstants.h"
#import "OneginiConfigModel.h"
#import "OGCDVMobileAuthenticationClient.h"

@implementation OGCDVClient {
}

- (void)start:(CDVInvokedUrlCommand *)command
{
  [self.commandDelegate runInBackground:^{
      [[[ONGClientBuilder new] build] start:^(BOOL result, NSError *error) {
          if (error != nil) {
            if (ONGGenericErrorOutdatedApplication == error.code) {
              [self sendErrorResultForCallbackId:command.callbackId withMessage:@"The application version is no longer valid, please visit the app store to update your application."];
            }

            if (ONGGenericErrorOutdatedOS == error.code) {
              [self sendErrorResultForCallbackId:command.callbackId withMessage:@"The operating system that you use is no longer valid, please update your OS."];
            }
          } else {
            OGCDVMobileAuthenticationClient *mobileAuthClient = [(CDVViewController *) self.viewController getCommandInstance:OGCDVPluginClassMobileAuthenticationClient];
            if (mobileAuthClient.pendingDeviceToken != nil) {
              [[ONGUserClient sharedInstance] storeDevicePushTokenInSession:mobileAuthClient.pendingDeviceToken];
              mobileAuthClient.pendingDeviceToken = nil;
            }
            NSDictionary *config = @{OGCDVPluginKeyResourceBaseURL: OneginiConfigModel.configuration[ONGResourceBaseURL]};
            [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:config] callbackId:command.callbackId];
          }
      }];
  }];
}

@end

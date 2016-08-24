#import "OneginiClient.h"

@implementation OneginiClient {}

#pragma mark -
#pragma mark overrides

- (void)start:(CDVInvokedUrlCommand*)command
{
#ifdef DEBUG
    [CDVPluginResult setVerbose:YES];
#endif

  [[ONGClientBuilder new] build];

  [[ONGClient sharedInstance] start:^(BOOL result, NSError *error) {
    if (error != nil) {
      if (ONGGenericErrorOutdatedApplication == error.code) {
        [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"The application version is no longer valid, please visit the app store to update your application."] callbackId:command.callbackId];
      }

      if (ONGGenericErrorOutdatedOS == error.code) {
        [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"The operating system that you use is no longer valid, please update your OS."] callbackId:command.callbackId];
      }
    }
  }];
}

@end

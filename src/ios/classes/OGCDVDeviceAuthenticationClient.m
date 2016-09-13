//  Copyright © 2016 Onegini. All rights reserved.

#import "OGCDVDeviceAuthenticationClient.h"
#import "OGCDVConstants.h"

@implementation OGCDVDeviceAuthenticationClient {}

- (void)authenticate:(CDVInvokedUrlCommand*)command
{
  [self.commandDelegate runInBackground:^{
    NSArray<NSString*> *optionalScopes = nil;
    if (command.arguments.count > 0) {
      NSDictionary *options = [command.arguments objectAtIndex:0];
      optionalScopes = options[OGCDVPluginKeyScopes];
    }

    [[ONGDeviceClient sharedInstance] authenticateDevice:optionalScopes completion:^(BOOL success, NSError * _Nullable error) {
      if (error != nil || !success) {
        [self sendErrorResultForCallbackId:command.callbackId withError:error];
      } else {
        [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK] callbackId:command.callbackId];
      }
    }];
  }];
}

@end

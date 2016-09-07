//  Copyright © 2016 Onegini. All rights reserved.

#import "OGCDVUserClient.h"

// TODO move to the helper class' interface? (export)
static NSString *const OGCDVPluginKeyPin = @"pin";

@implementation OGCDVUserClient {}

- (void)validatePinWithPolicy:(CDVInvokedUrlCommand*)command
{
  [self.commandDelegate runInBackground:^{
    NSDictionary *options = [command.arguments objectAtIndex:0];
    NSString *pin = options[OGCDVPluginKeyPin];

    [[ONGUserClient sharedInstance] validatePinWithPolicy:pin completion:^(BOOL valid, NSError * _Nullable error) {
      if (error != nil || !valid) {
        [self sendErrorResultForCallbackId:command.callbackId withError:error];
      } else {
        [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK] callbackId:command.callbackId];
      }
    }];
  }];
}

@end

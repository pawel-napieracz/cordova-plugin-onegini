//  Copyright © 2016 Onegini. All rights reserved.

#import "OGCDVChangePinClient.h"
#import "OGCDVUserClientHelper.h"

static NSString *const OGCDVPluginKeyProfileId = @"profileId";
static NSString *const OGCDVPluginKeyPin = @"pin";
static NSString *const OGCDVPluginKeyMaxFailureCount = @"maxFailureCount";
static NSString *const OGCDVPluginKeyRemainingFailureCount = @"remainingFailureCount";
static NSString *const OGCDVPluginKeyPinLength = @"pinLength";

@implementation OGCDVChangePinClient {}

- (void)start:(CDVInvokedUrlCommand*)command
{
  [self.commandDelegate runInBackground:^{
    self.startCallbackId = command.callbackId;
    NSDictionary *options = [command.arguments objectAtIndex:0];
    self.currentPin = options[OGCDVPluginKeyPin];

    if (self.currentPinChallenge != nil) {
      [self.currentPinChallenge.sender respondWithPin:self.currentPin challenge:self.currentPinChallenge];
    } else {
      [[ONGUserClient sharedInstance] changePin:self];
    }
  }];
}

- (void)createPin:(CDVInvokedUrlCommand*)command
{
  self.startCallbackId = nil;

  if (!self.createPinChallenge) {
    [self sendErrorResultForCallbackId:command.callbackId withMessage:@"Onegini: please invoke 'onegini.user.changePin.start' first."];
    return;
  }

  self.createPinCallbackId = command.callbackId;

  [self.commandDelegate runInBackground:^{
    NSDictionary *options = [command.arguments objectAtIndex:0];
    NSString *pin = options[OGCDVPluginKeyPin];
    [self.createPinChallenge.sender respondWithCreatedPin:pin challenge:self.createPinChallenge];
  }];
}

#pragma mark - ONGChangePinDelegate

-(void)userClient:(ONGUserClient *)userClient didReceivePinChallenge:(ONGPinChallenge *)challenge
{
  self.currentPinChallenge = challenge;

  if (challenge.error != nil) {
    NSDictionary *result = @{
                             OGCDVPluginKeyMaxFailureCount:@(challenge.maxFailureCount),
                             OGCDVPluginKeyRemainingFailureCount:@(challenge.remainingFailureCount),
                             @"description": [NSString stringWithFormat:@"Onegini: Incorrect Pin. Check the %@ and %@ properties for details.", OGCDVPluginKeyMaxFailureCount, OGCDVPluginKeyRemainingFailureCount]
                             };

    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsDictionary:result];
    [pluginResult setKeepCallbackAsBool:YES];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:self.startCallbackId];
    return;
  }

  [challenge.sender respondWithPin:self.currentPin challenge:challenge];
}

- (void)userClient:(ONGUserClient *)userClient didReceiveCreatePinChallenge:(ONGCreatePinChallenge *)challenge
{
  if (challenge.error != nil) {
    [self sendErrorResultForCallbackId:self.startCallbackId != nil ? self.startCallbackId : self.createPinCallbackId withError:challenge.error];
    return;
  }

  self.createPinChallenge = challenge;

  NSDictionary *result = @{OGCDVPluginKeyPinLength:@(challenge.pinLength)};
  [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:result] callbackId:self.startCallbackId];
}

- (void)userClient:(ONGUserClient *)userClient didChangePinForUser:(ONGUserProfile *)userProfile
{
  [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK] callbackId:self.createPinCallbackId];
  self.createPinChallenge = nil;
  self.createPinCallbackId = nil;
}

- (void)userClient:(ONGUserClient *)userClient didFailToChangePinForUser:(ONGUserProfile *)userProfile error:(NSError *)error
{
  [self sendErrorResultForCallbackId:self.startCallbackId != nil ? self.startCallbackId : self.createPinCallbackId withError:error];
}

@end

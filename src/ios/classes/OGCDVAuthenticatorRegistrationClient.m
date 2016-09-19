//  Copyright © 2016 Onegini. All rights reserved.

#import "OGCDVAuthenticatorRegistrationClient.h"
#import "OGCDVConstants.h"

@implementation OGCDVAuthenticatorRegistrationClient {
}

- (void)start:(CDVInvokedUrlCommand *)command
{
  [self.commandDelegate runInBackground:^{
      ONGUserProfile *user = [[ONGUserClient sharedInstance] authenticatedUserProfile];
      if (user == nil) {
        [self sendErrorResultForCallbackId:command.callbackId withMessage:@"Onegini: No user authenticated."];
        return;
      }

      NSDictionary *options = command.arguments[0];
      NSString *authenticatorId = options[OGCDVPluginKeyAuthenticatorId];

      NSSet<ONGAuthenticator *> *nonRegisteredAuthenticators = [[ONGUserClient sharedInstance] nonRegisteredAuthenticatorsForUser:user];
      for (ONGAuthenticator *authenticator in nonRegisteredAuthenticators) {
        if ([authenticator.identifier isEqualToString:authenticatorId]) {
          [[ONGUserClient sharedInstance] registerAuthenticator:authenticator delegate:self];
          break;
        }
      }
      [self sendErrorResultForCallbackId:command.callbackId withMessage:@"Onegini: No authenticator found."];
  }];
}

- (void)providePin:(CDVInvokedUrlCommand *)command
{
  if (!self.pinChallenge) {
    [self sendErrorResultForCallbackId:command.callbackId withMessage:@"Onegini: please invoke 'start' first."];
    return;
  }

  self.checkPinCallbackId = command.callbackId;
  NSDictionary *options = command.arguments[0];
  NSString *pin = options[OGCDVPluginKeyPin];
  [self.pinChallenge.sender respondWithPin:pin challenge:self.pinChallenge];
}

#pragma mark - ONGAuthenticationDelegate

-(void)userClient:(ONGUserClient *)userClient didAuthenticateUser:(ONGUserProfile *)userProfile
{
  self.pinChallenge = nil;
  [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK] callbackId:self.checkPinCallbackId];
}

-(void)userClient:(ONGUserClient *)userClient didFailToAuthenticateUser:(ONGUserProfile *)userProfile error:(NSError *)error
{
  // May be called when the Pin is incorrect <max> times, but also when running
  // 'startAuthentication' twice (which is incorrect usage).
  // But this is why we have the 'deregistered' property and the check on 'self.startAuthenticationCallbackId'.

  if (self.authenticationCallbackId) {
    [self sendErrorResultForCallbackId:self.authenticationCallbackId withMessage:@"Don't call 'startAuthentication' twice, call 'checkPin' instead."];
    return;
  }

  NSDictionary *result = @{
      @"description": error.localizedDescription,
      OGCDVPluginKeyRemainingFailureCount: @(0)
  };

  [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsDictionary:result] callbackId:self.checkPinCallbackId];
}

-(void)userClient:(ONGUserClient *)userClient didReceivePinChallenge:(ONGPinChallenge *)challenge
{
  self.pinChallenge = challenge;

  if (challenge.error != nil) {
    NSDictionary *result = @{
        OGCDVPluginKeyMaxFailureCount:@(challenge.maxFailureCount),
        OGCDVPluginKeyRemainingFailureCount:@(challenge.remainingFailureCount),
        @"description": [NSString stringWithFormat:@"Onegini: Incorrect Pin. Check the %@ and %@ properties for details.", OGCDVPluginKeyMaxFailureCount, OGCDVPluginKeyRemainingFailureCount]
    };

    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsDictionary:result];
    [pluginResult setKeepCallbackAsBool:YES];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:self.checkPinCallbackId];
    return;
  }

  CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
  [self.commandDelegate sendPluginResult:pluginResult callbackId:self.authenticationCallbackId];
  self.authenticationCallbackId = nil;
}


@end

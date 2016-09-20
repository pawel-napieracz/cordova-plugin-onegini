//  Copyright © 2016 Onegini. All rights reserved.

#import "OGCDVAuthenticatorRegistrationClient.h"
#import "OGCDVConstants.h"

@implementation OGCDVAuthenticatorRegistrationClient {
}

- (void)registerNew:(CDVInvokedUrlCommand *)command
{
  [self.commandDelegate runInBackground:^{
      ONGUserProfile *user = [[ONGUserClient sharedInstance] authenticatedUserProfile];
      if (user == nil) {
        [self sendErrorResultForCallbackId:command.callbackId withMessage:OGCDVPluginErrorKeyNoUserAuthenticated];
        return;
      }

      self.authenticationCallbackId = command.callbackId;

      NSDictionary *options = command.arguments[0];
      NSString *authenticatorId = options[OGCDVPluginKeyAuthenticatorId];

      NSSet<ONGAuthenticator *> *nonRegisteredAuthenticators = [[ONGUserClient sharedInstance] nonRegisteredAuthenticatorsForUser:user];
      for (ONGAuthenticator *authenticator in nonRegisteredAuthenticators) {
        if ([authenticator.identifier isEqualToString:authenticatorId]) {
          [[ONGUserClient sharedInstance] registerAuthenticator:authenticator delegate:self];
          return;
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

@end

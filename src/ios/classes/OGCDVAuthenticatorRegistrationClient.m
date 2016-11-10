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
            [self sendErrorResultForCallbackId:command.callbackId withErrorCode:OGCDVPluginErrCodeNoUserAuthenticated andMessage:OGCDVPluginErrDescriptionNoUserAuthenticated];
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

        [self sendErrorResultForCallbackId:command.callbackId withErrorCode:OGCDVPluginErrCodeNoSuchAuthenticator andMessage:OGCDVPluginErrDescriptionNoSuchAuthenticator];
    }];
}

- (void)providePin:(CDVInvokedUrlCommand *)command
{
    if (!self.pinChallenge) {
        [self sendErrorResultForCallbackId:command.callbackId withErrorCode:OGCDVPluginErrCodeNoPinChallenge andMessage:OGCDVPluginErrDescriptionNoPinChallenge];
        return;
    }

  NSDictionary *options = command.arguments[0];
  NSString *pin = options[OGCDVPluginKeyPin];
  [self.pinChallenge.sender respondWithPin:pin challenge:self.pinChallenge];
}

- (void)deregister:(CDVInvokedUrlCommand *)command
{
  [self.commandDelegate runInBackground:^{
      ONGUserProfile *user = [[ONGUserClient sharedInstance] authenticatedUserProfile];
      if (user == nil) {
        [self sendErrorResultForCallbackId:command.callbackId withMessage:OGCDVPluginErrDescriptionNoUserAuthenticated];
        return;
      }

      NSDictionary *options = command.arguments[0];
      NSString *authenticatorId = options[OGCDVPluginKeyAuthenticatorId];

      NSSet<ONGAuthenticator *> *registeredAuthenticators = [[ONGUserClient sharedInstance] registeredAuthenticatorsForUser:user];
      for (ONGAuthenticator *authenticator in registeredAuthenticators) {
        if ([authenticator.identifier isEqualToString:authenticatorId]) {
          [[ONGUserClient sharedInstance] deregisterAuthenticator:authenticator completion:^(BOOL deregistered, NSError * _Nullable error) {
            if (error || !deregistered) {
              [self sendErrorResultForCallbackId:command.callbackId withError:error];
            } else {
              [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK] callbackId:command.callbackId];
            }
          }];
          return;
        }
      }

      [self sendErrorResultForCallbackId:command.callbackId withErrorCode:OGCDVPluginErrCodeNoSuchAuthenticator andMessage:OGCDVPluginErrDescriptionNoSuchAuthenticator];
  }];
}

@end

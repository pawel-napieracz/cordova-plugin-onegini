//  Copyright © 2016 Onegini. All rights reserved.

#import "OGCDVHandleMobileAuthenticationRequestClient.h"
#import "OGCDVConstants.h"

@implementation OGCDVHandleMobileAuthenticationRequestClient {}

NSString *const OGCDVPluginKeyConfirmationResponse = @"response";

- (void)registerConfirmationListener:(CDVInvokedUrlCommand*)command
{
  // we're not invoking any callbacks
  self.confirmationListenerCallbackId = command.callbackId;
  [self processPendingConfirmationRequest];
}

- (void)confirm:(CDVInvokedUrlCommand*)command
{
  [self.commandDelegate runInBackground:^{
      if (self.confirmationChallenge == nil) {
        [self sendErrorResultForCallbackId:command.callbackId withMessage:@"Onegini: no confirmation challenge received."];
        return;
      };

      NSDictionary *options = command.arguments[0];
      BOOL response = [options[OGCDVPluginKeyConfirmationResponse] boolValue];

      self.confirmationChallenge(response);
      self.confirmationChallenge = nil;

      [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK] callbackId:command.callbackId];
  }];
}

- (void)handleMobileAuthenticationRequest:(NSDictionary *)userInfo
{
  if (userInfo != nil) {
    [[ONGUserClient sharedInstance] handleMobileAuthenticationRequest:userInfo delegate:self];
  }
}


#pragma mark - ONGMobileAuthenticationRequestDelegate

- (void)userClient:(ONGUserClient *)userClient didReceiveConfirmationChallenge:(void (^)(BOOL confirmRequest))confirmation forRequest:(ONGMobileAuthenticationRequest *)request
{
  // remember this while we're waiting for user response
  self.confirmationChallenge = confirmation;
  self.confirmationRequest = request;
  [self processPendingConfirmationRequest];
}

- (void) processPendingConfirmationRequest
{
  if (self.confirmationRequest == nil) {
    return;
  }

  NSDictionary *result = @{
      OGCDVPluginKeyProfileId: self.confirmationRequest.userProfile.profileId,
      OGCDVPluginKeyConfirmationType: self.confirmationRequest.type,
      OGCDVPluginKeyConfirmationMessage: self.confirmationRequest.message
  };

  CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:result];
  [pluginResult setKeepCallbackAsBool:YES];
  [self.commandDelegate sendPluginResult:pluginResult callbackId:self.confirmationListenerCallbackId];

  self.confirmationRequest = nil;
}

- (void)userClient:(ONGUserClient *)userClient didReceivePinChallenge:(ONGPinChallenge *)challenge forRequest:(ONGMobileAuthenticationRequest *)request
{
  // mandatory, but not currently used
}

@end

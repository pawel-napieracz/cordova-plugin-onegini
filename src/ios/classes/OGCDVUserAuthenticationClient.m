//  Copyright © 2016 Onegini. All rights reserved.

#import "OGCDVUserAuthenticationClient.h"
#import "OGCDVUserClientHelper.h"
#import "AppDelegate.h"

static NSString *const OGCDVPluginKeyProfileId = @"profileId";
static NSString *const OGCDVPluginKeyPin = @"pin";
static NSString *const OGCDVPluginKeyMaxFailureCount = @"maxFailureCount";
static NSString *const OGCDVPluginKeyRemainingFailureCount = @"remainingFailureCount";

@implementation OGCDVUserAuthenticationClient {}

- (void)getAuthenticatedUserProfile:(CDVInvokedUrlCommand *)command
{
  ONGUserProfile *authenticatedUserProfile = [[ONGUserClient sharedInstance] authenticatedUserProfile];
  if (authenticatedUserProfile == nil) {
    [self sendErrorResultForCallbackId:command.callbackId withMessage:@"Onegini: No user authenticated."];
  } else {
    NSDictionary *result = @{OGCDVPluginKeyProfileId: authenticatedUserProfile.profileId};
    [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:result] callbackId:command.callbackId];
  }
}

- (void)start:(CDVInvokedUrlCommand *)command
{
  NSDictionary *options = [command.arguments objectAtIndex:0];
  NSString *profileId = options[OGCDVPluginKeyProfileId];

  ONGUserProfile *authenticatedUserProfile = [[ONGUserClient sharedInstance] authenticatedUserProfile];
  if (authenticatedUserProfile && [authenticatedUserProfile.profileId isEqualToString:profileId]) {
    [self sendErrorResultForCallbackId:command.callbackId withMessage:@"Onegini: User already authenticated."];
    return;
  }

  self.authenticationCallbackId = command.callbackId;

  ONGUserProfile *user = [OGCDVUserClientHelper getRegisteredUserProfile:profileId];
  if (user == nil) {
    [self sendErrorResultForCallbackId:command.callbackId withMessage:@"Onegini: No registered user found."];
  } else {
    [[ONGUserClient sharedInstance] authenticateUser:user delegate:self];
  }
}

- (void)providePin:(CDVInvokedUrlCommand *)command
{
  if (!self.pinChallenge) {
    [self sendErrorResultForCallbackId:command.callbackId withMessage:@"Onegini: please invoke 'onegini.user.authenticate.start' first."];
    return;
  }

  self.checkPinCallbackId = command.callbackId;
  NSDictionary *options = [command.arguments objectAtIndex:0];
  NSString *pin = options[OGCDVPluginKeyPin];
  [self.pinChallenge.sender respondWithPin:pin challenge:self.pinChallenge];
}

- (void)reauthenticate:(CDVInvokedUrlCommand *)command
{
  NSDictionary *options = [command.arguments objectAtIndex:0];
  NSString *profileId = options[OGCDVPluginKeyProfileId];

  self.authenticationCallbackId = command.callbackId;

  ONGUserProfile *user = [OGCDVUserClientHelper getRegisteredUserProfile:profileId];
  if (user == nil) {
    [self sendErrorResultForCallbackId:command.callbackId withMessage:@"Onegini: No registered user found."];
  } else {
    [[ONGUserClient sharedInstance] reauthenticateUser:user delegate:self];
  }
}

- (void)logout:(CDVInvokedUrlCommand *)command
{
  [[ONGUserClient sharedInstance] logoutUser:^(ONGUserProfile * _Nonnull userProfile, NSError * _Nullable error) {
    if (error != nil) {
      [self sendErrorResultForCallbackId:command.callbackId withError:error];
    } else {
      [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK] callbackId:command.callbackId];
    }
  }];
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

  // Let's make sure the profile is no longer registered (there may be other (future) cases
  // when this delegate method is invoked..
  ONGUserProfile *user = [OGCDVUserClientHelper getRegisteredUserProfile:userProfile.profileId];

  NSDictionary *result = @{
                           @"description": error.localizedDescription,
                           @"deregistered": user == nil ? @(YES) : @(NO)
                           };

  [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsDictionary:result] callbackId:self.checkPinCallbackId];
}

-(void)userClient:(ONGUserClient *)userClient didReceivePinChallenge:(ONGPinChallenge *)challenge
{
  self.pinChallenge = challenge;

  if (challenge.error != nil) {
    NSMutableDictionary *result = [@{
                                     OGCDVPluginKeyMaxFailureCount:@(challenge.maxFailureCount),
                                     OGCDVPluginKeyRemainingFailureCount:@(challenge.remainingFailureCount),
                                     @"description": [NSString stringWithFormat:@"Onegini: Incorrect Pin. Check the %@ and %@ properties for details.", OGCDVPluginKeyMaxFailureCount, OGCDVPluginKeyRemainingFailureCount]
                                     } mutableCopy];

    [result setValue:@(NO) forKey:@"deregistered"];
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

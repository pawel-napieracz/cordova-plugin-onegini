//  Copyright © 2016 Onegini. All rights reserved.

#import "OGCDVUserAuthenticationClient.h"
#import "AppDelegate.h"

static NSString *const OGCDVPluginKeyProfileId = @"profileId";
static NSString *const OGCDVPluginKeyPin = @"pin";
static NSString *const OGCDVPluginKeyMaxFailureCount = @"maxFailureCount";
static NSString *const OGCDVPluginKeyRemainingFailureCount = @"remainingFailureCount";

@implementation OGCDVUserAuthenticationClient {}

- (void)startAuthentication:(CDVInvokedUrlCommand *)command
{
  self.startAuthenticationCallbackId = command.callbackId;
  NSDictionary *options = [command.arguments objectAtIndex:0];
  NSString *profileId = options[OGCDVPluginKeyProfileId];

  ONGUserProfile *profile = [self getRegisteredUserProfile:profileId];
  if (profile == nil) {
    [self sendErrorResultForCallbackId:command.callbackId withMessage:[NSString stringWithFormat: @"No registered user found for the provided %@.", OGCDVPluginKeyProfileId]];
  } else {
    [[ONGUserClient sharedInstance] authenticateUser:profile delegate:self];
  }
}

- (void)checkPin:(CDVInvokedUrlCommand *)command
{
  if (!self.pinChallenge) {
    [self sendErrorResultForCallbackId:command.callbackId withMessage:@"Please call 'startAuthentication' first."];
    return;
  }

  self.checkPinCallbackId = command.callbackId;
  NSDictionary *options = [command.arguments objectAtIndex:0];
  NSString *pin = options[OGCDVPluginKeyPin];
  [self.pinChallenge.sender respondWithPin:pin challenge:self.pinChallenge];
}

#pragma mark - Helper functions
- (ONGUserProfile*) getRegisteredUserProfile:(NSString*)profileId
{
  NSArray<ONGUserProfile *> *profiles = [[ONGUserClient sharedInstance] userProfiles].allObjects;
  for (ONGUserProfile *profile in profiles) {
    if ([profile.profileId isEqualToString:profileId]) {
      return profile;
    }
  }
  return nil;
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

  if (self.startAuthenticationCallbackId) {
    [self sendErrorResultForCallbackId:self.startAuthenticationCallbackId withMessage:@"Don't call 'startAuthentication' twice, call 'checkPin' instead."];
    return;
  }

  // Let's make sure the profile is no longer registered (there may be other (future) cases
  // when this delegate method is invoked..
  ONGUserProfile *profile = [self getRegisteredUserProfile:userProfile.profileId];

  NSDictionary *result = @{
                           @"description": error.localizedDescription,
                           @"deregistered": profile == nil ? @(YES) : @(NO)
                           };

  [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsDictionary:result] callbackId:self.checkPinCallbackId];
}

-(void)userClient:(ONGUserClient *)userClient didReceivePinChallenge:(ONGPinChallenge *)challenge
{
  self.pinChallenge = challenge;

  NSMutableDictionary *result = [@{
                                   OGCDVPluginKeyMaxFailureCount:@(challenge.maxFailureCount),
                                   OGCDVPluginKeyRemainingFailureCount:@(challenge.remainingFailureCount)
                                   } mutableCopy];

  if (challenge.error != nil) {
    [result setValue:[NSString stringWithFormat:@"Incorrect Pin. Check the %@ and %@ properties for details.", OGCDVPluginKeyMaxFailureCount, OGCDVPluginKeyRemainingFailureCount] forKey:@"description"];
    [result setValue:@(NO) forKey:@"deregistered"];
    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsDictionary:result];
    [pluginResult setKeepCallbackAsBool:YES];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:self.checkPinCallbackId];
    return;
  }

  CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:result];
  [self.commandDelegate sendPluginResult:pluginResult callbackId:self.startAuthenticationCallbackId];
  self.startAuthenticationCallbackId = nil;
}

@end

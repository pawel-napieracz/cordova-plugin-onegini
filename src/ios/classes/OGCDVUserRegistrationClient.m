//  Copyright © 2016 Onegini. All rights reserved.

#import "OGCDVUserRegistrationClient.h"
#import "AppDelegate.h"
#import "OGCDVWebBrowserViewController.h"

static NSString *const OGCDVPluginKeyProfileId = @"profileId";
static NSString *const OGCDVPluginKeyScopes = @"scopes";
static NSString *const OGCDVPluginKeyPin = @"pin";
static NSString *const OGCDVPluginKeyPinLength = @"pinLength";

@implementation OGCDVUserRegistrationClient {}

- (void)startRegistration:(CDVInvokedUrlCommand*)command
{
  self.callbackId = command.callbackId;
  NSDictionary *options = [command.arguments objectAtIndex:0];
  NSArray *scopes = options[OGCDVPluginKeyScopes];
  [[ONGUserClient sharedInstance] registerUser:scopes delegate:self];
}

- (void)createPin:(CDVInvokedUrlCommand*)command
{
  self.callbackId = command.callbackId;
  NSDictionary *options = [command.arguments objectAtIndex:0];
  NSString *pin = options[OGCDVPluginKeyPin];
  [self.createPinChallenge.sender respondWithCreatedPin:pin challenge:self.createPinChallenge];
}

- (void)getUserProfiles:(CDVInvokedUrlCommand*)command
{
  NSArray<ONGUserProfile *> *profiles = [[ONGUserClient sharedInstance] userProfiles].allObjects;

  NSMutableArray *result = [[NSMutableArray alloc] initWithCapacity:profiles.count];
  for (ONGUserProfile *profile in profiles) {
    [result addObject:@{OGCDVPluginKeyProfileId: profile.profileId}];
  }

  [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsArray:result] callbackId:command.callbackId];
}

#pragma mark - ONGRegistrationDelegate

- (void)userClient:(ONGUserClient *)userClient didReceivePinRegistrationChallenge:(ONGCreatePinChallenge *)challenge
{
  if (challenge.error != nil) {
    [self sendErrorResultForCallbackId:self.callbackId withError:challenge.error];
    return;
  }

  self.createPinChallenge = challenge;
  [self.viewController dismissViewControllerAnimated:YES completion:nil];

  NSDictionary *result = @{OGCDVPluginKeyPinLength:@(challenge.pinLength)};
  [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:result] callbackId:self.callbackId];
}

- (void)userClient:(ONGUserClient *)userClient didReceiveAuthenticationCodeRequestWithUrl:(NSURL *)url
{
  OGCDVWebBrowserViewController *webBrowserViewController = [OGCDVWebBrowserViewController new];
  webBrowserViewController.url = url;
  webBrowserViewController.completionBlock = ^(NSURL *completionURL) {};
  [self.viewController presentViewController:webBrowserViewController animated:YES completion:nil];
}

- (void)userClient:(ONGUserClient *)userClient didRegisterUser:(ONGUserProfile *)userProfile
{
  NSDictionary *result = @{OGCDVPluginKeyProfileId: userProfile.profileId};
  [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:result] callbackId:self.callbackId];
}

- (void)userClient:(ONGUserClient *)userClient didFailToRegisterWithError:(NSError *)error
{
  [self.viewController dismissViewControllerAnimated:YES completion:nil];
  [self sendErrorResultForCallbackId:self.callbackId withError:error];
}

#pragma mark - OGPinValidationDelegate

- (void)pinEntryError:(NSError *)error
{
  [self sendErrorResultForCallbackId:self.callbackId withError:error];
}

@end

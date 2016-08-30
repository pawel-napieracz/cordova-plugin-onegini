//  Copyright © 2016 Onegini. All rights reserved.

#import "OneginiUserRegistrationClient.h"
#import "AppDelegate.h"
#import "WebBrowserViewController.h"

static NSString *const ONGPluginKeyProfileId = @"profileId";
static NSString *const ONGPluginKeyScopes = @"scopes";
static NSString *const ONGPluginKeyPin = @"pin";
static NSString *const ONGPluginKeyPinLength = @"pinLength";

@implementation OneginiUserRegistrationClient {}

- (void)startRegistration:(CDVInvokedUrlCommand*)command
{
  self.callbackId = command.callbackId;
  NSDictionary *options = [command.arguments objectAtIndex:0];
  NSArray *scopes = options[ONGPluginKeyScopes];
  [[ONGUserClient sharedInstance] registerUser:scopes delegate:self];
}

- (void)createPIN:(CDVInvokedUrlCommand*)command
{
  self.callbackId = command.callbackId;
  NSDictionary *options = [command.arguments objectAtIndex:0];
  NSString *pin = options[ONGPluginKeyPin];
  [self.challenge.sender respondWithCreatedPin:pin challenge:self.challenge];
}

- (void)getUserProfiles:(CDVInvokedUrlCommand*)command
{
  NSArray<ONGUserProfile *> *profiles = [[ONGUserClient sharedInstance] userProfiles].allObjects;

  NSMutableArray *result = [[NSMutableArray alloc] initWithCapacity:profiles.count];
  for (ONGUserProfile *profile in profiles) {
    [result addObject:@{ONGPluginKeyProfileId: profile.profileId}];
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

  // remember for later
  self.challenge = challenge;

  // dismiss controller
  [self.viewController dismissViewControllerAnimated:YES completion:nil];

  NSDictionary *result = @{ONGPluginKeyPinLength:@5};
  [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:result] callbackId:self.callbackId];
}

- (void)userClient:(ONGUserClient *)userClient didReceiveAuthenticationCodeRequestWithUrl:(NSURL *)url
{
  WebBrowserViewController *webBrowserViewController = [WebBrowserViewController new];
  webBrowserViewController.url = url;
  webBrowserViewController.completionBlock = ^(NSURL *completionURL) {};
  [self.viewController presentViewController:webBrowserViewController animated:YES completion:nil];
}

- (void)userClient:(ONGUserClient *)userClient didRegisterUser:(ONGUserProfile *)userProfile
{
  NSDictionary *result = @{ONGPluginKeyProfileId: userProfile.profileId};
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

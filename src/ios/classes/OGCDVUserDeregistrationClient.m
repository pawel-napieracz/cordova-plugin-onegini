//  Copyright © 2016 Onegini. All rights reserved.

#import "OGCDVUserDeregistrationClient.h"
#import "OGCDVUserClientHelper.h"
#import "AppDelegate.h"

static NSString *const OGCDVPluginKeyProfileId = @"profileId";

@implementation OGCDVUserDeregistrationClient {}

- (void)deregister:(CDVInvokedUrlCommand*)command
{
  self.callbackId = command.callbackId;
  NSDictionary *options = [command.arguments objectAtIndex:0];
  NSString *profileId = options[OGCDVPluginKeyProfileId];
  ONGUserProfile *user = [OGCDVUserClientHelper getRegisteredUserProfile:profileId];

  if (user == nil) {
    [self sendErrorResultForCallbackId:command.callbackId withMessage:[NSString stringWithFormat: @"Onegini: No registered user found for the provided %@.", OGCDVPluginKeyProfileId]];
  } else {
    [[ONGUserClient sharedInstance] deregisterUser:user delegate:self];
  }
}

#pragma mark - ONGDeregistrationDelegate

- (void)deregistrationSuccessful:(ONGUserProfile *)userProfile
{
  // TODO there's a bug in the iOS SDK where deregistration doesn't log out the user, so until that's fixed we need this line
  [[ONGUserClient sharedInstance] logoutUser:nil];

  [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK] callbackId:self.callbackId];
}

- (void)deregistrationFailureWithError:(NSError *)error
{
  [self sendErrorResultForCallbackId:self.callbackId withError:error];
}

@end

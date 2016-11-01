//  Copyright © 2016 Onegini. All rights reserved.

#import "OGCDVAuthenticationDelegateHandler.h"
#import "OGCDVConstants.h"

@implementation OGCDVAuthenticationDelegateHandler {
}

#pragma mark - ONGAuthenticationDelegate

- (void)userClient:(ONGUserClient *)userClient didAuthenticateUser:(ONGUserProfile *)userProfile
{
    self.pinChallenge = nil;
    self.fingerprintChallenge = nil;
    NSDictionary *result =  @{
        OGCDVPluginKeyAuthenticationMethod: OGCDVPluginMethodSuccess
    };

    [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:result] callbackId:self.authenticationCallbackId];
}

- (void)userClient:(ONGUserClient *)userClient didFailToAuthenticateUser:(ONGUserProfile *)userProfile error:(NSError *)error
{
    [self sendErrorResultForCallbackId:self.authenticationCallbackId withError:error];
    self.pinChallenge = nil;
    self.fingerprintChallenge = nil;
}

- (void)userClient:(ONGUserClient *)userClient didReceivePinChallenge:(ONGPinChallenge *)challenge
{
    self.pinChallenge = challenge;
    NSMutableDictionary *result = [[NSMutableDictionary alloc] init];
    result[OGCDVPluginKeyAuthenticationMethod] = OGCDVPluginMethodPinRequest;

    if (challenge.error != nil) {
        result[OGCDVPluginKeyMaxFailureCount] = @(challenge.maxFailureCount);
        result[OGCDVPluginKeyRemainingFailureCount] = @(challenge.remainingFailureCount);
        result[@"description"] = [NSString stringWithFormat:@"Onegini: Incorrect Pin. Check the %@ and %@ properties for details.", OGCDVPluginKeyMaxFailureCount, OGCDVPluginKeyRemainingFailureCount];
    }

    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:result];
    [pluginResult setKeepCallbackAsBool:YES];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:self.authenticationCallbackId];
}

- (void)userClient:(ONGUserClient *)userClient didReceiveFingerprintChallenge:(ONGFingerprintChallenge *)challenge
{
    self.fingerprintChallenge = challenge;

    NSDictionary *result = @{
        OGCDVPluginKeyAuthenticationMethod: OGCDVPluginMethodFingerprintRequest
    };
    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:result];
    [pluginResult setKeepCallbackAsBool:YES];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:self.authenticationCallbackId];
}


@end
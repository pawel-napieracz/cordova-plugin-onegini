//  Copyright © 2016 Onegini. All rights reserved.

#import "OGCDVAuthenticationDelegateHandler.h"
#import "OGCDVChangePinClient.h"
#import "OGCDVConstants.h"

@implementation OGCDVChangePinClient {
}

- (void)start:(CDVInvokedUrlCommand *)command
{
    [self.commandDelegate runInBackground:^{
        self.startCallbackId = command.callbackId;
        [[ONGUserClient sharedInstance] changePin:self];
    }];
}

- (void)providePin:(CDVInvokedUrlCommand *)command
{
    if (!self.pinChallenge) {
        [self sendErrorResultForCallbackId:command.callbackId withErrorCode:OGCDVPluginErrCodeProvidePinNoAuthenticationInProgress andMessage:OGCDVPluginErrDescriptionProvidePinNoAuthenticationInProgress];
        return;
    }
    [self.commandDelegate runInBackground:^{
        NSDictionary *options = command.arguments[0];
        NSString *pin = options[OGCDVPluginKeyPin];
        [self.pinChallenge.sender respondWithPin:pin challenge:self.pinChallenge];
    }];
}

- (void)createPin:(CDVInvokedUrlCommand *)command
{
    if (!self.createPinChallenge) {
        [self sendErrorResultForCallbackId:command.callbackId withErrorCode:OGCDVPluginErrCodeCreatePinNoRegistrationInProgress andMessage:OGCDVPluginErrDescriptionCreatePinNoRegistrationInProgress];
        return;
    }

    [self.commandDelegate runInBackground:^{
        NSDictionary *options = command.arguments[0];
        NSString *pin = options[OGCDVPluginKeyPin];
        [self.createPinChallenge.sender respondWithCreatedPin:pin challenge:self.createPinChallenge];
    }];
}

#pragma mark - ONGChangePinDelegate

- (void)userClient:(ONGUserClient *)userClient didReceivePinChallenge:(ONGPinChallenge *)challenge
{
    self.pinChallenge = challenge;
    NSDictionary *result = @{
        OGCDVPluginKeyAuthenticationEvent: OGCDVPluginAuthEventPinRequest,
        OGCDVPluginKeyMaxFailureCount: @(challenge.maxFailureCount),
        OGCDVPluginKeyRemainingFailureCount: @(challenge.remainingFailureCount)
    };

    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:result];
    [pluginResult setKeepCallbackAsBool:YES];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:self.startCallbackId];
}

- (void)userClient:(ONGUserClient *)userClient didReceiveCreatePinChallenge:(ONGCreatePinChallenge *)challenge
{
    self.createPinChallenge = challenge;
    [self.viewController dismissViewControllerAnimated:YES completion:nil];

    NSMutableDictionary *result = [[NSMutableDictionary alloc] init];
    result[OGCDVPluginKeyAuthenticationEvent] = OGCDVPluginAuthEventCreatePinRequest;
    result[OGCDVPluginKeyPinLength] = @(challenge.pinLength);

    if (challenge.error != nil) {
        result[OGCDVPluginKeyErrorCode] = @(challenge.error.code);
        result[OGCDVPluginKeyErrorDescription] = challenge.error.localizedDescription;
    }

    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:result];
    [pluginResult setKeepCallbackAsBool:YES];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:self.startCallbackId];
}

- (void)userClient:(ONGUserClient *)userClient didChangePinForUser:(ONGUserProfile *)userProfile
{
    NSDictionary *result = @{
        OGCDVPluginKeyAuthenticationEvent: OGCDVPluginAuthEventSuccess
    };

    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:result];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:self.startCallbackId];

    self.createPinChallenge = nil;
    self.startCallbackId = nil;
}

- (void)userClient:(ONGUserClient *)userClient didFailToChangePinForUser:(ONGUserProfile *)userProfile error:(NSError *)error
{
    [self sendErrorResultForCallbackId:self.startCallbackId withError:error];
}

@end

/*
 * Copyright (c) 2016 Onegini B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#import "OGCDVMobileAuthenticationRequestClient.h"
#import "OGCDVMobileAuthenticationOperation.h"
#import "OGCDVConstants.h"
#import "OGCDVAuthenticationDelegateHandler.h"

NSString *const OGCDVPluginMobileAuthenticationMethodConfirmation = @"confirmation";
NSString *const OGCDVPluginMobileAuthenticationMethodPin = @"pin";
NSString *const OGCDVPluginMobileAuthenticationMethodFingerprint = @"fingerprint";
NSString *const OGCDVPluginMobileAuthenticationMethodFido = @"fido";
static OGCDVMobileAuthenticationRequestClient *sharedInstance;

@implementation OGCDVMobileAuthenticationRequestClient {
}

@synthesize operationQueue;
@synthesize challengeReceiversCallbackIds;
@synthesize authenticationEventsForMethods;

+ (id)sharedInstance
{
    return sharedInstance;
}

- (void)pluginInitialize
{
    operationQueue = [[NSOperationQueue alloc] init];
    [operationQueue setMaxConcurrentOperationCount:1];
    challengeReceiversCallbackIds = [[NSMutableDictionary alloc] init];
    authenticationEventsForMethods = @{
        OGCDVPluginMobileAuthenticationMethodConfirmation: OGCDVPluginEventConfirmationRequest,
        OGCDVPluginMobileAuthenticationMethodPin: OGCDVPluginEventPinRequest,
        OGCDVPluginMobileAuthenticationMethodFingerprint: OGCDVPluginEventFingerprintRequest,
        OGCDVPluginMobileAuthenticationMethodFido: OGCDVPluginAuthEventFidoRequest
    };

    sharedInstance = self;
}

- (void)setDelegate:(id)newDelegate
{
    delegate = newDelegate;
}

- (void)didReceiveRemoteNotification:(NSDictionary *)userInfo
{
    [[ONGUserClient sharedInstance] handleMobileAuthenticationRequest:userInfo delegate:self];
}

- (void)userClient:(ONGUserClient *)userClient didReceiveConfirmationChallenge:(void (^)(BOOL confirmRequest))confirmation forRequest:(ONGMobileAuthenticationRequest *)request
{
    OGCDVMobileAuthenticationOperation *operation = [[OGCDVMobileAuthenticationOperation alloc]
        initWithConfirmationChallenge:confirmation
                           forRequest:request
                            forMethod:OGCDVPluginMobileAuthenticationMethodConfirmation];
    [operationQueue addOperation:operation];
}

- (void)userClient:(ONGUserClient *)userClient didReceivePinChallenge:(ONGPinChallenge *)challenge forRequest:(ONGMobileAuthenticationRequest *)request
{
    if (challenge.error.code == ONGAuthenticationErrorInvalidPin) {
        [delegate setPinChallenge:challenge];
        [delegate sendChallenge:challengeReceiversCallbackIds[OGCDVPluginMobileAuthenticationMethodPin]];
        return;
    }

    OGCDVMobileAuthenticationOperation *operation = [[OGCDVMobileAuthenticationOperation alloc]
        initWithPinChallenge:challenge
                  forRequest:request
                   forMethod:OGCDVPluginMobileAuthenticationMethodPin];
    [operationQueue addOperation:operation];
}

- (void)userClient:(ONGUserClient *)userClient didReceiveFingerprintChallenge:(ONGFingerprintChallenge *)challenge
        forRequest:(ONGMobileAuthenticationRequest *)request
{
    OGCDVMobileAuthenticationOperation *operation = [[OGCDVMobileAuthenticationOperation alloc]
        initWithFingerprintChallenge:challenge
                          forRequest:request
                           forMethod:OGCDVPluginMobileAuthenticationMethodFingerprint];
    [operationQueue addOperation:operation];
}

- (void)userClient:(ONGUserClient *)userClient didReceiveFIDOChallenge:(ONGFIDOChallenge *)challenge
        forRequest:(ONGMobileAuthenticationRequest *)request
{
    OGCDVMobileAuthenticationOperation *operation = [[OGCDVMobileAuthenticationOperation alloc]
        initWithFidoChallenge:challenge
                   forRequest:request
                    forMethod:OGCDVPluginMobileAuthenticationMethodFido];
    [operationQueue addOperation:operation];
}

- (void)userClient:(ONGUserClient *)userClient didFailToHandleMobileAuthenticationRequest:(ONGMobileAuthenticationRequest *)request error:(NSError *)error
{
    [self sendErrorResultForCallbackId:[delegate completeOperationCallbackId] withError:error];
    [delegate completeOperation];
}

- (void)userClient:(ONGUserClient *)userClient didHandleMobileAuthenticationRequest:(ONGMobileAuthenticationRequest *)request
{
    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:[delegate completeOperationCallbackId]];
    [delegate completeOperation];
}

- (void)registerChallengeReceiver:(CDVInvokedUrlCommand *)command
{
    NSDictionary *options = command.arguments[0];
    NSString *method = options[OGCDVPluginKeyMethod];
    challengeReceiversCallbackIds[method] = command.callbackId;
}

- (void)replyToChallenge:(CDVInvokedUrlCommand *)command
{
    if (delegate == nil || [delegate isFinished]) {
        return;
    }

    [self.commandDelegate runInBackground:^{
        NSDictionary *options = command.arguments[0];
        NSString *method = [delegate mobileAuthenticationMethod];
        BOOL result = [options[OGCDVPluginKeyAccept] boolValue];

        if ([OGCDVPluginMobileAuthenticationMethodConfirmation isEqualToString:method]) {
            [delegate mobileAuthenticationRequestClient:self didReceiveConfirmationChallengeResponse:result
                                         withCallbackId:command.callbackId];
        } else if ([OGCDVPluginMobileAuthenticationMethodPin isEqualToString:method]) {
            NSString *pin = options[OGCDVPluginKeyPin];
            [delegate mobileAuthenticationRequestClient:self didReceivePinChallengeResponse:result withPin:pin
                                         withCallbackId:command.callbackId];
        } else if ([OGCDVPluginMobileAuthenticationMethodFingerprint isEqualToString:method]) {
            NSString *prompt = options[OGCDVPluginKeyPrompt];
            [delegate mobileAuthenticationRequestClient:self didReceiveFingerprintChallengeResponse:result
                                         withPrompt:prompt withCallbackId:command.callbackId];
        } else if ([OGCDVPluginMobileAuthenticationMethodFido isEqualToString:method]) {
            [delegate mobileAuthenticationRequestClient:self didReceiveFidoChallengeResponse:result
                                         withCallbackId:command.callbackId];
        } else {
            [self sendErrorResultForCallbackId:command.callbackId
                                 withErrorCode:OGCDVPluginErrCodeInvalidMobileAuthenticationMethod
                                    andMessage:OGCDVPluginErrDescriptionInvalidMobileAuthenticationMethod];
        }
    }];
}

@end

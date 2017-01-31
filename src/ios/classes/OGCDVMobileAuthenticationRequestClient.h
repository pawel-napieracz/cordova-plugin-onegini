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

#import "CDVPlugin+OGCDV.h"
#import "OneginiSDK.h"

@interface OGCDVMobileAuthenticationRequestClient : CDVPlugin<ONGMobileAuthenticationRequestDelegate> {
    NSOperationQueue *operationQueue;
    NSMutableDictionary *challengeReceiversCallbackIds;
    NSDictionary *authenticationEventsForMethods;
    id delegate;
}

@property (nonatomic, retain) NSOperationQueue *operationQueue;
@property (nonatomic, retain) NSMutableDictionary *challengeReceiversCallbackIds;
@property (nonatomic, retain) NSDictionary *authenticationEventsForMethods;

+ (id)sharedInstance;
- (void)setDelegate:(id)newDelegate;
- (void)didReceiveRemoteNotification:(NSDictionary *)userInfo;
- (void)registerChallengeReceiver:(CDVInvokedUrlCommand *)command;
- (void)replyToChallenge:(CDVInvokedUrlCommand *)command;

@end

@protocol OGCDVPluginMobileAuthenticationRequestDelegate
- (void)mobileAuthenticationRequestClient:(OGCDVMobileAuthenticationRequestClient *)mobileAuthenticationRequestClient
  didReceiveConfirmationChallengeResponse:(BOOL)accept withCallbackId:(NSString *)callbackId;
- (void)mobileAuthenticationRequestClient:(OGCDVMobileAuthenticationRequestClient *)mobileAuthenticationRequestClient
           didReceivePinChallengeResponse:(BOOL)accept withPin:(NSString *)pin withCallbackId:(NSString *)callbackId;
- (void)mobileAuthenticationRequestClient:(OGCDVMobileAuthenticationRequestClient *)mobileAuthenticationRequestClient
   didReceiveFingerprintChallengeResponse:(BOOL)accept withPrompt:(NSString *)prompt withCallbackId:(NSString *)callbackId;
- (void)mobileAuthenticationRequestClient:(OGCDVMobileAuthenticationRequestClient *)mobileAuthenticationRequestClient
   didReceiveFidoChallengeResponse:(BOOL)accept withCallbackId:(NSString *)callbackId;
- (void)completeOperation;
@end
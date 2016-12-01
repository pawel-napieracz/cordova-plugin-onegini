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

#import "OGCDVUserRegistrationClient.h"
#import "OGCDVWebBrowserViewController.h"
#import "OGCDVUserClientHelper.h"
#import "OGCDVConstants.h"

static OGCDVUserRegistrationClient *sharedInstance;

@implementation OGCDVUserRegistrationClient {
}

+ (id)sharedInstance
{
    return sharedInstance;
}

- (void)pluginInitialize
{
    sharedInstance = self;
}

- (void)start:(CDVInvokedUrlCommand *)command
{
    [self.commandDelegate runInBackground:^{
        self.callbackId = command.callbackId;
        NSArray *optionalScopes = nil;
        if (command.arguments.count > 0) {
            NSDictionary *options = command.arguments[0];
            optionalScopes = options[OGCDVPluginKeyScopes];
        }
        [[ONGUserClient sharedInstance] registerUser:optionalScopes delegate:self];
    }];
}

- (void)createPin:(CDVInvokedUrlCommand *)command
{
    [self.commandDelegate runInBackground:^{
        self.callbackId = command.callbackId;
        NSDictionary *options = command.arguments[0];
        NSString *pin = options[OGCDVPluginKeyPin];

        if (self.createPinChallenge) {
            [self.createPinChallenge.sender respondWithCreatedPin:pin challenge:self.createPinChallenge];
        } else {
            [self sendErrorResultForCallbackId:command.callbackId withErrorCode:OGCDVPluginErrCodeCreatePinNoRegistrationInProgress
                                    andMessage:OGCDVPluginErrDescriptionCreatePinNoRegistrationInProgress];
        }
    }];
}

- (void)getUserProfiles:(CDVInvokedUrlCommand *)command
{
    NSArray<ONGUserProfile *> *profiles = [[ONGUserClient sharedInstance] userProfiles].allObjects;

    NSMutableArray *result = [[NSMutableArray alloc] initWithCapacity:profiles.count];
    for (ONGUserProfile *profile in profiles) {
        [result addObject:@{OGCDVPluginKeyProfileId: profile.profileId}];
    }

    [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsArray:result] callbackId:command.callbackId];
}

- (void)isUserRegistered:(CDVInvokedUrlCommand *)command
{
    NSDictionary *options = command.arguments[0];
    NSString *profileId = options[OGCDVPluginKeyProfileId];

    BOOL isRegistered = [OGCDVUserClientHelper getRegisteredUserProfile:profileId] != nil;

    [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsBool:isRegistered] callbackId:command.callbackId];
}

#pragma mark - ONGRegistrationDelegate

- (void)userClient:(ONGUserClient *)userClient didReceivePinRegistrationChallenge:(ONGCreatePinChallenge *)challenge
{
    self.createPinChallenge = challenge;
    [self.viewController dismissViewControllerAnimated:YES completion:nil];

    NSMutableDictionary *result = [[NSMutableDictionary alloc] init];
    result[OGCDVPluginKeyAuthenticationEvent] = OGCDVPluginAuthEventCreatePinRequest;
    result[OGCDVPluginKeyProfileId] = challenge.userProfile.profileId;
    result[OGCDVPluginKeyPinLength] = @(challenge.pinLength);

    if (challenge.error != nil) {
        result[OGCDVPluginKeyErrorCode] = @(challenge.error.code);
        result[OGCDVPluginKeyErrorDescription] = challenge.error.localizedDescription;
    }

    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:result];
    [pluginResult setKeepCallbackAsBool:YES];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:self.callbackId];
}

- (void)userClient:(ONGUserClient *)userClient didReceiveRegistrationRequestWithUrl:(NSURL *)url
{
    // run on the main thread; we initiated registration in a background thread, but now we need to manipulate the UI
    dispatch_async(dispatch_get_main_queue(), ^{
        OGCDVWebBrowserViewController *webBrowserViewController = [OGCDVWebBrowserViewController new];
        if (self.userId != nil) {
            webBrowserViewController.url = [OGCDVUserRegistrationClient addQueryParameterToUrl:url
                                                                                 withQueryName:@"user_id"
                                                                                withQueryValue:self.userId];
        } else {
            webBrowserViewController.url = url;
        }
        webBrowserViewController.completionBlock = ^(NSURL *completionURL) {
        };
        [self.viewController presentViewController:webBrowserViewController animated:YES completion:nil];
    });
}

- (void)userClient:(ONGUserClient *)userClient didRegisterUser:(ONGUserProfile *)userProfile
{
    self.createPinChallenge = nil;
    NSDictionary *result = @{
        OGCDVPluginKeyAuthenticationEvent: OGCDVPluginAuthEventSuccess,
        OGCDVPluginKeyProfileId: userProfile.profileId
    };
    [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:result] callbackId:self.callbackId];
}

- (void)userClient:(ONGUserClient *)userClient didFailToRegisterWithError:(NSError *)error
{
    [self.viewController dismissViewControllerAnimated:YES completion:nil];
    [self sendErrorResultForCallbackId:self.callbackId withError:error];
}

+ (NSURL *)addQueryParameterToUrl:(NSURL *)url withQueryName:(NSString *)name withQueryValue:(NSString *)value
{
    NSURLComponents *components = [[NSURLComponents alloc] initWithURL:url resolvingAgainstBaseURL:false];
    NSURLQueryItem *newQueryItem = [[NSURLQueryItem alloc] initWithName:name value:value];
    NSMutableArray *queryItems = [NSMutableArray arrayWithCapacity:[components.queryItems count] + 1];
    for (NSURLQueryItem *qi in components.queryItems) {
        if (![qi.name isEqual:newQueryItem.name]) {
            [queryItems addObject:qi];
        }
    }
    [queryItems addObject:newQueryItem];
    [components setQueryItems:queryItems];
    return [components URL];
}

@end

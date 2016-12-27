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

#import "OGCDVResourceClient.h"
#import "OGCDVConstants.h"

NSString *const OGCDVPluginKeyAnonymous = @"anonymous";
NSString *const OGCDVPluginKeyBody = @"body";
NSString *const OGCDVPluginKeyUrl = @"url";
NSString *const OGCDVPluginKeyStatus = @"status";
NSString *const OGCDVPluginKeyStatusText = @"statusText";
NSString *const OGCDVPluginKeyHeaders = @"headers";
NSString *const OGCDVPluginKeyHttpResponse = @"httpResponse";

@implementation OGCDVResourceClient {
}

- (void)fetch:(CDVInvokedUrlCommand *)command
{
    [self.commandDelegate runInBackground:^{
        NSDictionary *options = command.arguments[0];

        NSString *url = options[OGCDVPluginKeyUrl];
        NSString *method = options[OGCDVPluginKeyMethod];
        NSDictionary *params = options[OGCDVPluginKeyBody];
        NSMutableDictionary *headers = options[OGCDVPluginKeyHeaders];
        NSDictionary *convertedHeaders = [self convertNumbersToStringsInDictionary:headers];
        BOOL anonymous = [options[OGCDVPluginKeyAnonymous] boolValue];

        ONGRequestBuilder *requestBuilder = [ONGRequestBuilder builder];
        [requestBuilder setHeaders:convertedHeaders];
        [requestBuilder setMethod:method];
        [requestBuilder setPath:url];

        if (params != nil) {
            [requestBuilder setParametersEncoding:ONGParametersEncodingJSON];
            [requestBuilder setParameters:params];
        }

        ONGResourceRequest *request = [requestBuilder build];

        if (anonymous) {
            [[ONGDeviceClient sharedInstance] fetchResource:request completion:^(ONGResourceResponse *response, NSError *error) {
                [self handleResponse:response withError:error forCallbackId:command.callbackId];
            }];
        } else {
            [[ONGUserClient sharedInstance] fetchResource:request completion:^(ONGResourceResponse *response, NSError *error) {
                [self handleResponse:response withError:error forCallbackId:command.callbackId];
            }];
        }
    }];
}

- (void)handleResponse:(ONGResourceResponse *)response withError:(NSError *)error forCallbackId:(NSString *)callbackId
{
    NSDictionary *httpResponse = @{
        OGCDVPluginKeyBody: [self getBodyFromResponse:response],
        OGCDVPluginKeyStatus: @(response.statusCode),
        OGCDVPluginKeyStatusText: [self getStatusText:response.statusCode],
        OGCDVPluginKeyHeaders: response.allHeaderFields == nil ? @{} : response.allHeaderFields
    };

    if (response.statusCode >= 200 && response.statusCode <= 299) {
        [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:httpResponse] callbackId:callbackId];
    } else {
        NSDictionary *result = @{
            OGCDVPluginKeyHttpResponse: httpResponse,
            OGCDVPluginKeyErrorCode: @(error.code),
            OGCDVPluginKeyErrorDescription: error.description
        };

        [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsDictionary:result] callbackId:callbackId];
    }
}

- (NSString *)getStatusText:(NSInteger)code
{
    return [NSHTTPURLResponse localizedStringForStatusCode:code];
}

- (NSString *)getBodyFromResponse:(ONGResourceResponse *)response
{
    return [[NSString alloc] initWithData:response.data encoding:NSUTF8StringEncoding];
}

- (NSDictionary *)convertNumbersToStringsInDictionary:(NSDictionary *)dictionary
{
    if (!dictionary) {
        return nil;
    }

    NSMutableDictionary *convertedDictionary = [NSMutableDictionary new];

    for (NSString *key in dictionary.allKeys) {
        id value = dictionary[key];
        [convertedDictionary setValue:([value isKindOfClass:[NSNumber class]] ? [value stringValue] : value) forKey:key];
    }

    return convertedDictionary;
}

@end

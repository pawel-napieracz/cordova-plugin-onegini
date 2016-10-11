//  Copyright © 2016 Onegini. All rights reserved.

#import "OGCDVResourceClient.h"
#import "OGCDVConstants.h"

NSString *const OGCDVPluginKeyAnonymous = @"anonymous";
NSString *const OGCDVPluginKeyBody = @"body";
NSString *const OGCDVPluginKeyUrl = @"url";
NSString *const OGCDVPluginKeyStatus = @"status";
NSString *const OGCDVPluginKeyStatusText = @"statusText";
NSString *const OGCDVPluginKeyHeaders = @"headers";

@implementation OGCDVResourceClient {
}

- (void)fetch:(CDVInvokedUrlCommand *)command
{
  [self.commandDelegate runInBackground:^{
      NSDictionary *options = command.arguments[0];

      NSString *url = options[OGCDVPluginKeyUrl];
      NSString *method = options[OGCDVPluginKeyMethod];
      NSDictionary *params = options[OGCDVPluginKeyBody];
      NSMutableDictionary *headers = options[OGCDVPluginKeyHeaders]; // isKindOfClass:[NSNull class]] ? nil : options[OGCDVPluginKeyHeaders];
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
  NSDictionary *result = @{
      OGCDVPluginKeyBody: [self getBodyFromResponse:response],
      OGCDVPluginKeyStatus: @(response.statusCode),
      OGCDVPluginKeyStatusText: [self getStatusText:response.statusCode],
      OGCDVPluginKeyHeaders: response.allHeaderFields == nil ? @{} : response.allHeaderFields
  };

  if (error == nil && response.statusCode == 200) {
    [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:result] callbackId:callbackId];
  } else {
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

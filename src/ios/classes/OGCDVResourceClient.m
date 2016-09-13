//  Copyright © 2016 Onegini. All rights reserved.

#import "OGCDVResourceClient.h"
#import "OGCDVConstants.h"

@implementation OGCDVResourceClient {
}

- (void)fetch:(CDVInvokedUrlCommand *)command
{
  [self.commandDelegate runInBackground:^{
      NSDictionary *options = command.arguments[0];

      // TODO add constants
      NSString *url = options[@"url"];
      NSString *method = options[@"method"];
      NSDictionary *headers = options[@"headers"];
      NSDictionary *body = options[@"body"];

      // TODO implement, this is completely untested
      ONGRequestBuilder* requestBuilder = [ONGRequestBuilder builder];
      [requestBuilder setHeaders:headers];
      [requestBuilder setMethod:method];
      [requestBuilder setPath:url];
      [requestBuilder setParameters:body];
  }];
}

@end

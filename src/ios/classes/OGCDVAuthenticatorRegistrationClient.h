//  Copyright © 2016 Onegini. All rights reserved.

#import "OGCDVAuthenticationDelegateHandler.h"

@interface OGCDVAuthenticatorRegistrationClient : OGCDVAuthenticationDelegateHandler

- (void)registerNew:(CDVInvokedUrlCommand *)command;
- (void)providePin:(CDVInvokedUrlCommand *)command;
- (void)deregister:(CDVInvokedUrlCommand *)command;

@end

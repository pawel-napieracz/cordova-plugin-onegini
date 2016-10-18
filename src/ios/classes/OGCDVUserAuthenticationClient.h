//  Copyright © 2016 Onegini. All rights reserved.

#import "OGCDVAuthenticationDelegateHandler.h"

@interface OGCDVUserAuthenticationClient : OGCDVAuthenticationDelegateHandler

- (void)getAuthenticatedUserProfile:(CDVInvokedUrlCommand *)command;
- (void)start:(CDVInvokedUrlCommand *)command;
- (void)providePin:(CDVInvokedUrlCommand *)command;
- (void)reauthenticate:(CDVInvokedUrlCommand *)command;
- (void)logout:(CDVInvokedUrlCommand *)command;

@end

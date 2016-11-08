//  Copyright © 2016 Onegini. All rights reserved.

#import "OGCDVAuthenticatorsClientHelper.h"
#import "OGCDVConstants.h"

@implementation OGCDVAuthenticatorsClientHelper

+ (NSDictionary *)dictionaryFromAuthenticator:(ONGAuthenticator *)authenticator
{
    NSString *type;
    switch (authenticator.type) {
        case 1:
            type = OGCDVPluginAuthenticatorTypePin;
            break;
        case 2:
            type = OGCDVPluginAuthenticatorTypeTouchId;
            break;
        default:
            type = nil;
            break;
    }
    NSDictionary *object = @{
        OGCDVPluginKeyAuthenticatorType: type,
        OGCDVPluginKeyAuthenticatorId: authenticator.identifier
    };
    return object;
}

@end
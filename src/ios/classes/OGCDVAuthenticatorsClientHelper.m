//  Copyright © 2016 Onegini. All rights reserved.

#import "OGCDVAuthenticatorsClientHelper.h"
#import "OGCDVConstants.h"

@implementation OGCDVAuthenticatorsClientHelper

+ (NSDictionary *)dictionaryFromAuthenticator:(ONGAuthenticator *)authenticator
{
    NSString *type = [self typeStringFromAuthenticatorType:authenticator.type];

    NSString *identifier;
    if (authenticator.type == ONGAuthenticatorPIN || authenticator.type == ONGAuthenticatorTouchID) {
        NSArray *identifierItems = [authenticator.identifier componentsSeparatedByString:@"."];
        identifier = [identifierItems lastObject];
    } else {
        identifier = authenticator.identifier;
    }

    NSDictionary *dictionary = @{
        OGCDVPluginKeyAuthenticatorType: type,
        OGCDVPluginKeyAuthenticatorId: identifier
    };
    return dictionary;
}

+ (ONGAuthenticator *)authenticatorFromArguments:(NSSet<ONGAuthenticator *> *)registeredAuthenticators options:
    (NSDictionary *)options
{
    NSString *authenticatorType = options[OGCDVPluginKeyAuthenticatorType];

    // TODO: Implement search on authenticatorId once custom authenticators have been implemented

    for (ONGAuthenticator *authenticator in registeredAuthenticators) {
        if ([[self typeStringFromAuthenticatorType:authenticator.type] isEqualToString:authenticatorType]) {
            return authenticator;
        }
    }
    return nil;
}

+ (NSString *)typeStringFromAuthenticatorType:(ONGAuthenticatorType)authenticatorType
{
    switch (authenticatorType) {
        case ONGAuthenticatorPIN:
            return OGCDVPluginAuthenticatorTypePin;
        case ONGAuthenticatorTouchID:
            return OGCDVPluginAuthenticatorTypeTouchId;
        default:
            return nil;
    }
}

@end
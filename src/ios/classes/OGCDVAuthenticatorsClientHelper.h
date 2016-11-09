//  Copyright © 2016 Onegini. All rights reserved.

#import "OneginiSDK.h"

@interface OGCDVAuthenticatorsClientHelper : NSObject

+ (NSDictionary *)dictionaryFromAuthenticator:(ONGAuthenticator *)authenticator;
+ (ONGAuthenticator *)authenticatorFromArguments:(NSSet<ONGAuthenticator *> *)registeredAuthenticators options:
    (NSDictionary *)options;

@end
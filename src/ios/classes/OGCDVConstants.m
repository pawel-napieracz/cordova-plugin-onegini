//  Copyright © 2016 Onegini. All rights reserved.

#import "OGCDVConstants.h"

NSString *const OGCDVPluginKeyMaxFailureCount = @"maxFailureCount";
NSString *const OGCDVPluginKeyPin = @"pin";
NSString *const OGCDVPluginKeyPinLength = @"pinLength";
NSString *const OGCDVPluginKeyProfileId = @"profileId";
NSString *const OGCDVPluginKeyRemainingFailureCount = @"remainingFailureCount";
NSString *const OGCDVPluginKeyScopes = @"scopes";
NSString *const OGCDVPluginKeyResourceBaseURL = @"resourceBaseURL";
NSString *const OGCDVPluginKeyAuthenticatorType = @"authenticatorType";
NSString *const OGCDVPluginKeyAuthenticatorId = @"authenticatorId";
NSString *const OGCDVPluginKeyMethod = @"method";
NSString *const OGCDVPluginKeyType = @"type";
NSString *const OGCDVPluginKeyMessage = @"message";
NSString *const OGCDVPluginKeyAuthenticationEvent = @"authenticationEvent";
NSString *const OGCDVPluginKeyAccept = @"accept";
NSString *const OGCDVPluginKeyPrompt = @"iosPrompt";
NSString *const OGCDVPluginKeyErrorCode = @"code";
NSString *const OGCDVPluginKeyErrorDescription = @"description";

NSString *const OGCDVPluginErrorKeyNoUserAuthenticated = @"Onegini: No user authenticated.";

NSString *const OGCDVPluginClassMobileAuthenticationClient = @"OneginiMobileAuthenticationClient";
NSString *const OGCDVPluginClassMobileAuthenticationRequestClient = @"OneginiMobileAuthenticationRequestClient";

NSString *const OGCDVPluginAuthenticatorTypePin = @"PIN";
NSString *const OGCDVPluginAuthenticatorTypeTouchId = @"Fingerprint";

NSString *const OGCDVPluginAuthEventSuccess = @"onSuccess";
NSString *const OGCDVPluginAuthEventConfirmationRequest = @"onConfirmationRequest";
NSString *const OGCDVPluginAuthEventPinRequest = @"onPinRequest";
NSString *const OGCDVPluginAuthEventCreatePinRequest = @"onCreatePinRequest";
NSString *const OGCDVPluginAuthEventFingerprintRequest = @"onFingerprintRequest";
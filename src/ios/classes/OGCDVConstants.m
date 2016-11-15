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

NSString *const OGCDVPluginClassMobileAuthenticationClient = @"OneginiMobileAuthenticationClient";
NSString *const OGCDVPluginClassMobileAuthenticationRequestClient = @"OneginiMobileAuthenticationRequestClient";

NSString *const OGCDVPluginAuthenticatorTypePin = @"PIN";
NSString *const OGCDVPluginAuthenticatorTypeTouchId = @"Fingerprint";

NSString *const OGCDVPluginAuthEventSuccess = @"onSuccess";
NSString *const OGCDVPluginAuthEventPinRequest = @"onPinRequest";
NSString *const OGCDVPluginAuthEventCreatePinRequest = @"onCreatePinRequest";
NSString *const OGCDVPluginAuthEventFingerprintRequest = @"onFingerprintRequest";

NSString *const OGCDVPluginErrDescriptionInternalError = @"Onegini: Internal plugin error";
NSString *const OGCDVPluginErrDescriptionIllegalArgumentProfile = @"Onegini: Argument Provided is not a valid profile object";
NSString *const OGCDVPluginErrDescriptionProfileNotRegistered = @"Onegini: No registered user found.";
NSString *const OGCDVPluginErrDescriptionUserAlreadyAuthenticated = @"Onegini: User already authenticated.";
NSString *const OGCDVPluginErrDescriptionNoUserAuthenticated = @"Onegini: No user authenticated.";
NSString *const OGCDVPluginErrDescriptionCreatePinNoRegistrationInProgress = @"Onegini: createPin called, but no registration in progress.";
NSString *const OGCDVPluginErrDescriptionProvidePinNoAuthenticationInProgress = @"Onegini: providePin called, but no pin authentication in progress.";
NSString *const OGCDVPluginErrDescriptionFingerprintNoAuthenticationInProgress = @"Onegini: received reply for fingerprint authentication, but no fingerprint authentication in progress.";
NSString *const OGCDVPluginErrDescriptionIncorrectPin = @"Onegini: Incorrect Pin. Check the maxFailureCount and remainingFailureCount properties for details.";
NSString *const OGCDVPluginErrDescriptionNoSuchAuthenticator = @"Onegini: No such authenticator found";
NSString *const OGCDVPluginErrDescriptionNoConfirmationChallenge = @"Onegini: Cannot reply to confirmation challenge, no challenge open";
NSString *const OGCDVPluginErrDescriptionNoPinChallenge = @"Onegini: Cannot reply to pin challenge, no challenge open";
NSString *const OGCDVPluginErrDescriptionNoFingerprintChallenge = @"Onegini: Cannot reply to fingerprint challenge, no challenge open";
NSString *const OGCDVPluginErrDescriptionInvalidMobileAuthenticationMethod = @"Onegini: Invalid mobile authentication method";

int const OGCDVPluginErrCodePluginInternalError = 8000;
int const OGCDVPluginErrCodeConfiguration = 8001;
int const OGCDVPluginErrCodeIllegalArgument = 8002;

int const OGCDVPluginErrCodeProfileNotRegistered = 8003;
int const OGCDVPluginErrCodeUserAlreadyAuthenticated = 8004;
int const OGCDVPluginErrCodeNoUserAuthenticated = 8005;
int const OGCDVPluginErrCodeNoSuchAuthenticator = 8006;
int const OGCDVPluginErrCodeCreatePinNoRegistrationInProgress = 8007;
int const OGCDVPluginErrCodeProvidePinNoAuthenticationInProgress = 8008;
int const OGCDVPluginErrCodeFingerprintNoAuthenticationInProgress = 8009;
int const OGCDVPluginErrCodeNoConfirmationChallenge = 8010;
int const OGCDVPluginErrCodeNoPinChallenge = 8011;
int const OGCDVPluginErrCodeNoFingerprintChallenge = 8012;
int const OGCDVPluginErrCodeInvalidMobileAuthenticationMethod = 8013;
int const OGCDVPluginErrCodeIoException = 8014;
int const OGCDVPluginErrCodeIncorrectPin = 8015;
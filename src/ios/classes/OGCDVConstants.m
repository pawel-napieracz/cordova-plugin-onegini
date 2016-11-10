//  Copyright © 2016 Onegini. All rights reserved.

#import "OGCDVConstants.h"

NSString *const OGCDVPluginKeyMaxFailureCount = @"maxFailureCount";
NSString *const OGCDVPluginKeyPin = @"pin";
NSString *const OGCDVPluginKeyPinLength = @"pinLength";
NSString *const OGCDVPluginKeyProfileId = @"profileId";
NSString *const OGCDVPluginKeyRemainingFailureCount = @"remainingFailureCount";
NSString *const OGCDVPluginKeyScopes = @"scopes";
NSString *const OGCDVPluginKeyResourceBaseURL = @"resourceBaseURL";
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

NSString *const OGCDVPluginAuthEventSuccess = @"onSuccess";
NSString *const OGCDVPluginAuthEventPinRequest = @"onPinRequest";
NSString *const OGCDVPluginAuthEventCreatePinRequest = @"onCreatePinRequest";
NSString *const OGCDVPluginAuthEventFingerprintRequest = @"onFingerprintRequest";

NSString *const OGCDVPluginErrInternalError = @"Onegini: Internal plugin error";
NSString *const OGCDVPluginErrIllegalArgumentProfile = @"Onegini: Argument Provided is not a valid profile object";
NSString *const OGCDVPluginErrProfileNotRegistered = @"Onegini: No registered user found.";
NSString *const OGCDVPluginErrUserAlreadyAuthenticated = @"Onegini: User already authenticated.";
NSString *const OGCDVPluginErrNoUserAuthenticated = @"Onegini: No user authenticated.";
NSString *const OGCDVPluginErrCreatePinNoRegistrationInProgress = @"Onegini: createPin called, but no registration in progress.";
NSString *const OGCDVPluginErrProvidePinNoAuthenticationInProgress = @"Onegini: providePin called, but no pin authentication in progress.";
NSString *const OGCDVPluginErrFingerprintNoAuthenticationInProgress = @"Onegini: received reply for fingerprint authentication, but no fingerprint authentication in progress.";
NSString *const OGCDVPluginErrIncorrectPin = @"Onegini: Incorrect Pin. Check the maxFailureCount and remainingFailureCount properties for details.";
NSString *const OGCDVPluginErrNoSuchAuthenticator = @"Onegini: No such authenticator found";
NSString *const OGCDVPluginErrNoConfirmationChallenge = @"Onegini: Cannot reply to confirmation challenge, no challenge open";
NSString *const OGCDVPluginErrNoPinChallenge = @"Onegini: Cannot reply to pin challenge, no challenge open";
NSString *const OGCDVPluginErrNoFingerprintChallenge = @"Onegini: Cannot reply to fingerprint challenge, no challenge open";
NSString *const OGCDVPluginErrInvalidMobileAuthenticationMethod = @"Onegini: Invalid mobile authentication method";

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
/*
 * Copyright (c) 2016 Onegini B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

extern NSString *const OGCDVPluginKeyMaxFailureCount;
extern NSString *const OGCDVPluginKeyPin;
extern NSString *const OGCDVPluginKeyPinLength;
extern NSString *const OGCDVPluginKeyProfileId;
extern NSString *const OGCDVPluginKeyRemainingFailureCount;
extern NSString *const OGCDVPluginKeyScopes;
extern NSString *const OGCDVPluginKeyResourceBaseURL;
extern NSString *const OGCDVPluginKeyAuthenticatorType;
extern NSString *const OGCDVPluginKeyAuthenticatorId;
extern NSString *const OGCDVPluginKeyAuthenticatorIsPreferred;
extern NSString *const OGCDVPluginKeyAuthenticatorIsRegistered;
extern NSString *const OGCDVPluginKeyAuthenticatorName;
extern NSString *const OGCDVPluginKeyMethod;
extern NSString *const OGCDVPluginKeyType;
extern NSString *const OGCDVPluginKeyMessage;
extern NSString *const OGCDVPluginKeyAuthenticationEvent;
extern NSString *const OGCDVPluginKeyAccept;
extern NSString *const OGCDVPluginKeyPrompt;
extern NSString *const OGCDVPluginKeyErrorCode;
extern NSString *const OGCDVPluginKeyErrorDescription;

extern NSString *const OGCDVPluginClassMobileAuthenticationClient;
extern NSString *const OGCDVPluginClassMobileAuthenticationRequestClient;

extern NSString *const OGCDVPluginAuthenticatorTypePin;
extern NSString *const OGCDVPluginAuthenticatorTypeTouchId;

extern NSString *const OGCDVPluginAuthEventSuccess;
extern NSString *const OGCDVPluginAuthEventConfirmationRequest;
extern NSString *const OGCDVPluginAuthEventPinRequest;
extern NSString *const OGCDVPluginAuthEventCreatePinRequest;
extern NSString *const OGCDVPluginAuthEventFingerprintRequest;

extern NSString *const OGCDVDidReceiveRegistrationCallbackURLNotification;

extern NSString *const OGCDVPluginErrDescriptionInternalError;
extern NSString *const OGCDVPluginErrDescriptionIllegalArgumentProfile;
extern NSString *const OGCDVPluginErrDescriptionProfileNotRegistered;
extern NSString *const OGCDVPluginErrDescriptionUserAlreadyAuthenticated;
extern NSString *const OGCDVPluginErrDescriptionNoUserAuthenticated;
extern NSString *const OGCDVPluginErrDescriptionNoSuchAuthenticator;
extern NSString *const OGCDVPluginErrDescriptionCreatePinNoRegistrationInProgress;
extern NSString *const OGCDVPluginErrDescriptionProvidePinNoAuthenticationInProgress;
extern NSString *const OGCDVPluginErrDescriptionFingerprintNoAuthenticationInProgress;
extern NSString *const OGCDVPluginErrDescriptionInvalidMobileAuthenticationMethod;
extern NSString *const OGCDVPluginErrDescriptionIncorrectPin;

extern int const OGCDVPluginErrCodePluginInternalError;
extern int const OGCDVPluginErrCodeConfiguration;
extern int const OGCDVPluginErrCodeIllegalArgument;

extern int const OGCDVPluginErrCodeProfileNotRegistered;
extern int const OGCDVPluginErrCodeUserAlreadyAuthenticated;
extern int const OGCDVPluginErrCodeNoUserAuthenticated;
extern int const OGCDVPluginErrCodeNoSuchAuthenticator;
extern int const OGCDVPluginErrCodeCreatePinNoRegistrationInProgress;
extern int const OGCDVPluginErrCodeProvidePinNoAuthenticationInProgress;
extern int const OGCDVPluginErrCodeFingerprintNoAuthenticationInProgress;
extern int const OGCDVPluginErrCodeInvalidMobileAuthenticationMethod;
extern int const OGCDVPluginErrCodeIoException;
extern int const OGCDVPluginErrCodeIncorrectPin;
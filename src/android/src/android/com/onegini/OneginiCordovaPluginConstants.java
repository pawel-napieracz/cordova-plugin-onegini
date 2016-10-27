package com.onegini;

public interface OneginiCordovaPluginConstants {

  // Json params
  String PARAM_SCOPES = "scopes";
  String PARAM_PROFILE_ID = "profileId";
  String PARAM_PIN = "pin";
  String PARAM_URL = "url";
  String PARAM_METHOD = "method";
  String PARAM_HEADERS = "headers";
  String PARAM_BODY = "body";
  String PARAM_AUTHENTICATOR_ID = "authenticatorId";
  String PARAM_ACCEPT = "accept";

  // Errors
  String ERROR_ARGUMENT_IS_NOT_A_VALID_PROFILE_OBJECT = "Onegini: Argument Provided is not a valid profile object";
  String ERROR_PROFILE_NOT_REGISTERED = "Onegini: No registered user found.";
  String ERROR_USER_ALREADY_AUTHENTICATED = "Onegini: User already authenticated.";
  String ERROR_NO_USER_AUTHENTICATED = "Onegini: No user authenticated.";
  String ERROR_CREATE_PIN_NO_REGISTRATION_IN_PROGRESS = "Onegini: createPin called, but no registration in progress.";
  String ERROR_PROVIDE_PIN_NO_AUTHENTICATION_IN_PROGRESS = "Onegini: providePin called, but no pin authentication in progress.";
  String ERROR_FINGERPRINT_NO_AUTHENTICATION_IN_PROGRESS = "Onegini: received reply for fingerprint authentication, but no fingerprint authentication in progress.";
  String ERROR_INCORRECT_PIN = "Onegini: Incorrect Pin. Check the maxFailureCount and remainingFailureCount properties for details.";
  String ERROR_PLUGIN_INTERNAL_ERROR = "Onegini: Internal plugin error";
  String ERROR_NO_SUCH_AUTHENTICATOR = "Onegini: No such authenticator found";
  String ERROR_NO_CONFIRMATION_CHALLENGE = "Onegini: Cannot reply to confirmation challenge, no challenge open";
  String ERROR_NO_PIN_CHALLENGE = "Onegini: Cannot reply to pin challenge, no challenge open";
  String ERROR_INVALID_MOBILE_AUTHENTICATOR_METHOD = "Onegini: Invalid mobile authenticator method";

  // Authentication methods
  String AUTH_METHOD_SUCCESS = "onSuccess";
  String AUTH_METHOD_PIN_REQUEST = "onPinRequest";
  String AUTH_METHOD_CREATE_PIN_REQUEST = "onCreatePinRequest";
  String AUTH_METHOD_FINGERPRINT_REQUEST = "onFingerprintRequest";
  String AUTH_METHOD_FINGERPRINT_FAILED = "onFingerprintFailed";
  String AUTH_METHOD_FINGERPRINT_CAPTURED = "onFingerprintCaptured";

  // Values
  int PIN_LENGTH = 5;
}

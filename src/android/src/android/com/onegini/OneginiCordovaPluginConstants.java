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

  // Error descriptions
  String ERROR_PLUGIN_INTERNAL_ERROR = "Onegini: Internal plugin error";
  String ERROR_ILLEGAL_ARGUMENT_PROFILE = "Onegini: Argument Provided is not a valid profile object";
  String ERROR_PROFILE_NOT_REGISTERED = "Onegini: No registered user found.";
  String ERROR_USER_ALREADY_AUTHENTICATED = "Onegini: User already authenticated.";
  String ERROR_NO_USER_AUTHENTICATED = "Onegini: No user authenticated.";
  String ERROR_CREATE_PIN_NO_REGISTRATION_IN_PROGRESS = "Onegini: createPin called, but no registration in progress.";
  String ERROR_PROVIDE_PIN_NO_AUTHENTICATION_IN_PROGRESS = "Onegini: providePin called, but no pin authentication in progress.";
  String ERROR_FINGERPRINT_NO_AUTHENTICATION_IN_PROGRESS = "Onegini: received reply for fingerprint authentication, but no fingerprint authentication in progress.";
  String ERROR_INCORRECT_PIN = "Onegini: Incorrect Pin. Check the maxFailureCount and remainingFailureCount properties for details.";
  String ERROR_NO_SUCH_AUTHENTICATOR = "Onegini: No such authenticator found";
  String ERROR_NO_CONFIRMATION_CHALLENGE = "Onegini: Cannot reply to confirmation challenge, no challenge open";
  String ERROR_NO_PIN_CHALLENGE = "Onegini: Cannot reply to pin challenge, no challenge open";
  String ERROR_NO_FINGERPRINT_CHALLENGE = "Onegini: Cannot reply to fingerprint challenge, no challenge open";
  String ERROR_INVALID_MOBILE_AUTHENTICATION_METHOD = "Onegini: Invalid mobile authentication method";

  // Error codes
  int ERROR_CODE_PLUGIN_INTERNAL_ERROR = 8000;
  int ERROR_CODE_CONFIGURATION = 8001;
  int ERROR_CODE_ILLEGAL_ARGUMENT = 8002;

  int ERROR_CODE_PROFILE_NOT_REGISTERED = 8003;
  int ERROR_CODE_USER_ALREADY_AUTHENTICATED = 8004;
  int ERROR_CODE_NO_USER_AUTHENTICATED = 8005;
  int ERROR_CODE_NO_SUCH_AUTHENTICATOR = 8006;
  int ERROR_CODE_CREATE_PIN_NO_REGISTRATION_IN_PROGRESS = 8007;
  int ERROR_CODE_PROVIDE_PIN_NO_AUTHENTICATION_IN_PROGRESS = 8008;
  int ERROR_CODE_FINGERPRINT_NO_AUTHENTICATION_IN_PROGRESS = 8009;
  int ERROR_CODE_NO_CONFIRMATION_CHALLENGE = 8010;
  int ERROR_CODE_NO_PIN_CHALLENGE = 8011;
  int ERROR_CODE_NO_FINGERPRINT_CHALLENGE = 8012;
  int ERROR_CODE_INVALID_MOBILE_AUTHENTICATION_METHOD = 8013;
  int ERROR_CODE_IO_EXCEPTION = 8014;
  int ERROR_CODE_INCORRECT_PIN = 8015;

  // Authentication methods
  String AUTH_EVENT_SUCCESS = "onSuccess";
  String AUTH_EVENT_PIN_REQUEST = "onPinRequest";
  String AUTH_EVENT_CREATE_PIN_REQUEST = "onCreatePinRequest";
  String AUTH_EVENT_FINGERPRINT_REQUEST = "onFingerprintRequest";
  String AUTH_EVENT_FINGERPRINT_FAILED = "onFingerprintFailed";
  String AUTH_EVENT_FINGERPRINT_CAPTURED = "onFingerprintCaptured";

  // Values
  int PIN_LENGTH = 5;

  // Extras
  String EXTRA_MOBILE_AUTHENTICATION = "com.onegini.CordovaPluginMobileAuthenticationExtra";

  // Push message
  String PUSH_MSG_CONTENT = "content";
  String PUSH_MSG_TRANSACTION_ID = "og_transaction_id";
}

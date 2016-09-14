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

  // Errors
  String ERROR_ARGUMENT_IS_NOT_A_VALID_PROFILE_OBJECT = "Onegini: Argument Provided is not a valid profile object";
  String ERROR_PROFILE_NOT_REGISTERED = "Onegini: No registered user found.";
  String ERROR_USER_ALREADY_AUTHENTICATED = "Onegini: User already authenticated.";
  String ERROR_NO_USER_AUTHENTICATED = "Onegini: No user authenticated.";
  String ERROR_CREATE_PIN_NO_REGISTRATION_IN_PROGRESS = "Onegini: createPin called, but no registration in progress. Did you call 'onegini.user.register.start'?";
  String ERROR_PROVIDE_PIN_NO_AUTHENTICATION_IN_PROGRESS = "Onegini: providePin called, but no authentication in progress. Did you call 'onegini.user.register.start'?";
  String ERROR_INCORRECT_PIN = "Onegini: Incorrect Pin. Check the maxFailureCount and remainingFailureCount properties for details.";
  String ERROR_PLUGIN_INTERNAL_ERROR = "Onegini: Internal plugin error";
  String ERROR_METHOD_REQUIRES_REQUEST_BODY = "Onegini: Method requires a request body";

  // Values
  int PIN_LENGTH = 5;
}

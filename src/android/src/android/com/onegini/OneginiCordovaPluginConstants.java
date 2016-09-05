package com.onegini;

public interface OneginiCordovaPluginConstants {

  // Actions
  String ACTION_START = "start";
  String ACTION_CHECK_PIN = "checkPin";
  String ACTION_CREATE_PIN = "createPin";
  String ACTION_GET_USER_PROFILES = "getUserProfiles";

  // Json params
  String PARAM_SCOPES = "scopes";
  String PARAM_PROFILE_ID = "profileId";

  // Errors
  String ERROR_ARGUMENT_IS_NOT_A_VALID_PROFILE_OBJECT = "Onegini: Argument Provided is not a valid profile object";
  String ERROR_PROFILE_NOT_REGISTERED = "Onegini: No registered user found for the provided profileId.";
  String ERROR_USER_ALREADY_AUTHENTICATED = "Onegini: User already authenticated";
  String ERROR_CREATE_PIN_NO_REGISTRATION_IN_PROGRESS = "Onegini: createPin called, but no registration in process. Did you call 'onegini.user.register.start'?";
}

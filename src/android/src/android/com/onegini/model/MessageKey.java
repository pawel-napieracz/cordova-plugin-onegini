package com.onegini.model;

public enum MessageKey {

  ERROR_PLUGIN_UNINITIALIZED,
  DISCONNECT_CONFIRM_QUESTION,
  DISCONNECT_FORGOT_PIN,
  DISCONNECT_DEVICE_DETACHED,

  // Authorization errors
  AUTHORIZATION_ERROR_PIN_INVALID,
  AUTHORIZATION_ERROR_TO_MANY_PIN_ATTEMPTS,
  AUTHORIZATION_ERROR_PIN_CHANGE_FAILED,
  AUTHORIZATION_ERROR_REGISTRATION_FAILED,
  AUTHORIZATION_ERROR,

  // PIN validation messages
  PIN_BLACK_LISTED,
  PIN_SHOULD_NOT_BE_A_SEQUENCE,
  PIN_SHOULD_NOT_USE_SIMILAR_DIGITS,
  PIN_TOO_SHORT,
  PIN_ENTRY_ERROR,
  PIN_CODES_DIFFERS,
  REMAINING_ATTEMPTS,
  MAX_SIMILAR_DIGITS,

  KEYBOARD_TITLE_ENTER_PIN,
  KEYBOARD_TITLE_ENTER_CURRENT_PIN,
  KEYBOARD_TITLE_CREATE_PIN,
  KEYBOARD_TITLE_VERIFY_PIN,

  // Button labels
  ALERT_POPUP_OK,
  CONFIRM_POPUP_CANCEL,
  CONFIRM_POPUP_OK,

  // General errors
  CONNECTIVITY_PROBLEM,
  UNSUPPORTED_APP_VERSION,

  // Resource errors
  RESOURCE_CALL_ERROR,
  RESOURCE_CALL_AUTHENTICATION_FAILED,
  RESOURCE_CALL_SCOPE_ERROR,
  RESOURCE_CALL_BAD_REQUEST,
  RESOURCE_CALL_UNAUTHORIZED,
  RESOURCE_CALL_INVALID_GRANT;
}

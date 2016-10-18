package com.onegini.handler;

import org.apache.cordova.CallbackContext;

import com.onegini.mobile.sdk.android.handlers.OneginiMobileAuthenticationEnrollmentHandler;
import com.onegini.mobile.sdk.android.handlers.error.OneginiMobileAuthenticationEnrollmentError;
import com.onegini.util.PluginResultBuilder;

public class MobileAuthenticationEnrollmentHandler implements OneginiMobileAuthenticationEnrollmentHandler {

  private CallbackContext callbackContext;

  public MobileAuthenticationEnrollmentHandler(final CallbackContext callbackContext) {
    this.callbackContext = callbackContext;
  }

  @Override
  public void onSuccess() {
    callbackContext.sendPluginResult(new PluginResultBuilder()
        .withSuccess()
        .build());
  }

  @Override
  public void onError(final OneginiMobileAuthenticationEnrollmentError oneginiMobileAuthenticationEnrollmentError) {
    callbackContext.sendPluginResult(new PluginResultBuilder()
        .withOneginiError(oneginiMobileAuthenticationEnrollmentError)
        .build());
  }
}

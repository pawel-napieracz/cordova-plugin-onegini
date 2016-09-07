package com.onegini.handler;

import org.apache.cordova.CallbackContext;

import com.onegini.mobile.sdk.android.handlers.OneginiPinValidationHandler;
import com.onegini.mobile.sdk.android.handlers.error.OneginiPinValidationError;
import com.onegini.util.PluginResultBuilder;

public class PinValidationHandler implements OneginiPinValidationHandler {

  private CallbackContext callbackContext;

  public PinValidationHandler(final CallbackContext callbackContext) {
    this.callbackContext = callbackContext;
  }

  @Override
  public void onSuccess() {
    callbackContext.success();
  }

  @Override
  public void onError(final OneginiPinValidationError oneginiPinValidationError) {
    callbackContext.sendPluginResult(new PluginResultBuilder()
        .withErrorType(oneginiPinValidationError.getErrorType())
        .withErrorDescription(oneginiPinValidationError.getErrorDescription())
        .build());
  }
}

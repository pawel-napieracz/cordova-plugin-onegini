package com.onegini.handler;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;

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
    final PluginResult pluginResult = new PluginResultBuilder()
        .withSuccess()
        .build();

    callbackContext.sendPluginResult(pluginResult);
  }

  @Override
  public void onError(final OneginiPinValidationError oneginiPinValidationError) {
    final PluginResult pluginResult = new PluginResultBuilder()
        .withOneginiError(oneginiPinValidationError)
        .build();

    callbackContext.sendPluginResult(pluginResult);
  }
}

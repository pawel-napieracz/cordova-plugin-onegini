package com.onegini.handler;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;

import com.onegini.mobile.sdk.android.handlers.OneginiDeregisterUserProfileHandler;
import com.onegini.mobile.sdk.android.handlers.error.OneginiDeregistrationError;
import com.onegini.util.PluginResultBuilder;

public class DeregistrationHandler implements OneginiDeregisterUserProfileHandler {
  private CallbackContext callbackContext;

  public DeregistrationHandler(final CallbackContext callbackContext) {
    this.callbackContext = callbackContext;
  }

  @Override
  public void onSuccess() {
    final PluginResult pluginResult = new PluginResultBuilder()
        .withSuccess()
        .build();

    sendPluginResult(pluginResult);
  }

  @Override
  public void onError(final OneginiDeregistrationError oneginiDeregistrationError) {
    PluginResult pluginResult = new PluginResultBuilder()
        .withError()
        .withOneginiError(oneginiDeregistrationError)
        .build();

    sendPluginResult(pluginResult);
  }

  private void sendPluginResult(final PluginResult pluginResult) {
    if (!callbackContext.isFinished()) {
      callbackContext.sendPluginResult(pluginResult);
    }
  }
}
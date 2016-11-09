package com.onegini.handler;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;

import com.onegini.mobile.sdk.android.handlers.OneginiAuthenticatorDeregistrationHandler;
import com.onegini.mobile.sdk.android.handlers.error.OneginiAuthenticatorDeregistrationError;
import com.onegini.util.PluginResultBuilder;

public class AuthenticatorDeregistrationHandler implements OneginiAuthenticatorDeregistrationHandler {
  private CallbackContext callbackContext;

  public AuthenticatorDeregistrationHandler(final CallbackContext callbackContext) {
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
  public void onError(final OneginiAuthenticatorDeregistrationError oneginiAuthenticatorDeregistrationError) {
    final PluginResult pluginResult = new PluginResultBuilder()
        .withOneginiError(oneginiAuthenticatorDeregistrationError)
        .build();

    sendPluginResult(pluginResult);
  }

  public void setCallbackContext(final CallbackContext callbackContext) {
    this.callbackContext = callbackContext;
  }

  private void sendPluginResult(final PluginResult pluginResult) {
    if (!callbackContext.isFinished()) {
      callbackContext.sendPluginResult(pluginResult);
    }
  }
}

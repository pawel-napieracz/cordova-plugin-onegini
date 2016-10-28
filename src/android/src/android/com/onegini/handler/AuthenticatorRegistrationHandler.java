package com.onegini.handler;

import static com.onegini.OneginiCordovaPluginConstants.AUTH_METHOD_SUCCESS;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;

import com.onegini.mobile.sdk.android.handlers.OneginiAuthenticatorRegistrationHandler;
import com.onegini.mobile.sdk.android.handlers.error.OneginiAuthenticatorRegistrationError;
import com.onegini.util.PluginResultBuilder;

public class AuthenticatorRegistrationHandler implements OneginiAuthenticatorRegistrationHandler {
  private CallbackContext callbackContext;

  public AuthenticatorRegistrationHandler(final CallbackContext callbackContext) {
    this.callbackContext = callbackContext;
  }

  @Override
  public void onSuccess() {
    final PluginResult pluginResult = new PluginResultBuilder()
        .withSuccess()
        .withAuthenticationMethod(AUTH_METHOD_SUCCESS)
        .build();

    sendPluginResult(pluginResult);
  }

  @Override
  public void onError(final OneginiAuthenticatorRegistrationError oneginiAuthenticatorRegistrationError) {
    final PluginResult pluginResult = new PluginResultBuilder()
        .withOneginiError(oneginiAuthenticatorRegistrationError)
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

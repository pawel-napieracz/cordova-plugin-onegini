package com.onegini.handler;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;

import com.onegini.mobile.android.sdk.handlers.OneginiAuthenticationHandler;
import com.onegini.mobile.android.sdk.handlers.error.OneginiAuthenticationError;
import com.onegini.mobile.android.sdk.model.entity.UserProfile;
import com.onegini.util.PluginResultBuilder;

public class AuthenticationHandler implements OneginiAuthenticationHandler {
  private final CallbackContext callbackContext;

  public AuthenticationHandler(final CallbackContext callbackContext) {
    this.callbackContext = callbackContext;
  }

  @Override
  public void onSuccess(final UserProfile userProfile) {
    final PluginResult pluginResult = new PluginResultBuilder()
        .withSuccess()
        .addUserProfile(userProfile)
        .build();

    sendPluginResult(pluginResult);
  }

  @Override
  public void onError(final OneginiAuthenticationError oneginiAuthenticationError) {
    final PluginResult pluginResult = new PluginResultBuilder()
        .withError()
        .withErrorType(oneginiAuthenticationError.getErrorType())
        .withErrorDescription(oneginiAuthenticationError.getErrorDescription())
        .build();

    sendPluginResult(pluginResult);
  }

  private void sendPluginResult(final PluginResult pluginResult) {
    if (!callbackContext.isFinished()) {
      callbackContext.sendPluginResult(pluginResult);
    }
  }
}

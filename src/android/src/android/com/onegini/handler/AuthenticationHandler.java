package com.onegini.handler;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;

import com.onegini.mobile.sdk.android.handlers.OneginiAuthenticationHandler;
import com.onegini.mobile.sdk.android.handlers.error.OneginiAuthenticationError;
import com.onegini.mobile.sdk.android.model.entity.UserProfile;
import com.onegini.util.PluginResultBuilder;

public class AuthenticationHandler implements OneginiAuthenticationHandler {
  private CallbackContext callbackContext;

  public AuthenticationHandler(final CallbackContext callbackContext) {
    this.callbackContext = callbackContext;
  }

  @Override
  public void onSuccess(final UserProfile userProfile) {
    final PluginResult pluginResult = new PluginResultBuilder()
        .withSuccess()
        .withProfileId(userProfile)
        .build();

    sendPluginResult(pluginResult);
  }

  @Override
  public void onError(final OneginiAuthenticationError oneginiAuthenticationError) {
    final PluginResultBuilder pluginResultBuilder = new PluginResultBuilder()
        .withError()
        .withErrorType(oneginiAuthenticationError.getErrorType())
        .withErrorDescription(oneginiAuthenticationError.getErrorDescription());

    if (oneginiAuthenticationError.getErrorType() == OneginiAuthenticationError.USER_DEREGISTERED) {
      pluginResultBuilder.withRemainingFailureCount(0);
    }

    sendPluginResult(pluginResultBuilder.build());
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

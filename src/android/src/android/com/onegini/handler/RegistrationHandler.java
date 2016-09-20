package com.onegini.handler;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;

import com.onegini.mobile.sdk.android.handlers.OneginiRegistrationHandler;
import com.onegini.mobile.sdk.android.handlers.error.OneginiRegistrationError;
import com.onegini.mobile.sdk.android.model.entity.UserProfile;
import com.onegini.util.PluginResultBuilder;

public class RegistrationHandler implements OneginiRegistrationHandler {

  private CallbackContext callbackContext;

  public RegistrationHandler(CallbackContext callbackContext) {
    this.callbackContext = callbackContext;
  }

  public void setCallbackContext(final CallbackContext callbackContext) {
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
  public void onError(final OneginiRegistrationError oneginiRegistrationError) {
    PluginResult pluginResult = new PluginResultBuilder()
        .withError()
        .withOneginiError(oneginiRegistrationError)
        .build();

    sendPluginResult(pluginResult);
  }

  private void sendPluginResult(final PluginResult pluginResult) {
    if (!callbackContext.isFinished()) {
      callbackContext.sendPluginResult(pluginResult);
    }
  }

}

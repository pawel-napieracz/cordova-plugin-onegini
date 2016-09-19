package com.onegini.handler;

import java.util.Set;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;

import com.onegini.mobile.sdk.android.handlers.OneginiInitializationHandler;
import com.onegini.mobile.sdk.android.handlers.error.OneginiInitializationError;
import com.onegini.mobile.sdk.android.model.OneginiClientConfigModel;
import com.onegini.mobile.sdk.android.model.entity.UserProfile;
import com.onegini.util.PluginResultBuilder;

public class InitializationHandler implements OneginiInitializationHandler {

  private final CallbackContext callbackContext;
  private final OneginiClientConfigModel configModel;

  public InitializationHandler(final CallbackContext callbackContext, final OneginiClientConfigModel configModel) {
    this.callbackContext = callbackContext;
    this.configModel = configModel;
  }

  @Override
  public void onSuccess(final Set<UserProfile> userProfiles) {
    final PluginResult pluginResult = new PluginResultBuilder()
        .withSuccess()
        .withOneginiConfigModel(configModel)
        .build();

    callbackContext.sendPluginResult(pluginResult);
  }

  @Override
  public void onError(final OneginiInitializationError oneginiInitializationError) {
    final PluginResult pluginResult = new PluginResultBuilder()
        .withError()
        .withErrorType(oneginiInitializationError.getErrorType())
        .withErrorDescription(oneginiInitializationError.getErrorDescription())
        .build();

    callbackContext.sendPluginResult(pluginResult);
  }
}

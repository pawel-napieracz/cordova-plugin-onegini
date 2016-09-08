package com.onegini.handler;

import java.util.Set;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;

import com.onegini.mobile.sdk.android.handlers.OneginiInitializationHandler;
import com.onegini.mobile.sdk.android.handlers.error.OneginiInitializationError;
import com.onegini.mobile.sdk.android.model.entity.UserProfile;
import com.onegini.util.PluginResultBuilder;

public class InitializationHandler implements OneginiInitializationHandler {

  private final CallbackContext callbackContext;

  public InitializationHandler(final CallbackContext callbackContext) {
    this.callbackContext = callbackContext;
  }

  @Override
  public void onSuccess(final Set<UserProfile> userProfiles) {
    callbackContext.sendPluginResult(new PluginResultBuilder()
        .withSuccess()
        .build());
  }

  @Override
  public void onError(final OneginiInitializationError oneginiInitializationError) {
    callbackContext.sendPluginResult(new PluginResultBuilder()
        .withError()
        .withErrorType(oneginiInitializationError.getErrorType())
        .withErrorDescription(oneginiInitializationError.getErrorDescription())
        .build());
  }
}

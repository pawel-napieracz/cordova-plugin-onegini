package com.onegini.handler;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;

import com.onegini.mobile.sdk.android.handlers.error.OneginiPinValidationError;
import com.onegini.mobile.sdk.android.handlers.request.OneginiCreatePinRequestHandler;
import com.onegini.mobile.sdk.android.handlers.request.callback.OneginiPinCallback;
import com.onegini.mobile.sdk.android.model.entity.UserProfile;
import com.onegini.util.PluginResultBuilder;

public class CreatePinRequestHandler implements OneginiCreatePinRequestHandler {

  private static CreatePinRequestHandler instance = null;
  private OneginiPinCallback pinCallback = null;
  private CallbackContext registrationCallback;
  private CallbackContext createPinCallback;
  private UserProfile userProfile;

  protected CreatePinRequestHandler() {
  }

  public static CreatePinRequestHandler getInstance() {
    if (instance == null) {
      instance = new CreatePinRequestHandler();
    }

    return instance;
  }

  public OneginiPinCallback getPinCallback() {
    return pinCallback;
  }

  public void setRegistrationCallback(final CallbackContext registrationCallback) {
    this.registrationCallback = registrationCallback;
  }

  public void setCreatePinCallback(final CallbackContext createPinCallback) {
    this.createPinCallback = createPinCallback;
  }

  @Override
  public void startPinCreation(final UserProfile userProfile, final OneginiPinCallback oneginiPinCallback) {
    this.pinCallback = oneginiPinCallback;
    this.userProfile = userProfile;

    PluginResult pluginResult = new PluginResultBuilder()
        .withSuccess()
        .build();

    sendRegistrationResult(pluginResult);
  }

  @Override
  public void onNextPinCreationAttempt(final OneginiPinValidationError oneginiPinValidationError) {
    PluginResult pluginResult = new PluginResultBuilder()
        .withErrorType(oneginiPinValidationError.getErrorType())
        .withErrorDescription(oneginiPinValidationError.getErrorDescription())
        .shouldKeepCallback()
        .build();

    sendCreatePinResult(pluginResult);
  }

  @Override
  public void finishPinCreation() {
    sendCreatePinResult(new PluginResultBuilder()
        .withSuccess()
        .withProfileId(userProfile)
        .build());

    pinCallback = null;
    userProfile = null;
  }

  private void sendRegistrationResult(final PluginResult pluginResult) {
    if (registrationCallback != null) {
      registrationCallback.sendPluginResult(pluginResult);
    }
  }

  private void sendCreatePinResult(final PluginResult pluginResult) {
    if (createPinCallback != null) {
      createPinCallback.sendPluginResult(pluginResult);
    }
  }
}

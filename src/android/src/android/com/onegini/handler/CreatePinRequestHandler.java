package com.onegini.handler;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;

import com.onegini.mobile.android.sdk.handlers.error.OneginiPinValidationError;
import com.onegini.mobile.android.sdk.handlers.request.OneginiCreatePinRequestHandler;
import com.onegini.mobile.android.sdk.handlers.request.callback.OneginiPinCallback;
import com.onegini.mobile.android.sdk.model.entity.UserProfile;
import com.onegini.util.PluginResultBuilder;

public class CreatePinRequestHandler implements OneginiCreatePinRequestHandler {

  private static CreatePinRequestHandler instance = null;
  private OneginiPinCallback pinCallback = null;
  private CallbackContext cordovaCallback;

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

  public void setCordovaCallback(final CallbackContext cordovaCallback) {
    this.cordovaCallback = cordovaCallback;
  }

  @Override
  public void startPinCreation(final UserProfile userProfile, final OneginiPinCallback oneginiPinCallback) {
    this.pinCallback = oneginiPinCallback;

    PluginResult pluginResult = new PluginResultBuilder()
        .withSuccess()
        .shouldKeepCallback()
        .build();

    sendPluginResult(pluginResult);
  }

  @Override
  public void onNextPinCreationAttempt(final OneginiPinValidationError oneginiPinValidationError) {
    PluginResult pluginResult = new PluginResultBuilder()
        .withErrorType(oneginiPinValidationError.getErrorType())
        .withErrorDescription(oneginiPinValidationError.getErrorDescription())
        .shouldKeepCallback()
        .build();

    sendPluginResult(pluginResult);
  }

  @Override
  public void finishPinCreation() {
    pinCallback = null;
    PluginResult pluginResult = new PluginResultBuilder()
        .withSuccess()
        .build();

    sendPluginResult(pluginResult);
  }

  private void sendPluginResult(final PluginResult pluginResult) {
    if (cordovaCallback != null) {
      cordovaCallback.sendPluginResult(pluginResult);
    }
  }
}

package com.onegini.handler;

import static com.onegini.OneginiCordovaPluginConstants.PIN_LENGTH;

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

  public void setRegistrationCallbackContext(final CallbackContext registrationCallback) {
    this.registrationCallback = registrationCallback;
  }

  public void setCreatePinCallback(final CallbackContext createPinCallback) {
    this.createPinCallback = createPinCallback;
  }

  @Override
  public void startPinCreation(final UserProfile userProfile, final OneginiPinCallback oneginiPinCallback) {
    this.pinCallback = oneginiPinCallback;

    PluginResult pluginResult = new PluginResultBuilder()
        .withSuccess()
        .withPinLength(PIN_LENGTH)
        .build();

    sendStartRegistrationResult(pluginResult);
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
    this.pinCallback = null;
    this.registrationCallback = null;
  }

  private void sendStartRegistrationResult(final PluginResult pluginResult) {
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

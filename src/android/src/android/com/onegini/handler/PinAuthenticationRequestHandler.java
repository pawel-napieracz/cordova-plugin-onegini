package com.onegini.handler;

import static com.onegini.OneginiCordovaPluginConstants.ERROR_INCORRECT_PIN;
import static com.onegini.OneginiCordovaPluginConstants.PIN_LENGTH;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;

import com.onegini.mobile.sdk.android.handlers.request.OneginiPinAuthenticationRequestHandler;
import com.onegini.mobile.sdk.android.handlers.request.callback.OneginiPinCallback;
import com.onegini.mobile.sdk.android.model.entity.UserProfile;
import com.onegini.util.PluginResultBuilder;

public class PinAuthenticationRequestHandler implements OneginiPinAuthenticationRequestHandler {

  private static PinAuthenticationRequestHandler instance = null;
  private OneginiPinCallback pinCallback = null;
  private CallbackContext onNextAuthenticationAttemptCallback;
  private CallbackContext startAuthenticationCallback;
  private CallbackContext finishAuthenticationCallback;

  protected PinAuthenticationRequestHandler() {

  }

  public static PinAuthenticationRequestHandler getInstance() {
    if (instance == null) {
      instance = new PinAuthenticationRequestHandler();
    }

    return instance;
  }

  public OneginiPinCallback getPinCallback() {
    return pinCallback;
  }

  public void setOnNextAuthenticationAttemptCallback(final CallbackContext onNextAuthenticationAttemptCallback) {
    this.onNextAuthenticationAttemptCallback = onNextAuthenticationAttemptCallback;
  }

  public void setStartAuthenticationCallback(final CallbackContext startAuthenticationCallback) {
    this.startAuthenticationCallback = startAuthenticationCallback;
  }

  public void setFinishAuthenticationCallback(final CallbackContext finishAuthenticationCallback) {
    this.finishAuthenticationCallback = finishAuthenticationCallback;
  }

  @Override
  public void startAuthentication(final UserProfile userProfile, final OneginiPinCallback oneginiPinCallback) {
    this.pinCallback = oneginiPinCallback;

    sendStartAuthenticationResult(new PluginResultBuilder()
        .withSuccess()
        .build());
  }

  @Override
  public void onNextAuthenticationAttempt(final int failedAttempts, final int maxAttempts) {
    final int remainingAttempts = maxAttempts - failedAttempts;

    sendOnNextAuthenticationAttemptResult(new PluginResultBuilder()
        .withError()
        .withErrorDescription(ERROR_INCORRECT_PIN)
        .withMaxFailureCount(maxAttempts)
        .withRemainingFailureCount(remainingAttempts)
        .shouldKeepCallback()
        .build());
  }

  @Override
  public void finishAuthentication() {
    pinCallback = null;
    sendFinishAuthenticationResult(new PluginResultBuilder()
        .withSuccess()
        .withPinLength(PIN_LENGTH)
        .build());
  }

  private void sendOnNextAuthenticationAttemptResult(final PluginResult pluginResult) {
    if (onNextAuthenticationAttemptCallback != null) {
      onNextAuthenticationAttemptCallback.sendPluginResult(pluginResult);
    }
  }

  private void sendStartAuthenticationResult(final PluginResult pluginResult) {
    if (startAuthenticationCallback != null) {
      startAuthenticationCallback.sendPluginResult(pluginResult);
    }
  }

  private void sendFinishAuthenticationResult(final PluginResult pluginResult) {
    if (finishAuthenticationCallback != null && !finishAuthenticationCallback.isFinished()) {
      finishAuthenticationCallback.sendPluginResult(pluginResult);
    }
  }
}

package com.onegini.handler;

import static com.onegini.OneginiCordovaPluginConstants.ERROR_INCORRECT_PIN;
import static com.onegini.OneginiCordovaPluginConstants.PIN_LENGTH;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;

import com.onegini.mobile.sdk.android.handlers.request.OneginiPinAuthenticationRequestHandler;
import com.onegini.mobile.sdk.android.handlers.request.callback.OneginiPinCallback;
import com.onegini.mobile.sdk.android.model.entity.AuthenticationAttemptCounter;
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
  public void startAuthentication(final UserProfile userProfile, final OneginiPinCallback oneginiPinCallback, final AuthenticationAttemptCounter authenticationAttemptCounter) {
    this.pinCallback = oneginiPinCallback;

    final PluginResult pluginResult = new PluginResultBuilder()
        .withSuccess()
        .build();

    sendStartAuthenticationResult(pluginResult);
  }

  @Override
  public void onNextAuthenticationAttempt(final AuthenticationAttemptCounter authenticationAttemptCounter) {
    final PluginResult pluginResult = new PluginResultBuilder()
        .withError()
        .withErrorDescription(ERROR_INCORRECT_PIN)
        .withMaxFailureCount(authenticationAttemptCounter.getMaxAttempts())
        .withRemainingFailureCount(authenticationAttemptCounter.getRemainingAttempts())
        .shouldKeepCallback()
        .build();

    sendOnNextAuthenticationAttemptResult(pluginResult);
  }

  @Override
  public void finishAuthentication() {
    pinCallback = null;
    final PluginResult pluginResult = new PluginResultBuilder()
        .withSuccess()
        .withPinLength(PIN_LENGTH)
        .build();

    sendFinishAuthenticationResult(pluginResult);
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

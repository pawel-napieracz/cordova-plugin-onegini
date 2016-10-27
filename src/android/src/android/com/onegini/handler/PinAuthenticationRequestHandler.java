package com.onegini.handler;

import static com.onegini.OneginiCordovaPluginConstants.AUTH_METHOD_PIN_REQUEST;
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

  public void setStartAuthenticationCallback(final CallbackContext startAuthenticationCallback) {
    this.startAuthenticationCallback = startAuthenticationCallback;
  }

  public void setFinishAuthenticationCallback(final CallbackContext finishAuthenticationCallback) {
    this.finishAuthenticationCallback = finishAuthenticationCallback;
  }

  @Override
  public void startAuthentication(final UserProfile userProfile, final OneginiPinCallback pinCallback, final AuthenticationAttemptCounter authenticationAttemptCounter) {
    this.pinCallback = pinCallback;

    final PluginResult pluginResult = new PluginResultBuilder()
        .withSuccess()
        .shouldKeepCallback()
        .withAuthenticationMethod(AUTH_METHOD_PIN_REQUEST)
        .withPinLength(PIN_LENGTH)
        .build();

    sendStartAuthenticationResult(pluginResult);
  }

  @Override
  public void onNextAuthenticationAttempt(final AuthenticationAttemptCounter authenticationAttemptCounter) {
    final PluginResult pluginResult = new PluginResultBuilder()
        .shouldKeepCallback()
        .withErrorDescription(ERROR_INCORRECT_PIN)
        .withSuccess()
        .withMaxFailureCount(authenticationAttemptCounter.getMaxAttempts())
        .withRemainingFailureCount(authenticationAttemptCounter.getRemainingAttempts())
        .withAuthenticationMethod(AUTH_METHOD_PIN_REQUEST)
        .withPinLength(PIN_LENGTH)
        .build();

    sendStartAuthenticationResult(pluginResult);
  }

  @Override
  public void finishAuthentication() {
    pinCallback = null;
  }

  private void sendStartAuthenticationResult(final PluginResult pluginResult) {
    if (startAuthenticationCallback != null) {
      startAuthenticationCallback.sendPluginResult(pluginResult);
    }
  }

}

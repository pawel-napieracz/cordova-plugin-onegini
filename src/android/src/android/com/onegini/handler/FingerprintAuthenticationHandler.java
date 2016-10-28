package com.onegini.handler;

import static com.onegini.OneginiCordovaPluginConstants.AUTH_METHOD_FINGERPRINT_CAPTURED;
import static com.onegini.OneginiCordovaPluginConstants.AUTH_METHOD_FINGERPRINT_FAILED;
import static com.onegini.OneginiCordovaPluginConstants.AUTH_METHOD_FINGERPRINT_REQUEST;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;

import com.onegini.mobile.sdk.android.handlers.request.OneginiFingerprintAuthenticationRequestHandler;
import com.onegini.mobile.sdk.android.handlers.request.callback.OneginiFingerprintCallback;
import com.onegini.mobile.sdk.android.model.entity.UserProfile;
import com.onegini.util.PluginResultBuilder;

public class FingerprintAuthenticationHandler implements OneginiFingerprintAuthenticationRequestHandler {
  private static FingerprintAuthenticationHandler instance;
  private CallbackContext startAuthenticationCallbackContext;
  private OneginiFingerprintCallback fingerprintCallback;

  protected FingerprintAuthenticationHandler() {

  }

  public static FingerprintAuthenticationHandler getInstance() {
    if (instance == null) {
      instance = new FingerprintAuthenticationHandler();
    }

    return instance;
  }

  public void setStartAuthenticationCallbackContext(final CallbackContext startAuthenticationCallbackContext) {
    this.startAuthenticationCallbackContext = startAuthenticationCallbackContext;
  }

  public OneginiFingerprintCallback getFingerprintCallback() {
    return fingerprintCallback;
  }

  @Override
  public void startAuthentication(final UserProfile userProfile, final OneginiFingerprintCallback fingerprintCallback) {
    this.fingerprintCallback = fingerprintCallback;

    final PluginResult pluginResult = new PluginResultBuilder()
        .withSuccess()
        .shouldKeepCallback()
        .withAuthenticationMethod(AUTH_METHOD_FINGERPRINT_REQUEST)
        .build();

    sendStartAuthenticationResult(pluginResult);
  }

  @Override
  public void onNextAuthenticationAttempt() {
    final PluginResult pluginResult = new PluginResultBuilder()
        .withSuccess()
        .shouldKeepCallback()
        .withAuthenticationMethod(AUTH_METHOD_FINGERPRINT_FAILED)
        .build();

    sendStartAuthenticationResult(pluginResult);
  }

  @Override
  public void onFingerprintCaptured() {
    final PluginResult pluginResult = new PluginResultBuilder()
        .withSuccess()
        .shouldKeepCallback()
        .withAuthenticationMethod(AUTH_METHOD_FINGERPRINT_CAPTURED)
        .build();

    sendStartAuthenticationResult(pluginResult);
  }

  @Override
  public void finishAuthentication() {
    fingerprintCallback = null;
  }

  private void sendStartAuthenticationResult(final PluginResult pluginResult) {
    if (startAuthenticationCallbackContext != null && !startAuthenticationCallbackContext.isFinished()) {
      startAuthenticationCallbackContext.sendPluginResult(pluginResult);
    }
  }
}

package com.onegini.mobile.sdk.cordova.handler;

import static com.onegini.mobile.sdk.cordova.OneginiCordovaPluginConstants.EVENT_OTP_CONFIRMATION_REQUEST;
import static com.onegini.mobile.sdk.cordova.OneginiCordovaPluginConstants.EVENT_SUCCESS;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;

import com.onegini.mobile.sdk.android.handlers.OneginiMobileAuthWithOtpHandler;
import com.onegini.mobile.sdk.android.handlers.error.OneginiMobileAuthWithOtpError;
import com.onegini.mobile.sdk.android.handlers.request.OneginiMobileAuthWithOtpRequestHandler;
import com.onegini.mobile.sdk.android.handlers.request.callback.OneginiAcceptDenyCallback;
import com.onegini.mobile.sdk.android.model.entity.OneginiMobileAuthenticationRequest;
import com.onegini.mobile.sdk.cordova.util.PluginResultBuilder;

public class MobileAuthWithOtpHandler implements OneginiMobileAuthWithOtpHandler, OneginiMobileAuthWithOtpRequestHandler {

  private static MobileAuthWithOtpHandler instance = null;
  private OneginiAcceptDenyCallback acceptDenyCallback;
  private CallbackContext callbackContext;

  private MobileAuthWithOtpHandler() {
  }

  public static MobileAuthWithOtpHandler getInstance() {
    if (instance == null) {
      instance = new MobileAuthWithOtpHandler();
    }

    return instance;
  }

  // *START* Mobile auth with OTP Request Handler methods

  @Override
  public void startAuthentication(final OneginiMobileAuthenticationRequest oneginiMobileAuthenticationRequest,
                                  final OneginiAcceptDenyCallback oneginiAcceptDenyCallback) {
    this.acceptDenyCallback = oneginiAcceptDenyCallback;

    PluginResult pluginResult = new PluginResultBuilder()
        .withSuccess()
        .shouldKeepCallback()
        .withEvent(EVENT_OTP_CONFIRMATION_REQUEST)
        .withOneginiMobileAuthenticationRequest(oneginiMobileAuthenticationRequest)
        .build();

    sendPluginResult(pluginResult);
  }

  private void sendPluginResult(final PluginResult pluginResult) {
    if (callbackContext != null && !callbackContext.isFinished()) {
      callbackContext.sendPluginResult(pluginResult);
    }
  }

  @Override
  public void finishAuthentication() {
    acceptDenyCallback = null;
  }

  // *END* Mobile auth with OTP Request Handler methods

  // *START* Mobile auth with OTP Handler methods

  @Override
  public void onSuccess() {
    final PluginResult pluginResult = new PluginResultBuilder()
        .withSuccess()
        .withEvent(EVENT_SUCCESS)
        .build();

    sendPluginResult(pluginResult);
    this.callbackContext = null;
  }

  @Override
  public void onError(final OneginiMobileAuthWithOtpError oneginiMobileAuthWithOtpError) {
    final PluginResult pluginResult = new PluginResultBuilder()
        .withError()
        .withOneginiError(oneginiMobileAuthWithOtpError).build();

    sendPluginResult(pluginResult);
    this.callbackContext = null;
  }
}

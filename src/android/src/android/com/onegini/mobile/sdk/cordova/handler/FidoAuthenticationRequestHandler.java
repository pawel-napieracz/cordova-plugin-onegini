package com.onegini.mobile.sdk.cordova.handler;

import static com.onegini.mobile.sdk.cordova.OneginiCordovaPluginConstants.AUTH_EVENT_FIDO_REQUEST;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;

import com.onegini.mobile.sdk.android.handlers.request.OneginiFidoAuthenticationRequestHandler;
import com.onegini.mobile.sdk.android.handlers.request.callback.OneginiFidoCallback;
import com.onegini.mobile.sdk.android.model.entity.UserProfile;
import com.onegini.mobile.sdk.cordova.util.PluginResultBuilder;

public class FidoAuthenticationRequestHandler implements OneginiFidoAuthenticationRequestHandler {
  private static FidoAuthenticationRequestHandler instance;
  private CallbackContext startAuthenticationCallbackContext;
  private OneginiFidoCallback fidoCallback;

  protected FidoAuthenticationRequestHandler() {
  }

  public static FidoAuthenticationRequestHandler getInstance() {
    if (instance == null) {
      instance = new FidoAuthenticationRequestHandler();
      }

    return instance;
  }

  public void setStartAuthenticationCallbackContext(final CallbackContext startAuthenticationCallbackContext) {
    this.startAuthenticationCallbackContext = startAuthenticationCallbackContext;
  }

  public OneginiFidoCallback getFidoCallback() {
    return fidoCallback;
  }

  @Override
  public void startAuthentication(final UserProfile userProfile, final OneginiFidoCallback fidoCallback) {
    this.fidoCallback = fidoCallback;

    final PluginResult pluginResult = new PluginResultBuilder()
        .withSuccess()
        .shouldKeepCallback()
        .withAuthenticationEvent(AUTH_EVENT_FIDO_REQUEST)
        .build();

    sendStartAuthenticationResult(pluginResult);
  }

  @Override
  public void finishAuthentication() {
    fidoCallback = null;
  }

  private void sendStartAuthenticationResult(final PluginResult pluginResult) {
    if (startAuthenticationCallbackContext != null && !startAuthenticationCallbackContext.isFinished()) {
      startAuthenticationCallbackContext.sendPluginResult(pluginResult);
    }
  }

}

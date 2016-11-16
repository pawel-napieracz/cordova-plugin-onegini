package com.onegini.handler;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;

import com.onegini.mobile.sdk.android.handlers.OneginiLogoutHandler;
import com.onegini.mobile.sdk.android.handlers.error.OneginiLogoutError;
import com.onegini.util.PluginResultBuilder;

public class LogoutHandler implements OneginiLogoutHandler{

  private CallbackContext callbackContext;

  public LogoutHandler(final CallbackContext callbackContext) {
    this.callbackContext = callbackContext;
  }

  @Override
  public void onSuccess() {
    final PluginResult pluginResult = new PluginResultBuilder()
        .withSuccess()
        .build();

    callbackContext.sendPluginResult(pluginResult);

  }

  @Override
  public void onError(final OneginiLogoutError logoutError) {
    final PluginResult pluginResult = new PluginResultBuilder()
        .withError()
        .withOneginiError(logoutError)
        .build();

    callbackContext.sendPluginResult(pluginResult);
  }
}

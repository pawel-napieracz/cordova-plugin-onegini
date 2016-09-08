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
    callbackContext.sendPluginResult(new PluginResultBuilder()
        .withSuccess()
        .build());

  }

  @Override
  public void onError(final OneginiLogoutError oneginiLogoutError) {
    callbackContext.sendPluginResult(new PluginResultBuilder()
        .withError()
        .withErrorDescription(oneginiLogoutError.getErrorDescription())
        .build());
  }
}

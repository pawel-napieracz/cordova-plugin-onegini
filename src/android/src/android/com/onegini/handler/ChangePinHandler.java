package com.onegini.handler;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;

import android.util.Log;
import com.onegini.mobile.sdk.android.handlers.OneginiChangePinHandler;
import com.onegini.mobile.sdk.android.handlers.error.OneginiChangePinError;
import com.onegini.util.PluginResultBuilder;

public class ChangePinHandler implements OneginiChangePinHandler {
  private CallbackContext callbackContext;

  public ChangePinHandler(final CallbackContext callbackContext) {
    this.callbackContext = callbackContext;
  }

  @Override
  public void onSuccess() {
    sendPluginResult(new PluginResultBuilder()
        .withSuccess()
        .withPinLength(5)
        .build());
  }

  @Override
  public void onError(final OneginiChangePinError oneginiChangePinError) {
    sendPluginResult(new PluginResultBuilder()
        .withError()
        .withErrorDescription(oneginiChangePinError.getErrorDescription())
        .build());
  }

  public void setCallbackContext(final CallbackContext callbackContext) {
    this.callbackContext = callbackContext;
  }

  private void sendPluginResult(final PluginResult pluginResult) {
    if (!callbackContext.isFinished()) {
      callbackContext.sendPluginResult(pluginResult);
    }
  }
}

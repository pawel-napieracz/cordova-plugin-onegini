package com.onegini.handler;

import org.apache.cordova.CallbackContext;

import com.onegini.mobile.sdk.android.handlers.error.OneginiDeviceAuthenticationError;
import com.onegini.mobile.sdk.android.internal.handlers.OneginiDeviceAuthenticationHandler;
import com.onegini.util.PluginResultBuilder;

public class DeviceAuthenticationHandler implements OneginiDeviceAuthenticationHandler {

  private CallbackContext callbackContext;

  public DeviceAuthenticationHandler(final CallbackContext callbackContext) {
    this.callbackContext = callbackContext;
  }

  @Override
  public void onSuccess() {
    callbackContext.success();
  }

  @Override
  public void onError(final OneginiDeviceAuthenticationError oneginiDeviceAuthenticationError) {
    callbackContext.sendPluginResult(new PluginResultBuilder()
        .withError()
        .withErrorType(oneginiDeviceAuthenticationError.getErrorType())
        .withErrorDescription(oneginiDeviceAuthenticationError.getErrorDescription())
        .build());
  }
}

package com.onegini.handler;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;

import com.onegini.mobile.sdk.android.handlers.error.OneginiDeviceAuthenticationError;
import com.onegini.mobile.sdk.android.handlers.OneginiDeviceAuthenticationHandler;
import com.onegini.util.PluginResultBuilder;

public class DeviceAuthenticationHandler implements OneginiDeviceAuthenticationHandler {

  private CallbackContext callbackContext;

  public DeviceAuthenticationHandler(final CallbackContext callbackContext) {
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
  public void onError(final OneginiDeviceAuthenticationError oneginiDeviceAuthenticationError) {
    final PluginResult pluginResult = new PluginResultBuilder()
        .withError()
        .withErrorType(oneginiDeviceAuthenticationError.getErrorType())
        .withErrorDescription(oneginiDeviceAuthenticationError.getErrorDescription())
        .build();

    callbackContext.sendPluginResult(pluginResult);
  }
}

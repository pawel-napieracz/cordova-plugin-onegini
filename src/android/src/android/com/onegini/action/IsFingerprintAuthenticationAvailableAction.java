package com.onegini.action;

import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import com.onegini.OneginiCordovaPlugin;

public class IsFingerprintAuthenticationAvailableAction implements OneginiPluginAction {
  @Override
  public void execute(final JSONArray args, final CallbackContext callbackContext, final OneginiCordovaPlugin client) {
    if (client.getOneginiClient().isFingerprintAuthenticationAvailable()) {
      callbackContext.success();
    }
    else {
      callbackContext.error("Fingerprint authentication not available");
    }
  }
}

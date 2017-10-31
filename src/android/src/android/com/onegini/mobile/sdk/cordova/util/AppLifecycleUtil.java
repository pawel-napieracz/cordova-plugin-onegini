package com.onegini.mobile.sdk.cordova.util;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

@SuppressWarnings("unused")
public class AppLifecycleUtil extends CordovaPlugin {

  private static boolean isAppInForeground = true;

  public static boolean isAppInForeground() {
    return isAppInForeground;
  }

  private static void setIsAppInForeground(final boolean value) {
    isAppInForeground = value;
  }

  @Override
  public boolean execute(final String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {
    if ("foreground".equals(action)) {
      setIsAppInForeground(true);
      return true;
    } else if("background".equals(action)){
      setIsAppInForeground(false);
      return true;
    }
    return false;
  }
}

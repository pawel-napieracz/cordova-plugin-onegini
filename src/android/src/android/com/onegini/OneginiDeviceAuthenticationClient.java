package com.onegini;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import com.onegini.handler.DeviceAuthenticationHandler;
import com.onegini.mobile.sdk.android.client.OneginiClient;
import com.onegini.util.ScopesUtil;

public class OneginiDeviceAuthenticationClient extends CordovaPlugin {

  private static final String ACTION_AUTHENTICATE = "authenticate";

  @Override
  public boolean execute(final String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {
    if (ACTION_AUTHENTICATE.equals(action)) {
      authenticate(args, callbackContext);
      return true;
    }

    return false;
  }

  private void authenticate(final JSONArray args, final CallbackContext callbackContext) throws JSONException {
    final DeviceAuthenticationHandler deviceAuthenticationHandler = new DeviceAuthenticationHandler(callbackContext);
    final String[] scopes = ScopesUtil.getScopesFromActionArguments(args);

    cordova.getThreadPool().execute(new Runnable() {
      @Override
      public void run() {
        getOneginiClient().getDeviceClient().authenticateDevice(scopes, deviceAuthenticationHandler);
      }
    });
  }

  private OneginiClient getOneginiClient() {
    return OneginiSDK.getOneginiClient(cordova.getActivity().getApplicationContext());
  }
}

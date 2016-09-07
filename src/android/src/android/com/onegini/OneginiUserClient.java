package com.onegini;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import com.onegini.handler.PinValidationHandler;
import com.onegini.mobile.sdk.android.client.*;
import com.onegini.mobile.sdk.android.client.OneginiClient;

public class OneginiUserClient extends CordovaPlugin {

  private static final String ACTION_VALIDATE_PIN_WITH_POLICY = "validatePinWithPolicy";

  @Override
  public boolean execute(final String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {
    if (ACTION_VALIDATE_PIN_WITH_POLICY.equals(action)) {
      validatePinWithPolicy(args, callbackContext);
      return true;
    }

    return false;
  }

  private void validatePinWithPolicy(final JSONArray args, final CallbackContext callbackContext) throws JSONException {
    final String pin = args.getJSONObject(0).getString("pin");
    final PinValidationHandler pinValidationHandler = new PinValidationHandler(callbackContext);

    cordova.getThreadPool().execute(new Runnable() {
      @Override
      public void run() {
        getOneginiClient().getUserClient().validatePinWithPolicy(pin.toCharArray(), pinValidationHandler);
      }
    });
  }

  private OneginiClient getOneginiClient() {
    return OneginiSDK.getOneginiClient(cordova.getActivity().getApplicationContext());
  }
}

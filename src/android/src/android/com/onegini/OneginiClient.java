package com.onegini;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;

import com.onegini.handler.InitializationHandler;

public class OneginiClient extends CordovaPlugin {

  @Override
  public void initialize(final CordovaInterface cordova, final CordovaWebView webView) {
    super.initialize(cordova, webView);
  }

  @Override
  public boolean execute(final String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {
    if (action.equals("start")) {
      OneginiSDK.getOneginiClient(cordova.getActivity().getApplicationContext()).start(new InitializationHandler(callbackContext));
      return true;
    }

    return false;
  }
}

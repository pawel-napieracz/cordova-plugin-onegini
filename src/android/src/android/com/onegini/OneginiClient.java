package com.onegini;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.Intent;
import android.net.Uri;
import com.onegini.handler.InitializationHandler;

public class OneginiClient extends CordovaPlugin {

  @Override
  public boolean execute(final String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {
    if ("start".equals(action)) {
      start(callbackContext);
      return true;
    }

    return false;
  }

  @Override
  public void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    handleRedirection(intent.getData());
  }

  private void handleRedirection(final Uri uri) {
    final com.onegini.mobile.android.sdk.client.OneginiClient client = OneginiSDK.getOneginiClient(cordova.getActivity().getApplicationContext());
    if (uri != null && client.getConfigModel().getRedirectUri().startsWith(uri.getScheme())) {
      client.getUserClient().handleAuthorizationCallback(uri);
    }
  }

  private void start(final CallbackContext callbackContext) {
    cordova.getThreadPool().execute(new Runnable() {
      public void run() {
        OneginiSDK.getOneginiClient(cordova.getActivity().getApplicationContext()).start(new InitializationHandler(callbackContext));
      }
    });
  }
}

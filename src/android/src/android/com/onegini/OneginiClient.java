package com.onegini;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.Intent;
import android.net.Uri;
import com.onegini.handler.InitializationHandler;
import com.onegini.mobile.sdk.android.model.OneginiClientConfigModel;

public class OneginiClient extends CordovaPlugin {

  private static final String ACTION_START = "start";

  @Override
  public boolean execute(final String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {
    if (ACTION_START.equals(action)) {
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
    final com.onegini.mobile.sdk.android.client.OneginiClient client = OneginiSDK.getOneginiClient(cordova.getActivity().getApplicationContext());
    if (uri != null && client.getConfigModel().getRedirectUri().startsWith(uri.getScheme())) {
      client.getUserClient().handleRegistrationCallback(uri);
    }
  }

  private void start(final CallbackContext callbackContext) {
    final OneginiClientConfigModel configModel = OneginiSDK.getOneginiClient(cordova.getActivity().getApplicationContext()).getConfigModel();

    cordova.getThreadPool().execute(new Runnable() {
      public void run() {
        OneginiSDK.getOneginiClient(cordova.getActivity().getApplicationContext()).start(new InitializationHandler(callbackContext, configModel));
      }
    });
  }
}

package com.onegini;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.Intent;
import android.net.Uri;
import com.onegini.handler.RegistrationHandler;
import com.onegini.handler.CreatePinRequestHandler;
import com.onegini.mobile.android.sdk.handlers.request.callback.OneginiPinCallback;
import com.onegini.util.PluginResultBuilder;

public class OneginiUserClient extends CordovaPlugin {

  @Override
  public boolean execute(final String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {
    if ("startRegistration".equals(action)) {
      startRegistration(args, callbackContext);
      return true;
    }
    else if("createPIN".equals(action)) {
      createPIN(args, callbackContext);
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

  private void startRegistration(final JSONArray args, final CallbackContext callbackContext) throws JSONException {
    final String[] scopes = new String[args.length()];
    for (int i = 0; i < args.length(); i++) {
      scopes[i] = args.getString(i);
    }

    cordova.getThreadPool().execute(new Runnable() {
      public void run() {
        OneginiSDK.getOneginiClient(cordova.getActivity().getApplicationContext()).getUserClient().registerUser(scopes, new RegistrationHandler(callbackContext));
      }
    });
  }

  private void createPIN(final JSONArray args, final CallbackContext callbackContext) throws JSONException {
    final String pin = args.getString(0);
    CreatePinRequestHandler.getInstance().setCordovaCallback(callbackContext);
    OneginiPinCallback pinCallback = CreatePinRequestHandler.getInstance().getPinCallback();

    if (pinCallback == null) {
      final PluginResult pluginResult = new PluginResultBuilder()
          .withErrorDescription("Plugin: No pincallback set")
          .build();
      callbackContext.sendPluginResult(pluginResult);
    } else {
      pinCallback.acceptAuthenticationRequest(pin.toCharArray());
    }
  }
}

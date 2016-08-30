package com.onegini;

import java.util.Set;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

import com.onegini.handler.CreatePinRequestHandler;
import com.onegini.handler.RegistrationHandler;
import com.onegini.mobile.android.sdk.handlers.request.callback.OneginiPinCallback;
import com.onegini.mobile.android.sdk.model.entity.UserProfile;
import com.onegini.util.PluginResultBuilder;

public class OneginiUserRegistrationClient extends CordovaPlugin {

  @Override
  public boolean execute(final String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {
    if ("startRegistration".equals(action)) {
      startRegistration(args, callbackContext);
      return true;
    } else if ("createPin".equals(action)) {
      createPin(args, callbackContext);
      return true;
    } else if ("getUserProfiles".equals(action)) {
      getUserProfiles(callbackContext);
      return true;
    }

    return false;
  }

  private void startRegistration(final JSONArray args, final CallbackContext callbackContext) throws JSONException {
    final String[] scopes = new String[args.length()];
    for (int i = 0; i < args.length(); i++) {
      scopes[i] = args.getString(i);
    }

    cordova.getThreadPool().execute(new Runnable() {
      public void run() {
        OneginiSDK.getOneginiClient(cordova.getActivity().getApplicationContext()).getUserClient()
            .registerUser(scopes, new RegistrationHandler(callbackContext));
      }
    });
  }

  private void createPin(final JSONArray args, final CallbackContext callbackContext) throws JSONException {
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

  public void getUserProfiles(final CallbackContext callbackContext) {
    cordova.getThreadPool().execute(new Runnable() {
      public void run() {
       Set<UserProfile> userProfiles = OneginiSDK.getOneginiClient(cordova.getActivity().getApplicationContext()).getUserClient()
            .getUserProfiles();

        final PluginResult pluginResult = new PluginResultBuilder()
            .withSuccess()
            .addUserProfiles(userProfiles)
            .build();

        callbackContext.sendPluginResult(pluginResult);
      }
    });
  }
}

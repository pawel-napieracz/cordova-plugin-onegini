package com.onegini;

import static com.onegini.OneginiCordovaPluginConstants.ACTION_CREATE_PIN;
import static com.onegini.OneginiCordovaPluginConstants.ACTION_GET_USER_PROFILES;
import static com.onegini.OneginiCordovaPluginConstants.ACTION_START;
import static com.onegini.OneginiCordovaPluginConstants.PARAM_SCOPES;

import java.util.Set;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

import com.onegini.handler.CreatePinRequestHandler;
import com.onegini.handler.RegistrationHandler;
import com.onegini.mobile.sdk.android.handlers.request.callback.OneginiPinCallback;
import com.onegini.mobile.sdk.android.model.entity.UserProfile;
import com.onegini.util.PluginResultBuilder;

public class OneginiUserRegistrationClient extends CordovaPlugin {

  @Override
  public boolean execute(final String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {
    if (ACTION_START.equals(action)) {
      startRegistration(args, callbackContext);
      return true;
    } else if (ACTION_CREATE_PIN.equals(action)) {
      createPin(args, callbackContext);
      return true;
    } else if (ACTION_GET_USER_PROFILES.equals(action)) {
      getUserProfiles(callbackContext);
      return true;
    }

    return false;
  }

  private void startRegistration(final JSONArray args, final CallbackContext callbackContext) throws JSONException {
    final String[] scopes;
    JSONArray scopesJSON;

    try {
      scopesJSON = args.getJSONObject(0).getJSONArray(PARAM_SCOPES);
    } catch (JSONException e) {
      scopesJSON = new JSONArray();
    }

    scopes = new String[scopesJSON.length()];
    for (int i = 0; i < scopesJSON.length(); i++) {
      scopes[i] = scopesJSON.getString(i);
    }

    CreatePinRequestHandler.getInstance().setRegistrationCallback(callbackContext);

    cordova.getThreadPool().execute(new Runnable() {
      public void run() {
        OneginiSDK.getOneginiClient(cordova.getActivity().getApplicationContext()).getUserClient()
            .registerUser(scopes, new RegistrationHandler(callbackContext));
      }
    });
  }

  private void createPin(final JSONArray args, final CallbackContext callbackContext) throws JSONException {
    final String pin = args.getJSONObject(0).getString("pin");
    CreatePinRequestHandler.getInstance().setCreatePinCallback(callbackContext);
    OneginiPinCallback pinCallback = CreatePinRequestHandler.getInstance().getPinCallback();

    if (pinCallback == null) {
      final PluginResult pluginResult = new PluginResultBuilder()
          .withErrorDescription("Onegini: createPin called, but no registration in process. Did you call 'onegini.user.register.start'?")
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

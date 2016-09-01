package com.onegini;

import java.util.Set;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import android.support.annotation.Nullable;
import com.onegini.handler.AuthenticationHandler;
import com.onegini.handler.PinAuthenticationRequestHandler;
import com.onegini.mobile.android.sdk.client.OneginiClient;
import com.onegini.mobile.android.sdk.handlers.request.callback.OneginiPinCallback;
import com.onegini.mobile.android.sdk.model.entity.UserProfile;
import com.onegini.util.PluginResultBuilder;

public class OneginiUserAuthenticationClient extends CordovaPlugin {

  @Override
  public boolean execute(final String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {
    if ("start".equals(action)) {
      startAuthentication(args, callbackContext);
      return true;
    } else if ("checkPin".equals(action)) {
      checkPin(args, callbackContext);
      return true;
    }

    return false;
  }

  private void startAuthentication(final JSONArray args, final CallbackContext callbackContext) {
    final UserProfile userProfile;

    try {
      userProfile = findUserProfileById(args.getJSONObject(0).getString("profileId"));
    } catch (JSONException e) {
      callbackContext.sendPluginResult(new PluginResultBuilder()
          .withError()
          .withErrorDescription("Onegini: Argument Provided is not a valid profile object")
          .build());

      return;
    }

    if (userProfile == null) {
      callbackContext.sendPluginResult(new PluginResultBuilder()
          .withError()
          .withErrorDescription("Onegini: No such profile registered")
          .build());

      return;
    }

    if (userProfile == getOneginiClient().getUserClient().getAuthenticatedUserProfile()) {
      callbackContext.sendPluginResult(new PluginResultBuilder()
          .withError()
          .withErrorDescription("Onegini: User already authenticated")
          .build());
    }

    PinAuthenticationRequestHandler.getInstance().setAuthenticationCordovaCallback(callbackContext);

    cordova.getThreadPool().execute(new Runnable() {
      public void run() {
        getOneginiClient().getUserClient()
            .authenticateUser(userProfile, new AuthenticationHandler(callbackContext));
      }
    });
  }

  private void checkPin(final JSONArray args, final CallbackContext callbackContext) throws JSONException {
    final String pin = args.getString(0);
    final OneginiPinCallback pinCallback = PinAuthenticationRequestHandler.getInstance().getPinCallback();
    PinAuthenticationRequestHandler.getInstance().setCheckPinCordovaCallback(callbackContext);

    if (pinCallback == null) {
      callbackContext.sendPluginResult(new PluginResultBuilder()
          .withErrorDescription("Onegini: createPin called, but no registration in process. Did you call 'onegini.user.register.start'?")
          .build());
    } else {
      pinCallback.acceptAuthenticationRequest(pin.toCharArray());
    }
  }

  @Nullable
  private UserProfile findUserProfileById(final String id) {
    Set<UserProfile> userProfiles = getOneginiClient().getUserClient().getUserProfiles();
    for (UserProfile userProfile : userProfiles) {
      if (id.equals(userProfile.getProfileId())) {
        return userProfile;
      }
    }

    return null;
  }

  private OneginiClient getOneginiClient() {
    return OneginiSDK.getOneginiClient(cordova.getActivity().getApplicationContext());
  }
}

package com.onegini;

import static com.onegini.OneginiCordovaPluginConstants.ACTION_CHECK_PIN;
import static com.onegini.OneginiCordovaPluginConstants.ACTION_START;
import static com.onegini.OneginiCordovaPluginConstants.PARAM_PROFILE_ID;

import java.util.Set;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import com.onegini.handler.AuthenticationHandler;
import com.onegini.handler.PinAuthenticationRequestHandler;
import com.onegini.mobile.sdk.android.client.OneginiClient;
import com.onegini.mobile.sdk.android.handlers.request.callback.OneginiPinCallback;
import com.onegini.mobile.sdk.android.model.entity.UserProfile;
import com.onegini.util.PluginResultBuilder;
import com.onegini.util.UserProfileUtil;

public class OneginiUserAuthenticationClient extends CordovaPlugin {

  @Override
  public boolean execute(final String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {
    if (ACTION_START.equals(action)) {
      startAuthentication(args, callbackContext);
      return true;
    } else if (ACTION_CHECK_PIN.equals(action)) {
      checkPin(args, callbackContext);
      return true;
    }

    return false;
  }

  private void startAuthentication(final JSONArray args, final CallbackContext callbackContext) {
    final UserProfile userProfile;

    try {
      userProfile = getUserProfile(args);

    } catch (JSONException e) {
      callbackContext.sendPluginResult(new PluginResultBuilder()
          .withError()
          .withErrorDescription(OneginiCordovaPluginConstants.ERROR_ARGUMENT_IS_NOT_A_VALID_PROFILE_OBJECT)
          .build());

      return;
    }

    if (userProfile == null) {
      callbackContext.sendPluginResult(new PluginResultBuilder()
          .withError()
          .withErrorDescription(OneginiCordovaPluginConstants.ERROR_PROFILE_NOT_REGISTERED)
          .build());

      return;
    }

    if (userProfile == getOneginiClient().getUserClient().getAuthenticatedUserProfile()) {
      callbackContext.sendPluginResult(new PluginResultBuilder()
          .withError()
          .withErrorDescription(OneginiCordovaPluginConstants.ERROR_USER_ALREADY_AUTHENTICATED)
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

  private UserProfile getUserProfile(final JSONArray args) throws JSONException {
    String profileId = args.getJSONObject(0).getString(PARAM_PROFILE_ID);
    Set<UserProfile> registeredUserProfiles = getOneginiClient().getUserClient().getUserProfiles();

    return UserProfileUtil.findUserProfileById(profileId, registeredUserProfiles);
  }

  private void checkPin(final JSONArray args, final CallbackContext callbackContext) throws JSONException {
    final String pin = args.getString(0);
    final OneginiPinCallback pinCallback = PinAuthenticationRequestHandler.getInstance().getPinCallback();
    PinAuthenticationRequestHandler.getInstance().setCheckPinCordovaCallback(callbackContext);

    if (pinCallback == null) {
      callbackContext.sendPluginResult(new PluginResultBuilder()
          .withErrorDescription(OneginiCordovaPluginConstants.ERROR_CREATE_PIN_NO_REGISTRATION_IN_PROGRESS)
          .build());
    } else {
      pinCallback.acceptAuthenticationRequest(pin.toCharArray());
    }
  }

  private OneginiClient getOneginiClient() {
    return OneginiSDK.getOneginiClient(cordova.getActivity().getApplicationContext());
  }
}

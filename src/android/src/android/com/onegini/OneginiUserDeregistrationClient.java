package com.onegini;

import static com.onegini.OneginiCordovaPluginConstants.ERROR_PROFILE_NOT_REGISTERED;
import static com.onegini.OneginiCordovaPluginConstants.PARAM_PROFILE_ID;

import java.util.Set;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import com.onegini.handler.DeregistrationHandler;
import com.onegini.mobile.sdk.android.model.entity.UserProfile;
import com.onegini.util.PluginResultBuilder;
import com.onegini.util.UserProfileUtil;

public class OneginiUserDeregistrationClient extends CordovaPlugin {

  private static final String ACTION_DEREGISTER = "deregister";

  @Override
  public boolean execute(final String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {
    if (ACTION_DEREGISTER.equals(action)) {
      startDeregistration(args, callbackContext);
      return true;
    }
    return false;
  }

  private void startDeregistration(final JSONArray args, final CallbackContext callbackContext) throws JSONException {
    final UserProfile userProfile = getUserProfile(args);

    if (userProfile == null) {
      callbackContext.sendPluginResult(new PluginResultBuilder()
          .withError()
          .withErrorDescription(ERROR_PROFILE_NOT_REGISTERED)
          .build());

      return;
    }

    final DeregistrationHandler deregistrationHandler = new DeregistrationHandler(callbackContext);

    cordova.getThreadPool().execute(new Runnable() {
      public void run() {
        getOneginiClient().getUserClient()
            .deregisterUser(userProfile, deregistrationHandler);
      }
    });
  }

  private UserProfile getUserProfile(final JSONArray args) throws JSONException {
    String profileId = args.getJSONObject(0).getString(PARAM_PROFILE_ID);
    Set<UserProfile> registeredUserProfiles = getOneginiClient().getUserClient().getUserProfiles();

    return UserProfileUtil.findUserProfileById(profileId, registeredUserProfiles);
  }

  private com.onegini.mobile.sdk.android.client.OneginiClient getOneginiClient() {
    return OneginiSDK.getInstance().getOneginiClient(cordova.getActivity().getApplicationContext());
  }


}

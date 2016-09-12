package com.onegini;

import static com.onegini.OneginiCordovaPluginConstants.ERROR_ARGUMENT_IS_NOT_A_VALID_PROFILE_OBJECT;
import static com.onegini.OneginiCordovaPluginConstants.ERROR_CREATE_PIN_NO_REGISTRATION_IN_PROGRESS;
import static com.onegini.OneginiCordovaPluginConstants.ERROR_PLUGIN_INTERNAL_ERROR;
import static com.onegini.OneginiCordovaPluginConstants.PARAM_PROFILE_ID;

import java.util.Set;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

import com.onegini.handler.CreatePinRequestHandler;
import com.onegini.handler.RegistrationHandler;
import com.onegini.mobile.sdk.android.client.OneginiClient;
import com.onegini.mobile.sdk.android.handlers.request.callback.OneginiPinCallback;
import com.onegini.mobile.sdk.android.model.entity.UserProfile;
import com.onegini.util.PluginResultBuilder;
import com.onegini.util.ScopesUtil;
import com.onegini.util.UserProfileUtil;

public class OneginiUserRegistrationClient extends CordovaPlugin {

  private static final String ACTION_START = "start";
  private static final String ACTION_CREATE_PIN = "createPin";
  private static final String ACTION_GET_USER_PROFILES = "getUserProfiles";
  private static final String ACTION_IS_USER_REGISTERED = "isUserRegistered";

  private RegistrationHandler registrationHandler;

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
    } else if (ACTION_IS_USER_REGISTERED.equals(action)) {
      isUserRegistered(args, callbackContext);
    }

    return false;
  }

  private void startRegistration(final JSONArray args, final CallbackContext startRegistrationCallbackContext) throws JSONException {
    final String[] scopes = ScopesUtil.getScopesFromActionArguments(args);

    CreatePinRequestHandler.getInstance().setRegistrationCallbackContext(startRegistrationCallbackContext);
    registrationHandler = new RegistrationHandler(startRegistrationCallbackContext);

    cordova.getThreadPool().execute(new Runnable() {
      public void run() {
        getOneginiClient().getUserClient()
            .registerUser(scopes, registrationHandler);
      }
    });
  }

  private void createPin(final JSONArray args, final CallbackContext createPinCallbackContext) throws JSONException {
    final String pin = args.getJSONObject(0).getString("pin");
    OneginiPinCallback pinCallback = CreatePinRequestHandler.getInstance().getPinCallback();
    CreatePinRequestHandler.getInstance().setCreatePinCallback(createPinCallbackContext);

    if (pinCallback == null) {
      final PluginResult pluginResult = new PluginResultBuilder()
          .withErrorDescription(ERROR_CREATE_PIN_NO_REGISTRATION_IN_PROGRESS)
          .build();
      createPinCallbackContext.sendPluginResult(pluginResult);
    } else {
      registrationHandler.setCallbackContext(createPinCallbackContext);
      pinCallback.acceptAuthenticationRequest(pin.toCharArray());
    }
  }

  private void isUserRegistered(final JSONArray args, final CallbackContext callbackContext) {
    final Set<UserProfile> userProfiles = getOneginiClient().getUserClient().getUserProfiles();
    final String userProfileId;
    final PluginResult pluginResult;
    final boolean userIsRegistered;

    try {
      userProfileId = args.getJSONObject(0).getString(PARAM_PROFILE_ID);
    } catch (JSONException e) {
      callbackContext.sendPluginResult(new PluginResultBuilder()
          .withError()
          .withErrorDescription(ERROR_ARGUMENT_IS_NOT_A_VALID_PROFILE_OBJECT)
          .build());

      return;
    }

    userIsRegistered = UserProfileUtil.findUserProfileById(userProfileId, userProfiles) != null;
    pluginResult = new PluginResult(PluginResult.Status.OK, userIsRegistered);
    callbackContext.sendPluginResult(pluginResult);
  }

  public void getUserProfiles(final CallbackContext callbackContext) {
    cordova.getThreadPool().execute(new Runnable() {
      public void run() {
        final Set<UserProfile> userProfiles = getOneginiClient().getUserClient().getUserProfiles();
        final JSONArray resultPayload;

        try {
          resultPayload = UserProfileUtil.ProfileSetToJSONArray(userProfiles);
        } catch (JSONException e) {
          callbackContext.error(ERROR_PLUGIN_INTERNAL_ERROR + " : " + e.getMessage());
          return;
        }

        callbackContext.success(resultPayload);
      }
    });
  }

  private OneginiClient getOneginiClient() {
    return OneginiSDK.getOneginiClient(cordova.getActivity().getApplicationContext());
  }
}

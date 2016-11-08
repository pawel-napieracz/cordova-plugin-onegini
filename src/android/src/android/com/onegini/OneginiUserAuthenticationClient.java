package com.onegini;

import static com.onegini.OneginiCordovaPluginConstants.ERROR_ARGUMENT_IS_NOT_A_VALID_PROFILE_OBJECT;
import static com.onegini.OneginiCordovaPluginConstants.ERROR_FINGERPRINT_NO_AUTHENTICATION_IN_PROGRESS;
import static com.onegini.OneginiCordovaPluginConstants.ERROR_NO_USER_AUTHENTICATED;
import static com.onegini.OneginiCordovaPluginConstants.ERROR_PROFILE_NOT_REGISTERED;
import static com.onegini.OneginiCordovaPluginConstants.ERROR_USER_ALREADY_AUTHENTICATED;
import static com.onegini.OneginiCordovaPluginConstants.PARAM_ACCEPT;
import static com.onegini.OneginiCordovaPluginConstants.PARAM_PROFILE_ID;

import java.util.Set;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import com.onegini.handler.AuthenticationHandler;
import com.onegini.handler.FingerprintAuthenticationRequestHandler;
import com.onegini.handler.LogoutHandler;
import com.onegini.handler.PinAuthenticationRequestHandler;
import com.onegini.mobile.sdk.android.client.OneginiClient;
import com.onegini.mobile.sdk.android.handlers.request.callback.OneginiFingerprintCallback;
import com.onegini.mobile.sdk.android.handlers.request.callback.OneginiPinCallback;
import com.onegini.mobile.sdk.android.model.entity.UserProfile;
import com.onegini.util.ActionArgumentsUtil;
import com.onegini.util.PluginResultBuilder;
import com.onegini.util.UserProfileUtil;

public class OneginiUserAuthenticationClient extends CordovaPlugin {

  private static final String ACTION_START = "start";
  private static final String ACTION_PROVIDE_PIN = "providePin";
  private static final String ACTION_RESPOND_TO_FINGERPRINT_REQUEST = "respondToFingerprintRequest";
  private static final String ACTION_FALLBACK_TO_PIN = "fallbackToPin";
  private static final String ACTION_REAUTHENTICATE = "reauthenticate";
  private final static String ACTION_LOGOUT = "logout";
  private static final String ACTION_GET_AUTHENTICATED_USER_PROFILE = "getAuthenticatedUserProfile";
  private AuthenticationHandler authenticationHandler;

  @Override
  public boolean execute(final String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {
    if (ACTION_START.equals(action)) {
      startAuthentication(args, callbackContext);
      return true;
    } else if (ACTION_PROVIDE_PIN.equals(action)) {
      providePin(args, callbackContext);
      return true;
    } else if (ACTION_GET_AUTHENTICATED_USER_PROFILE.equals(action)) {
      getAuthenticatedUserProfile(callbackContext);
      return true;
    } else if (ACTION_REAUTHENTICATE.equals(action)) {
      reauthenticate(args, callbackContext);
      return true;
    } else if (ACTION_LOGOUT.equals(action)) {
      logout(callbackContext);
      return true;
    } else if (ACTION_RESPOND_TO_FINGERPRINT_REQUEST.equals(action)) {
      respondToFingerprintRequest(callbackContext, args);
      return true;
    } else if (ACTION_FALLBACK_TO_PIN.equals(action)) {
      fallbackToPin(callbackContext);
      return true;
    }

    return false;
  }

  private void startAuthentication(final JSONArray args, final CallbackContext callbackContext) {
    final UserProfile userProfile;

    try {
      userProfile = getUserProfileForAuthentication(args);
    } catch (Exception e) {
      callbackContext.sendPluginResult(new PluginResultBuilder()
          .withError()
          .withErrorDescription(e.getMessage())
          .build());

      return;
    }

    final UserProfile authenticatedUserProfile = getOneginiClient().getUserClient().getAuthenticatedUserProfile();
    if (userProfile.equals(authenticatedUserProfile)) {
      callbackContext.sendPluginResult(new PluginResultBuilder()
          .withError()
          .withErrorDescription(ERROR_USER_ALREADY_AUTHENTICATED)
          .build());

      return;
    }

    PinAuthenticationRequestHandler.getInstance().setStartAuthenticationCallbackContext(callbackContext);
    FingerprintAuthenticationRequestHandler.getInstance().setStartAuthenticationCallbackContext(callbackContext);
    authenticationHandler = new AuthenticationHandler(callbackContext);

    cordova.getThreadPool().execute(new Runnable() {
      public void run() {
        getOneginiClient().getUserClient().authenticateUser(userProfile, authenticationHandler);
      }
    });
  }

  private void reauthenticate(final JSONArray args, final CallbackContext callbackContext) {
    final UserProfile userProfile;

    try {
      userProfile = getUserProfileForAuthentication(args);
    } catch (Exception e) {
      callbackContext.sendPluginResult(new PluginResultBuilder()
          .withError()
          .withErrorDescription(e.getMessage())
          .build());

      return;
    }

    PinAuthenticationRequestHandler.getInstance().setStartAuthenticationCallbackContext(callbackContext);
    FingerprintAuthenticationRequestHandler.getInstance().setStartAuthenticationCallbackContext(callbackContext);
    authenticationHandler = new AuthenticationHandler(callbackContext);

    cordova.getThreadPool().execute(new Runnable() {
      @Override
      public void run() {
        getOneginiClient().getUserClient()
            .reauthenticateUser(userProfile, authenticationHandler);
      }
    });
  }

  private UserProfile getUserProfileForAuthentication(final JSONArray args) throws Exception {
    final UserProfile userProfile;

    try {
      userProfile = getUserProfile(args);

    } catch (JSONException e) {
      throw new Exception(ERROR_ARGUMENT_IS_NOT_A_VALID_PROFILE_OBJECT);
    }

    if (userProfile == null) {
      throw new Exception(ERROR_PROFILE_NOT_REGISTERED);
    }

    return userProfile;
  }

  private UserProfile getUserProfile(final JSONArray args) throws JSONException {
    String profileId = args.getJSONObject(0).getString(PARAM_PROFILE_ID);
    Set<UserProfile> registeredUserProfiles = getOneginiClient().getUserClient().getUserProfiles();

    return UserProfileUtil.findUserProfileById(profileId, registeredUserProfiles);
  }

  private void providePin(final JSONArray args, final CallbackContext callbackContext) throws JSONException {
    final String pin = ActionArgumentsUtil.getPinFromArguments(args);
    final OneginiPinCallback pinCallback = PinAuthenticationRequestHandler.getInstance().getPinCallback();
    authenticationHandler.setCallbackContext(callbackContext);

    if (pinCallback == null) {
      callbackContext.sendPluginResult(new PluginResultBuilder()
          .withErrorDescription(OneginiCordovaPluginConstants.ERROR_PROVIDE_PIN_NO_AUTHENTICATION_IN_PROGRESS)
          .build());

      return;
    }

    pinCallback.acceptAuthenticationRequest(pin.toCharArray());

  }

  private void respondToFingerprintRequest(final CallbackContext callbackContext, final JSONArray args) {
    final OneginiFingerprintCallback fingerprintCallback = FingerprintAuthenticationRequestHandler.getInstance().getFingerprintCallback();
    final boolean shouldAccept;

    if (fingerprintCallback == null) {
      callbackContext.sendPluginResult(new PluginResultBuilder()
          .withErrorDescription(ERROR_FINGERPRINT_NO_AUTHENTICATION_IN_PROGRESS)
          .build());

      return;
    }

    try {
      shouldAccept = args.getJSONObject(0).getBoolean(PARAM_ACCEPT);
    } catch (JSONException e) {
      callbackContext.sendPluginResult(new PluginResultBuilder()
          .withErrorDescription(e.getMessage())
          .build());

      return;
    }

    cordova.getThreadPool().execute(new Runnable() {
      @Override
      public void run() {
        if (shouldAccept) {
          fingerprintCallback.acceptAuthenticationRequest();
        } else {
          fingerprintCallback.denyAuthenticationRequest();
        }
      }
    });
  }

  private void fallbackToPin(final CallbackContext callbackContext) {
    cordova.getThreadPool().execute(new Runnable() {
      @Override
      public void run() {
        final OneginiFingerprintCallback fingerprintCallback = FingerprintAuthenticationRequestHandler.getInstance().getFingerprintCallback();

        if (fingerprintCallback == null) {
          return;
        }

        fingerprintCallback.fallbackToPin();
      }
    });
  }

  private void logout(final CallbackContext callbackContext) {
    cordova.getThreadPool().execute(new Runnable() {
      @Override
      public void run() {
        getOneginiClient().getUserClient()
            .logout(new LogoutHandler(callbackContext));
      }
    });
  }

  private void getAuthenticatedUserProfile(final CallbackContext callbackContext) {
    cordova.getThreadPool().execute(new Runnable() {
      @Override
      public void run() {
        final UserProfile authenticatedUserProfile = getOneginiClient().getUserClient().getAuthenticatedUserProfile();

        PluginResultBuilder pluginResultBuilder = new PluginResultBuilder();
        if (authenticatedUserProfile == null) {
          pluginResultBuilder
              .withError()
              .withErrorDescription(ERROR_NO_USER_AUTHENTICATED);
        } else {
          pluginResultBuilder
              .withSuccess()
              .withProfileId(authenticatedUserProfile);
        }

        callbackContext.sendPluginResult(pluginResultBuilder.build());
      }
    });
  }

  private OneginiClient getOneginiClient() {
    return OneginiSDK.getInstance().getOneginiClient(cordova.getActivity().getApplicationContext());
  }
}

package com.onegini;

import static com.onegini.OneginiCordovaPluginConstants.ERROR_NO_SUCH_AUTHENTICATOR;
import static com.onegini.OneginiCordovaPluginConstants.ERROR_NO_USER_AUTHENTICATED;
import static com.onegini.OneginiCordovaPluginConstants.ERROR_PLUGIN_INTERNAL_ERROR;

import java.util.Set;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import com.onegini.mobile.sdk.android.model.OneginiAuthenticator;
import com.onegini.mobile.sdk.android.model.entity.UserProfile;
import com.onegini.util.ActionArgumentsUtil;
import com.onegini.util.AuthenticatorUtil;
import com.onegini.util.PluginResultBuilder;

public class OneginiAuthenticatorsClient extends CordovaPlugin {
  private static final String ACTION_GET_REGISTERED_AUTHENTICATORS = "getRegistered";
  private static final String ACTION_GET_ALL_AUTHENTICATORS = "getAll";
  private static final String ACTION_GET_NOT_REGISTERED_AUTHENTICATORS = "getNotRegistered";
  private static final String ACTION_SET_PREFERRED_AUTHENTICATOR = "setPreferred";

  @Override
  public boolean execute(final String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {
    if (ACTION_GET_REGISTERED_AUTHENTICATORS.equals(action)) {
      getAuthenticators(callbackContext, action);
      return true;
    } else if (ACTION_GET_ALL_AUTHENTICATORS.equals(action)) {
      getAuthenticators(callbackContext, action);
      return true;
    } else if (ACTION_GET_NOT_REGISTERED_AUTHENTICATORS.equals(action)) {
      getAuthenticators(callbackContext, action);
      return true;
    } else if (ACTION_SET_PREFERRED_AUTHENTICATOR.equals(action)) {
      setPreferredAuthenticator(args, callbackContext);
      return true;
    }

    return false;
  }

  private void getAuthenticators(final CallbackContext callbackContext, final String action) {
    cordova.getThreadPool().execute(new Runnable() {
      @Override
      public void run() {
        final UserProfile userProfile = getOneginiClient().getUserClient().getAuthenticatedUserProfile();

        if (userProfile == null) {
          callbackContext.sendPluginResult(new PluginResultBuilder()
              .withErrorDescription(ERROR_NO_USER_AUTHENTICATED)
              .build());

          return;
        }

        final Set<OneginiAuthenticator> authenticatorSet;
        if (ACTION_GET_REGISTERED_AUTHENTICATORS.equals(action)) {
          authenticatorSet = getOneginiClient().getUserClient().getRegisteredAuthenticators(userProfile);
        } else if (ACTION_GET_NOT_REGISTERED_AUTHENTICATORS.equals(action)){
          authenticatorSet = getOneginiClient().getUserClient().getNotRegisteredAuthenticators(userProfile);
        } else if (ACTION_GET_ALL_AUTHENTICATORS.equals(action)) {
          authenticatorSet = getOneginiClient().getUserClient().getAllAuthenticators(userProfile);
        } else {
          return;
        }

        final JSONArray authenticatorJSONArray;
        try {
          authenticatorJSONArray = AuthenticatorUtil.AuthenticatorSetToJSONArray(authenticatorSet);
        } catch (JSONException e) {
          callbackContext.sendPluginResult(new PluginResultBuilder()
              .withErrorDescription(ERROR_PLUGIN_INTERNAL_ERROR)
              .build());

          return;
        }

        callbackContext.success(authenticatorJSONArray);
      }
    });
  }

  private void setPreferredAuthenticator(final JSONArray args, final CallbackContext callbackContext) throws JSONException {
    final UserProfile userProfile = getOneginiClient().getUserClient().getAuthenticatedUserProfile();
    if (userProfile == null) {
      callbackContext.sendPluginResult(new PluginResultBuilder()
          .withErrorDescription(ERROR_NO_USER_AUTHENTICATED)
          .build());

      return;
    }

    final Set<OneginiAuthenticator> authenticatorSet = getOneginiClient().getUserClient().getRegisteredAuthenticators(userProfile);
    final OneginiAuthenticator authenticator = ActionArgumentsUtil.getAuthenticatorFromArguments(args, authenticatorSet);

    if (authenticator == null) {
      callbackContext.sendPluginResult(new PluginResultBuilder()
          .withErrorDescription(ERROR_NO_SUCH_AUTHENTICATOR)
          .build());

      return;
    }

    getOneginiClient().getUserClient().setPreferredAuthenticator(authenticator);

    callbackContext.sendPluginResult(new PluginResultBuilder()
        .withSuccess()
        .build());
  }

  private com.onegini.mobile.sdk.android.client.OneginiClient getOneginiClient() {
    return OneginiSDK.getInstance().getOneginiClient(cordova.getActivity().getApplicationContext());
  }
}

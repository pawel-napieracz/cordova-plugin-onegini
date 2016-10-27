package com.onegini;

import static com.onegini.OneginiCordovaPluginConstants.ERROR_NO_SUCH_AUTHENTICATOR;
import static com.onegini.OneginiCordovaPluginConstants.ERROR_NO_USER_AUTHENTICATED;

import java.util.Set;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import com.onegini.handler.AuthenticatorDeregistrationHandler;
import com.onegini.handler.AuthenticatorRegistrationHandler;
import com.onegini.handler.FingerprintAuthenticationHandler;
import com.onegini.handler.PinAuthenticationRequestHandler;
import com.onegini.mobile.sdk.android.handlers.request.callback.OneginiPinCallback;
import com.onegini.mobile.sdk.android.model.OneginiAuthenticator;
import com.onegini.mobile.sdk.android.model.entity.UserProfile;
import com.onegini.util.ActionArgumentsUtil;
import com.onegini.util.PluginResultBuilder;

public class OneginiAuthenticatorRegistrationClient extends CordovaPlugin {
  private static final String ACTION_START = "start";
  private static final String ACTION_PROVIDE_PIN = "providePin";
  private static final String ACTION_DEREGISTER = "deregister";

  private AuthenticatorRegistrationHandler authenticatorRegistrationHandler;

  @Override
  public boolean execute(final String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {
    if (ACTION_START.equals(action)) {
      startRegistration(args, callbackContext);
      return true;
    } else if (ACTION_PROVIDE_PIN.equals(action)) {
      providePin(args, callbackContext);
      return true;
    } else if (ACTION_DEREGISTER.equals(action)) {
      deregister(args, callbackContext);
      return true;
    }
    return false;
  }

  private void startRegistration(final JSONArray args, final CallbackContext callbackContext) throws JSONException {
    final UserProfile userProfile = getOneginiClient().getUserClient().getAuthenticatedUserProfile();
    final Set<OneginiAuthenticator> authenticatorSet;
    final OneginiAuthenticator authenticator;

    if (userProfile == null) {
      callbackContext.sendPluginResult(new PluginResultBuilder()
          .withErrorDescription(ERROR_NO_USER_AUTHENTICATED)
          .build());

      return;
    }

    authenticatorSet = getOneginiClient().getUserClient().getNotRegisteredAuthenticators(userProfile);
    authenticator = ActionArgumentsUtil.getAuthenticatorFromArguments(args, authenticatorSet);

    if (authenticator == null) {
      callbackContext.sendPluginResult(new PluginResultBuilder()
          .withErrorDescription(ERROR_NO_SUCH_AUTHENTICATOR)
          .build());

      return;
    }

    PinAuthenticationRequestHandler.getInstance().setStartAuthenticationCallback(callbackContext);
    authenticatorRegistrationHandler = new AuthenticatorRegistrationHandler(callbackContext);
    getOneginiClient().getUserClient().registerAuthenticator(authenticator, authenticatorRegistrationHandler);
  }

  private void providePin(final JSONArray args, final CallbackContext callbackContext) throws JSONException {
    final String pin = ActionArgumentsUtil.getPinFromArguments(args);
    final OneginiPinCallback pinCallback = PinAuthenticationRequestHandler.getInstance().getPinCallback();
    authenticatorRegistrationHandler.setCallbackContext(callbackContext);

    if (pinCallback == null) {
      callbackContext.sendPluginResult(new PluginResultBuilder()
          .withErrorDescription(OneginiCordovaPluginConstants.ERROR_PROVIDE_PIN_NO_AUTHENTICATION_IN_PROGRESS)
          .build());

      return;
    }

    pinCallback.acceptAuthenticationRequest(pin.toCharArray());
  }

  private void deregister(final JSONArray args, final CallbackContext callbackContext) throws JSONException {
    final UserProfile userProfile = getOneginiClient().getUserClient().getAuthenticatedUserProfile();
    final Set<OneginiAuthenticator> authenticatorSet;
    final OneginiAuthenticator authenticator;

    if (userProfile == null) {
      callbackContext.sendPluginResult(new PluginResultBuilder()
          .withErrorDescription(ERROR_NO_USER_AUTHENTICATED)
          .build());

      return;
    }

    authenticatorSet = getOneginiClient().getUserClient().getRegisteredAuthenticators(userProfile);
    authenticator = ActionArgumentsUtil.getAuthenticatorFromArguments(args, authenticatorSet);

    if (authenticator == null) {
      callbackContext.sendPluginResult(new PluginResultBuilder()
          .withErrorDescription(ERROR_NO_SUCH_AUTHENTICATOR)
          .build());

      return;
    }

    AuthenticatorDeregistrationHandler authenticationDeregistrationHandler = new AuthenticatorDeregistrationHandler(callbackContext);
    getOneginiClient().getUserClient().deregisterAuthenticator(authenticator, authenticationDeregistrationHandler);
  }

  private com.onegini.mobile.sdk.android.client.OneginiClient getOneginiClient() {
    return OneginiSDK.getOneginiClient(cordova.getActivity().getApplicationContext());
  }
}

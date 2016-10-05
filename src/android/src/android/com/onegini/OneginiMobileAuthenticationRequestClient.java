package com.onegini;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import com.onegini.handler.MobileAuthenticationHandler;
import com.onegini.mobileAuthentication.Callback;

public class OneginiMobileAuthenticationRequestClient extends CordovaPlugin {

  private static final String ACTION_REGISTER_CHALLENGE_RECEIVER = "registerChallengeReceiver";
  private static final String ACTION_REPLY_TO_CONFIRMATION_CHALLENGE = "replyToConfirmationChallenge";
  private static final String PARAM_METHOD = "method";
  private static final String PARAM_ACCEPT = "accept";

  @Override
  public boolean execute(final String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {
    if (ACTION_REGISTER_CHALLENGE_RECEIVER.equals(action)) {
      registerChallengeReceiver(args, callbackContext);
      return true;
    } else if (ACTION_REPLY_TO_CONFIRMATION_CHALLENGE.equals(action)) {
      replyToConfirmationChallenge(args, callbackContext);
      return true;
    }

    return false;
  }

  private void registerChallengeReceiver(final JSONArray args, final CallbackContext callbackContext) throws JSONException {
    final String methodString = args.getJSONObject(0).getString(PARAM_METHOD);
    final Callback.Method method = Callback.Method.valueOf(methodString);

    MobileAuthenticationHandler.getInstance().registerAuthenticationChallengeReceiver(method, callbackContext);
  }

  private void replyToConfirmationChallenge(final JSONArray args, final CallbackContext callbackContext) throws JSONException {
    final MobileAuthenticationHandler mobileAuthenticationHandler = MobileAuthenticationHandler.getInstance();
    final Boolean shouldAccept = args.getJSONObject(0).getBoolean(PARAM_ACCEPT);

    mobileAuthenticationHandler.replyToConfirmationChallenge(callbackContext, shouldAccept);
  }
}

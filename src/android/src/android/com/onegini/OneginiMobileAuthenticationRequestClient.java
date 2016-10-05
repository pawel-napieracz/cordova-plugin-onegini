package com.onegini;

import static com.onegini.OneginiCordovaPluginConstants.ERROR_INVALID_MOBILE_AUTHENTICATOR_METHOD;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import com.onegini.handler.MobileAuthenticationHandler;
import com.onegini.mobileAuthentication.Callback;
import com.onegini.util.ActionArgumentsUtil;
import com.onegini.util.PluginResultBuilder;

public class OneginiMobileAuthenticationRequestClient extends CordovaPlugin {

  private static final String ACTION_REGISTER_CHALLENGE_RECEIVER = "registerChallengeReceiver";
  private static final String ACTION_REPLY_TO_CHALLENGE = "replyToChallenge";
  private static final String PARAM_ACCEPT = "accept";

  @Override
  public boolean execute(final String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {
    if (ACTION_REGISTER_CHALLENGE_RECEIVER.equals(action)) {
      registerChallengeReceiver(args, callbackContext);
      return true;
    } else if (ACTION_REPLY_TO_CHALLENGE.equals(action)) {
      replyToChallenge(args, callbackContext);
      return true;
    }

    return false;
  }

  private void registerChallengeReceiver(final JSONArray args, final CallbackContext callbackContext) throws JSONException {
    final Callback.Method method = ActionArgumentsUtil.getCallbackMethodFromArguments(args);

    cordova.getThreadPool().execute(new Runnable() {
      @Override
      public void run() {
        MobileAuthenticationHandler.getInstance().registerAuthenticationChallengeReceiver(method, callbackContext);
      }
    });
  }

  private void replyToConfirmationChallenge(final JSONArray args, final CallbackContext callbackContext) throws JSONException {
    final Boolean shouldAccept = args.getJSONObject(0).getBoolean(PARAM_ACCEPT);

    cordova.getThreadPool().execute(new Runnable() {
      @Override
      public void run() {
        MobileAuthenticationHandler.getInstance().replyToConfirmationChallenge(callbackContext, shouldAccept);
      }
    });
  }

  private void replyToPinChallenge(final JSONArray args, final CallbackContext callbackContext) throws JSONException {
    final boolean shouldAccept = args.getJSONObject(0).getBoolean(PARAM_ACCEPT);
    final char[] pin;

    if (shouldAccept) {
      pin = ActionArgumentsUtil.getPinFromArguments(args).toCharArray();
    } else {
      pin = null;
    }

    cordova.getThreadPool().execute(new Runnable() {
      @Override
      public void run() {
        MobileAuthenticationHandler.getInstance().replyToPinChallenge(callbackContext, shouldAccept, pin);
      }
    });
  }

  private void replyToChallenge(final JSONArray args, final CallbackContext callbackContext) throws JSONException {
    final Callback.Method method = ActionArgumentsUtil.getCallbackMethodFromArguments(args);

    switch (method) {
      case CONFIRMATION:
        replyToConfirmationChallenge(args, callbackContext);
        break;
      case PIN:
        replyToPinChallenge(args, callbackContext);
        break;
      default:
        callbackContext.sendPluginResult(new PluginResultBuilder()
            .withErrorDescription(ERROR_INVALID_MOBILE_AUTHENTICATOR_METHOD)
            .build());
        break;
    }
  }
}

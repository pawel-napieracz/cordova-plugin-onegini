package com.onegini;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import com.onegini.handler.MobileAuthenticationHandler;
import com.onegini.mobileAuthentication.Callback;
import com.onegini.mobileAuthentication.ConfirmationCallback;
import com.onegini.mobileAuthentication.FingerprintCallback;
import com.onegini.mobileAuthentication.PinCallback;
import com.onegini.util.ActionArgumentsUtil;

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

  private void replyToChallenge(final JSONArray args, final CallbackContext callbackContext) throws JSONException {
    final Callback callback = MobileAuthenticationHandler.getInstance().getCurrentCallback();
    final boolean shouldAccept = args.getJSONObject(0).getBoolean(PARAM_ACCEPT);

    if (callback == null) {
      return;
    }

    callback.setChallengeResponseCallbackContext(callbackContext);

    if (callback instanceof ConfirmationCallback) {
      final ConfirmationCallback confirmationCallback = (ConfirmationCallback) callback;
      replyToConfirmationChallenge(confirmationCallback, shouldAccept);
    } else if (callback instanceof PinCallback) {
      final PinCallback pinCallback = (PinCallback) callback;
      replyToPinChallenge(pinCallback, args, shouldAccept);
    } else if (callback instanceof FingerprintCallback) {
      final FingerprintCallback fingerprintCallback = (FingerprintCallback) callback;
      replyToFingerprintChallenge(fingerprintCallback, shouldAccept);
    }
  }

  private void replyToConfirmationChallenge(final ConfirmationCallback confirmationCallback, final boolean shouldAccept) throws JSONException {
    cordova.getThreadPool().execute(new Runnable() {
      @Override
      public void run() {
        if (shouldAccept) {
          confirmationCallback.getAcceptDenyCallback().acceptAuthenticationRequest();
        } else {
          confirmationCallback.getAcceptDenyCallback().denyAuthenticationRequest();
        }
      }
    });
  }

  private void replyToPinChallenge(final PinCallback pinCallback, final JSONArray args, final boolean shouldAccept) throws JSONException {
    final char[] pin;

    if (shouldAccept) {
      pin = ActionArgumentsUtil.getPinFromArguments(args).toCharArray();
    } else {
      pin = null;
    }

    cordova.getThreadPool().execute(new Runnable() {
      @Override
      public void run() {
        if (shouldAccept) {
          pinCallback.getPinCallback().acceptAuthenticationRequest(pin);
        } else {
          pinCallback.getPinCallback().denyAuthenticationRequest();
        }
      }
    });
  }

  private void replyToFingerprintChallenge(final FingerprintCallback fingerprintCallback, final boolean shouldAccept) throws JSONException {
    cordova.getThreadPool().execute(new Runnable() {
      @Override
      public void run() {
        if (shouldAccept) {
          fingerprintCallback.getFingerprintCallback().acceptAuthenticationRequest();
        } else {
          fingerprintCallback.getFingerprintCallback().denyAuthenticationRequest();
        }
      }
    });
  }
}

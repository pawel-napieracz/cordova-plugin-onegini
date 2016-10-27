package com.onegini;

import static com.onegini.OneginiCordovaPluginConstants.ERROR_CREATE_PIN_NO_REGISTRATION_IN_PROGRESS;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

import com.onegini.handler.ChangePinHandler;
import com.onegini.handler.CreatePinRequestHandler;
import com.onegini.handler.PinAuthenticationRequestHandler;
import com.onegini.mobile.sdk.android.client.OneginiClient;
import com.onegini.mobile.sdk.android.handlers.request.callback.OneginiPinCallback;
import com.onegini.util.ActionArgumentsUtil;
import com.onegini.util.PluginResultBuilder;

public class OneginiChangePinClient extends CordovaPlugin {

  private final static String ACTION_START = "start";
  private final static String ACTION_CREATE_PIN = "createPin";
  private ChangePinHandler changePinHandler;

  @Override
  public boolean execute(final String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {
    if (ACTION_START.equals(action)) {
      startChangePin(args, callbackContext);
      return true;
    } else if (ACTION_CREATE_PIN.equals(action)) {
      createPin(args, callbackContext);
      return true;
    }

    return false;
  }

  private void startChangePin(final JSONArray args, final CallbackContext callbackContext) throws JSONException {
    final String pin = ActionArgumentsUtil.getPinFromArguments(args);

//    PinAuthenticationRequestHandler.getInstance().setOnNextAuthenticationAttemptCallback(callbackContext);
    PinAuthenticationRequestHandler.getInstance().setFinishAuthenticationCallback(callbackContext);
    this.changePinHandler = new ChangePinHandler(callbackContext);
    
    cordova.getThreadPool().execute(new Runnable() {
      @Override
      public void run() {
        getOneginiClient().getUserClient().changePin(changePinHandler);
        PinAuthenticationRequestHandler.getInstance().getPinCallback()
            .acceptAuthenticationRequest(pin.toCharArray());
      }
    });
  }

  private void createPin(final JSONArray args, final CallbackContext callbackContext) throws JSONException {
    final String pin = ActionArgumentsUtil.getPinFromArguments(args);
    final OneginiPinCallback pinCallback = CreatePinRequestHandler.getInstance().getOneginiPinCallback();
    CreatePinRequestHandler.getInstance().setOnNextPinCreationAttemptCallback(callbackContext);

    if (pinCallback == null) {
      final PluginResult pluginResult = new PluginResultBuilder()
          .withError()
          .withErrorDescription(ERROR_CREATE_PIN_NO_REGISTRATION_IN_PROGRESS)
          .build();
      callbackContext.sendPluginResult(pluginResult);
    } else {
      changePinHandler.setCallbackContext(callbackContext);
      pinCallback.acceptAuthenticationRequest(pin.toCharArray());
    }

  }

  private OneginiClient getOneginiClient() {
    return OneginiSDK.getOneginiClient(cordova.getActivity().getApplicationContext());
  }
}

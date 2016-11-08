package com.onegini;

import static com.onegini.OneginiCordovaPluginConstants.ERROR_CREATE_PIN_NO_REGISTRATION_IN_PROGRESS;
import static com.onegini.OneginiCordovaPluginConstants.ERROR_PROVIDE_PIN_NO_AUTHENTICATION_IN_PROGRESS;

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
  private final static String ACTION_PROVIDE_PIN = "providePin";

  @Override
  public boolean execute(final String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {
    if (ACTION_START.equals(action)) {
      startChangePin(callbackContext);
      return true;
    } else if (ACTION_CREATE_PIN.equals(action)) {
      createPin(args, callbackContext);
      return true;
    } else if (ACTION_PROVIDE_PIN.equals(action)) {
      providePin(args, callbackContext);
      return true;
    }

    return false;
  }

  private void startChangePin(final CallbackContext callbackContext) throws JSONException {
    PinAuthenticationRequestHandler.getInstance().setStartAuthenticationCallbackContext(callbackContext);
    CreatePinRequestHandler.getInstance().setOnStartPinCreationCallback(callbackContext);

    cordova.getThreadPool().execute(new Runnable() {
      @Override
      public void run() {
        final ChangePinHandler changePinHandler = new ChangePinHandler(callbackContext);
        getOneginiClient().getUserClient().changePin(changePinHandler);
      }
    });
  }

  private void providePin(final JSONArray args, final CallbackContext callbackContext) throws JSONException {
    final String pin = ActionArgumentsUtil.getPinFromArguments(args);
    final OneginiPinCallback pinCallback = PinAuthenticationRequestHandler.getInstance().getPinCallback();

    if (pinCallback == null) {
      callbackContext.sendPluginResult(new PluginResultBuilder()
          .withErrorDescription(ERROR_PROVIDE_PIN_NO_AUTHENTICATION_IN_PROGRESS)
          .build());

      return;
    }

    pinCallback.acceptAuthenticationRequest(pin.toCharArray());
  }

  private void createPin(final JSONArray args, final CallbackContext callbackContext) throws JSONException {
    final String pin = ActionArgumentsUtil.getPinFromArguments(args);
    final OneginiPinCallback pinCallback = CreatePinRequestHandler.getInstance().getOneginiPinCallback();

    if (pinCallback == null) {
      final PluginResult pluginResult = new PluginResultBuilder()
          .withError()
          .withErrorDescription(ERROR_CREATE_PIN_NO_REGISTRATION_IN_PROGRESS)
          .build();
      callbackContext.sendPluginResult(pluginResult);
    } else {
      pinCallback.acceptAuthenticationRequest(pin.toCharArray());
    }

  }

  private OneginiClient getOneginiClient() {
    return OneginiSDK.getInstance().getOneginiClient(cordova.getActivity().getApplicationContext());
  }
}

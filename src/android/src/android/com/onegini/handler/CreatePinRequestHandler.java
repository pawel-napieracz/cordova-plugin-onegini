package com.onegini.handler;

import org.apache.cordova.CallbackContext;

import android.util.Log;
import com.onegini.mobile.android.sdk.handlers.error.OneginiPinValidationError;
import com.onegini.mobile.android.sdk.handlers.request.OneginiCreatePinRequestHandler;
import com.onegini.mobile.android.sdk.handlers.request.callback.OneginiPinCallback;
import com.onegini.mobile.android.sdk.model.entity.UserProfile;

public class CreatePinRequestHandler implements OneginiCreatePinRequestHandler {

  private CallbackContext callbackContext;

  public CallbackContext getCallbackContext() {
    return callbackContext;
  }

  public void setCallbackContext(final CallbackContext callbackContext) {
    this.callbackContext = callbackContext;
  }

  @Override
  public void startPinCreation(final UserProfile userProfile, final OneginiPinCallback oneginiPinCallback) {
    Log.v("OneginiCordovaPlugin", "startPinCreation");
  }

  @Override
  public void onNextPinCreationAttempt(final OneginiPinValidationError oneginiPinValidationError) {
    Log.v("OneginiCordovaPlugin", "onNextPinCreationAttempt");
  }

  @Override
  public void finishPinCreation() {
    Log.v("OneginiCordovaPlugin", "finishPinCreation");
  }
}

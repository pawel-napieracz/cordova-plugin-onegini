package com.onegini.handler;

import java.util.HashMap;
import java.util.Map;

import org.apache.cordova.CallbackContext;
import org.json.JSONObject;

import com.onegini.mobile.sdk.android.handlers.OneginiRegistrationHandler;
import com.onegini.mobile.sdk.android.handlers.error.OneginiRegistrationError;
import com.onegini.mobile.sdk.android.model.entity.UserProfile;

public class RegistrationHandler implements OneginiRegistrationHandler {

  private CallbackContext callbackContext;

  public RegistrationHandler(CallbackContext callbackContext) {
    this.callbackContext = callbackContext;
  }

  //TODO Rework to use new builder util
  @Override
  public void onSuccess(final UserProfile userProfile) {
    final Map<String, Object> payloadMap = new HashMap<String, Object>();
    payloadMap.put("profileId", userProfile.getProfileId());
    payloadMap.put("pinLength", 5);
    final JSONObject payload = new JSONObject(payloadMap);
    callbackContext.success(payload);
  }

  //TODO Rework to use new builder util
  @Override
  public void onError(final OneginiRegistrationError oneginiRegistrationError) {
    final Map<String, Object> errorMap = new HashMap<String, Object>();
    errorMap.put("type", oneginiRegistrationError.getErrorType());
    errorMap.put("description", oneginiRegistrationError.getErrorDescription());
    final JSONObject payload = new JSONObject(errorMap);
    callbackContext.error(payload);
  }
}

package com.onegini.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONObject;

import com.onegini.mobile.android.sdk.handlers.OneginiInitializationHandler;
import com.onegini.mobile.android.sdk.handlers.error.OneginiInitializationError;
import com.onegini.mobile.android.sdk.model.entity.UserProfile;

public class InitializationHandler implements OneginiInitializationHandler {

  private final CallbackContext callbackContext;

  public InitializationHandler(final CallbackContext callbackContext) {
    this.callbackContext = callbackContext;
  }

  @Override
  public void onSuccess(final Set<UserProfile> userProfiles) {
    final JSONArray payload = new JSONArray();

    for (final UserProfile userProfile : userProfiles) {
      final Map<String, Object> userProfileMap = new HashMap<String, Object>();
      userProfileMap.put("id", userProfile.getProfileId());
      userProfileMap.put("isDefault", userProfile.isDefault());
      payload.put(new JSONObject(userProfileMap));
    }

    callbackContext.success(payload);
  }

  @Override
  public void onError(final OneginiInitializationError oneginiInitializationError) {
    final Map<String, Object> payload = new HashMap<String, Object>();
    payload.put("type", oneginiInitializationError.getErrorType());
    payload.put("description", oneginiInitializationError.getErrorDescription());

    callbackContext.error(new JSONObject(payload));
  }
}

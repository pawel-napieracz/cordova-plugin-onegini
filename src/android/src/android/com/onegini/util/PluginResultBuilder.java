package com.onegini.util;

import static org.apache.cordova.PluginResult.Status.ERROR;
import static org.apache.cordova.PluginResult.Status.OK;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.onegini.mobile.sdk.android.model.entity.UserProfile;

public class PluginResultBuilder {

  private JSONObject payload;
  private PluginResult.Status status;
  private boolean shouldKeepCallback;

  public PluginResultBuilder() {
    payload = new JSONObject();
    shouldKeepCallback = false;
  }

  public PluginResultBuilder withSuccess() {
    status = OK;
    return this;
  }

  public PluginResultBuilder withError() {
    status = ERROR;
    return this;
  }

  public PluginResultBuilder withErrorDescription(final String description) {
    status = ERROR;

    try {
      payload.put("description", description);
    } catch (JSONException e) {
      handleException(e);
    }

    return this;
  }

  public PluginResultBuilder shouldKeepCallback() {
    this.shouldKeepCallback = true;
    return this;
  }

  public PluginResultBuilder withErrorType(final int errorType) {
    status = ERROR;

    try {
      payload.put("errorType", errorType);
    } catch (JSONException e) {
      handleException(e);
    }

    return this;
  }

  public PluginResultBuilder withRemainingFailureCount(int remainingFailureCount) {
    try {
      payload.put("remainingFailureCount", remainingFailureCount);
    } catch (JSONException e) {
      handleException(e);
    }

    return this;
  }

  public PluginResultBuilder withMaxFailureCount(int maxFailureCount) {
    try {
      payload.put("maxFailureCount", maxFailureCount);
    } catch (JSONException e) {
      handleException(e);
    }

    return this;
  }

  public PluginResultBuilder withPinLength(int pinLength) {
    try {
      payload.put("pinLength", pinLength);
    } catch (JSONException e) {
      handleException(e);
    }

    return this;
  }

  public PluginResultBuilder addUserProfile(final UserProfile userProfile) {
    try {
      JSONArray userProfilesJSON;
      if (payload.has("userProfiles")) {
        userProfilesJSON = payload.getJSONArray("userProfiles");
      } else {
        userProfilesJSON = new JSONArray();
      }

      JSONObject userProfileJSON = new JSONObject();
      userProfileJSON.put("profileId", userProfile.getProfileId());

      userProfilesJSON.put(userProfileJSON);
      payload.put("userProfiles", userProfilesJSON);
    } catch (JSONException e) {
      handleException(e);
    }

    return this;
  }

  public PluginResultBuilder addUserProfiles(final Set<UserProfile> userProfiles) {
    for (final UserProfile userProfile : userProfiles) {
      this.addUserProfile(userProfile);
    }

    return this;
  }

  private void handleException(JSONException e) {
    this.status = ERROR;

    Map<String, String> payload = new HashMap<String, String>();
    payload.put("description", "OneginiPlugin: Internal error: " + e.getMessage());
    this.payload = new JSONObject(payload);
  }

  public PluginResult build() {
    PluginResult pluginResult = new PluginResult(status, payload);
    pluginResult.setKeepCallback(shouldKeepCallback);
    return pluginResult;
  }
}

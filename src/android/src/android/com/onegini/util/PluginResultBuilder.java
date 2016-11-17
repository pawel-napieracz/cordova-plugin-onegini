package com.onegini.util;

import static com.onegini.OneginiCordovaPluginConstants.ERROR_CODE_PLUGIN_INTERNAL_ERROR;
import static com.onegini.OneginiCordovaPluginConstants.ERROR_DESCRIPTION_PLUGIN_INTERNAL_ERROR;
import static org.apache.cordova.PluginResult.Status.ERROR;
import static org.apache.cordova.PluginResult.Status.OK;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import org.apache.cordova.PluginResult;
import org.json.JSONException;
import org.json.JSONObject;

import com.onegini.mobile.sdk.android.handlers.error.OneginiError;
import com.onegini.mobile.sdk.android.model.OneginiClientConfigModel;
import com.onegini.mobile.sdk.android.model.entity.OneginiMobileAuthenticationRequest;
import com.onegini.mobile.sdk.android.model.entity.UserProfile;
import retrofit.client.Response;

public class PluginResultBuilder {

  private JSONObject payload;
  private PluginResult.Status status;
  private Boolean shouldKeepCallback = false;

  public PluginResultBuilder() {
    payload = new JSONObject();
  }

  public PluginResultBuilder withSuccess() {
    status = OK;
    return this;
  }

  public PluginResultBuilder withError() {
    status = ERROR;
    return this;
  }

  public PluginResultBuilder shouldKeepCallback() {
    this.shouldKeepCallback = true;

    return this;
  }

  public PluginResultBuilder withPluginError(final String description, final int code) {
    status = ERROR;

    try {
      payload.put("description", description);
      payload.put("code", code);
    } catch (JSONException e) {
      handleException(e);
    }

    return this;
  }

  public PluginResultBuilder withOneginiError(final OneginiError oneginiError) {
    status = ERROR;

    if (oneginiError == null) {
      return this;
    }

    try {
      payload.put("code", oneginiError.getErrorType());
      payload.put("description", oneginiError.getErrorDescription());
    } catch (JSONException e) {
      handleException(e);
    }

    return this;
  }

  public PluginResultBuilder withOneginiMobileAuthenticationRequest(final OneginiMobileAuthenticationRequest mobileAuthenticationRequest) {
    try {
      payload.put("type", mobileAuthenticationRequest.getType());
      payload.put("message", mobileAuthenticationRequest.getMessage());
      payload.put("profileId", mobileAuthenticationRequest.getUserProfile().getProfileId());
    } catch (JSONException e) {
      handleException(e);
    }

    return this;
  }

  public PluginResultBuilder withMobileAuthenticationEvent(final String eventName) {
    try {
      payload.put("mobileAuthenticationEvent", eventName);
    } catch (JSONException e) {
      handleException(e);
    }

    return this;
  }

  public PluginResultBuilder withAuthenticationEvent(final String eventName) {
    try {
      payload.put("authenticationEvent", eventName);
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

  public PluginResultBuilder withProfileId(UserProfile userProfile) {
    try {
      payload.put("profileId", userProfile.getProfileId());
    } catch (JSONException e) {
      handleException(e);
    }

    return this;
  }

  public PluginResultBuilder withRetrofitResponse(Response response) {
    final int responseStatus = response.getStatus();
    if (responseStatus == HttpURLConnection.HTTP_OK) {
      this.status = OK;
    } else {
      this.status = ERROR;
    }

    try {
      payload.put("body", RetrofitResponseUtil.getBodyStringFromRetrofitResponse(response));
      payload.put("status", responseStatus);
      payload.put("statusText", response.getReason());
      payload.put("headers", RetrofitResponseUtil.getJsonHeadersFromRetrofitResponse(response));
    } catch (JSONException e) {
      handleException(e);
    }

    return this;
  }

  public PluginResultBuilder withOneginiConfigModel(final OneginiClientConfigModel configModel) {
    try {
      payload.put("resourceBaseURL", configModel.getResourceBaseUrl());
    } catch (JSONException e) {
      handleException(e);
    }

    return this;
  }

  private void handleException(JSONException e) {
    this.status = ERROR;

    Map<String, Object> payload = new HashMap<String, Object>();
    payload.put("description", ERROR_DESCRIPTION_PLUGIN_INTERNAL_ERROR + " : " + e.getMessage());
    payload.put("code",ERROR_CODE_PLUGIN_INTERNAL_ERROR);
    this.payload = new JSONObject(payload);
  }

  public PluginResult build() {
    PluginResult pluginResult;

    if (payload.length() == 0) {
      pluginResult = new PluginResult(status);
    } else {
      pluginResult = new PluginResult(status, payload);
    }

    pluginResult.setKeepCallback(shouldKeepCallback);

    return pluginResult;
  }
}

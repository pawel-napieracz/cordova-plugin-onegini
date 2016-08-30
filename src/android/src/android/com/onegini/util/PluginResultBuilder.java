package com.onegini.util;

import static org.apache.cordova.PluginResult.Status.ERROR;
import static org.apache.cordova.PluginResult.Status.OK;

import java.util.HashMap;

import org.apache.cordova.PluginResult;
import org.json.JSONObject;

public class PluginResultBuilder {

  private HashMap<String, String> payload;
  private PluginResult.Status status;
  private boolean shouldKeepCallback = false;

  public PluginResultBuilder() {
    payload = new HashMap<String, String>();
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
    payload.put("description", description);
    return this;
  }

  public PluginResultBuilder shouldKeepCallback() {
    this.shouldKeepCallback = true;
    return this;
  }

  public PluginResultBuilder withErrorType(final int errorType) {
    status = ERROR;
    payload.put("errorType", Integer.toString(errorType));
    return this;
  }

  public PluginResult build() {
    PluginResult pluginResult = new PluginResult(status, new JSONObject(payload));
    pluginResult.setKeepCallback(shouldKeepCallback);
    return pluginResult;
  }
}

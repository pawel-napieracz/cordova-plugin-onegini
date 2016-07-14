package com.onegini.model;

import org.apache.cordova.CordovaPreferences;

import android.os.Build;
import com.onegini.exception.PluginConfigException;
import com.onegini.mobile.sdk.android.library.model.OneginiClientConfigModel;

public class OneginiCordovaPluginConfigModel {

  public static OneginiCordovaPluginConfigModel from(final CordovaPreferences preferences) throws PluginConfigException {
    final OneginiCordovaPluginConfigModel config = new OneginiCordovaPluginConfigModel();

    config.gcmSenderId = preferences.getString("OneginiGcmSenderId", null);
    if (config.gcmSenderId == null) {
      config.gcmSenderId = preferences.getString("kOGgcmSenderId", null);
    }
    if (config.gcmSenderId == null) {
      throw new PluginConfigException("Missing property in config.xml file: OneginiGcmSenderId");
    }
    config.useNativePinScreen = preferences.getBoolean("kOGUseNativePinScreen", false) || preferences.getBoolean("OneginiNativeScreens", false);
    config.useEmbeddedWebview = true;

    return config;
  }

  private String gcmSenderId;
  private boolean useEmbeddedWebview;
  private boolean useNativePinScreen;

  public boolean useEmbeddedWebview() {
    return useEmbeddedWebview;
  }

  public String getGcmSenderId() {
    return gcmSenderId;
  }

  public boolean useNativePinScreen() {
    return useNativePinScreen;
  }
}

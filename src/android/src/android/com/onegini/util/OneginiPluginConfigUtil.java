package com.onegini.util;

import org.apache.cordova.ConfigXmlParser;

import android.content.Context;
import com.onegini.exception.PluginConfigException;
import com.onegini.model.OneginiCordovaPluginConfigModel;

public class OneginiPluginConfigUtil {

  public static OneginiCordovaPluginConfigModel retrievePluginConfig(final Context context) throws PluginConfigException {
    final ConfigXmlParser configXmlParser = new ConfigXmlParser();
    configXmlParser.parse(context);
    return OneginiCordovaPluginConfigModel.from(configXmlParser.getPreferences());
  }
}

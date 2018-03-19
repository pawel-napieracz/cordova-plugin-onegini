package com.onegini.mobile.sdk.cordova.util;


import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.util.Log;
import com.onegini.mobile.sdk.cordova.customregistration.CustomIdentityProvider;

public class ApplicationConfigurationParser {

  private static final String TAG = ApplicationConfigurationParser.class.getSimpleName();

  private static final String CONFIG_FILE_NAME = "config";
  private static final String CONFIG_FILE_EXTENSION = "xml";

  private static final String CUSTOM_IDP = "custom-idp";
  private static final String CUSTOM_IDP_ID = "id";
  private static final String CUSTOM_IDP_FLOW_TYPE = "flowType";

  private final Context context;

  public ApplicationConfigurationParser(final Context context) {
    this.context = context;
  }

  public Set<CustomIdentityProvider> parserCustomIdentityProviders() {
    final Set<CustomIdentityProvider> identityProviders = new HashSet<CustomIdentityProvider>();

    final XmlResourceParser xmlResourceParser = getXmlParser();
    try {
      int eventType = xmlResourceParser.getEventType();
      while (eventType != XmlResourceParser.END_DOCUMENT) {
        if (XmlResourceParser.START_TAG == eventType) {
          if (CUSTOM_IDP.equals(xmlResourceParser.getName())) {
            final String id = xmlResourceParser.getAttributeValue(null, CUSTOM_IDP_ID);
            final String flowType = xmlResourceParser.getAttributeValue(null, CUSTOM_IDP_FLOW_TYPE);
            identityProviders.add(new CustomIdentityProvider(id, flowType));
          }
        }
        eventType = xmlResourceParser.next();
      }
    } catch (XmlPullParserException e) {
      Log.w(TAG, "Parsing the list of custom identity providers has failed: " + e.getMessage());
    } catch (IOException e) {
      Log.w(TAG, "Parsing the list of custom identity providers has failed: " + e.getMessage());
    } finally {
      xmlResourceParser.close();
    }
    return identityProviders;
  }

  private XmlResourceParser getXmlParser() {
    final int configResourceId = context.getResources().getIdentifier(CONFIG_FILE_NAME, CONFIG_FILE_EXTENSION, context.getPackageName());
    return context.getResources().getXml(configResourceId);
  }
}

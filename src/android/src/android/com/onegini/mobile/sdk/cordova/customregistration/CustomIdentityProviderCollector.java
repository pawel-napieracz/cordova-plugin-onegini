package com.onegini.mobile.sdk.cordova.customregistration;


import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.support.annotation.Nullable;
import com.onegini.mobile.sdk.cordova.util.ApplicationConfigurationParser;

public class CustomIdentityProviderCollector {

  private static final Set<CustomIdentityProvider> customIdentityProviders = new HashSet<CustomIdentityProvider>();

  private CustomIdentityProviderCollector() {
  }

  public static Set getAll() {
    return customIdentityProviders;
  }

  @Nullable
  public static CustomIdentityProvider get(final String id) {
    for (final CustomIdentityProvider identityProvider : customIdentityProviders) {
      if (id.equals(identityProvider.getId())) {
        return identityProvider;
      }
    }
    return null;
  }

  public static void collectFromConfigXml(final Context context) {
    customIdentityProviders.clear();
    customIdentityProviders.addAll(new ApplicationConfigurationParser(context).parserCustomIdentityProviders());
  }
}

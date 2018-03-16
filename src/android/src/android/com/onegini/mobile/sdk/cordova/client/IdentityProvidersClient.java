package com.onegini.mobile.sdk.cordova.client;


import static com.onegini.mobile.sdk.cordova.OneginiCordovaPluginConstants.ERROR_CODE_PLUGIN_INTERNAL_ERROR;
import static com.onegini.mobile.sdk.cordova.OneginiCordovaPluginConstants.ERROR_DESCRIPTION_PLUGIN_INTERNAL_ERROR;

import java.util.Set;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import com.onegini.mobile.sdk.android.model.OneginiIdentityProvider;
import com.onegini.mobile.sdk.cordova.OneginiSDK;
import com.onegini.mobile.sdk.cordova.util.IdentityProvidersUtil;
import com.onegini.mobile.sdk.cordova.util.PluginResultBuilder;

@SuppressWarnings("unused")
public class IdentityProvidersClient extends CordovaPlugin {

  private static final String ACTION_GET_IDENTITY_PROVIDERS = "getIdentityProviders";

  private static Set<OneginiIdentityProvider> cachedIdentityProviders;

  @Override
  public boolean execute(final String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {
    if (ACTION_GET_IDENTITY_PROVIDERS.equals(action)) {
      getIdentityProviders(callbackContext);
      return true;
    }
    return false;
  }

  public static Set<OneginiIdentityProvider> getIdentityProviders(final com.onegini.mobile.sdk.android.client.OneginiClient oneginiClient) {
    if (cachedIdentityProviders == null) {
      cachedIdentityProviders = oneginiClient.getUserClient().getIdentityProviders();
    }
    return cachedIdentityProviders;
  }

  private void getIdentityProviders(final CallbackContext callbackContext) {
    cordova.getThreadPool().execute(new Runnable() {
      @Override
      public void run() {
        cachedIdentityProviders = getOneginiClient().getUserClient().getIdentityProviders();
        try {
          final JSONArray parsedIdentityProviders = IdentityProvidersUtil.identityProvidersSetToJsonArray(cachedIdentityProviders);
          callbackContext.success(parsedIdentityProviders);
        } catch (JSONException e) {
          callbackContext.sendPluginResult(new PluginResultBuilder()
              .withPluginError(ERROR_DESCRIPTION_PLUGIN_INTERNAL_ERROR, ERROR_CODE_PLUGIN_INTERNAL_ERROR)
              .build());
        }
      }
    });
  }

  private com.onegini.mobile.sdk.android.client.OneginiClient getOneginiClient() {
    return OneginiSDK.getInstance().getOneginiClient(cordova.getActivity().getApplicationContext());
  }
}

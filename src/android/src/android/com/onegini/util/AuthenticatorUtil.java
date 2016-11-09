package com.onegini.util;

import static com.onegini.OneginiCordovaPluginConstants.PARAM_AUTHENTICATOR_ID;

import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.onegini.mobile.sdk.android.model.OneginiAuthenticator;

public class AuthenticatorUtil {

  public static JSONArray authenticatorSetToJSONArray(final Set<OneginiAuthenticator> authenticatorSet) throws JSONException {
    JSONArray authenticatorJSONArray= new JSONArray();
    for (final OneginiAuthenticator authenticator: authenticatorSet) {
      //TODO Switch to actual ID when getId method is available in Onegini SDK.
      final JSONObject authenticatorJSON = authenticatorToJSONObject(authenticator);
      authenticatorJSONArray.put(authenticatorJSON);
    }

    return authenticatorJSONArray;
  }

  public static JSONObject authenticatorToJSONObject(final OneginiAuthenticator authenticator) throws JSONException {
    final JSONObject authenticatorJSON = new JSONObject();
    authenticatorJSON.put(PARAM_AUTHENTICATOR_ID, "com.onegini.authenticator." + authenticator.getName());
    return authenticatorJSON;
  }
}

package com.onegini.util;

import static com.onegini.OneginiCordovaPluginConstants.PARAM_AUTHENTICATOR_ID;

import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.onegini.mobile.sdk.android.model.OneginiAuthenticator;

public class AuthenticatorUtil {

  public static JSONArray AuthenticatorSetToJSONArray(final Set<OneginiAuthenticator> authenticatorSet) throws JSONException {
    JSONArray authenticatorJSONArray= new JSONArray();
    for (final OneginiAuthenticator authenticator: authenticatorSet) {
      final JSONObject authenticatorJSON = new JSONObject();
      //TODO Swicht to actual ID when getId method becames available in Onegini SDK.
      authenticatorJSON.put(PARAM_AUTHENTICATOR_ID, "com.onegini.authenticator." + authenticator.getName());
      authenticatorJSONArray.put(authenticatorJSON);
    }

    return authenticatorJSONArray;
  }
}

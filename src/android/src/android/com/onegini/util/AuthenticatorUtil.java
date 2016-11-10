package com.onegini.util;

import static com.onegini.OneginiCordovaPluginConstants.PARAM_AUTHENTICATOR_ID;
import static com.onegini.OneginiCordovaPluginConstants.PARAM_AUTHENTICATOR_TYPE;

import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.onegini.mobile.sdk.android.model.OneginiAuthenticator;

public class AuthenticatorUtil {

  private final static String AUTHENTICATOR_TYPE_PIN = "PIN";
  private final static String AUTHENTICATOR_TYPE_FINGERPRINT = "Fingerprint";

  public static JSONArray authenticatorSetToJSONArray(final Set<OneginiAuthenticator> authenticatorSet) throws JSONException {
    JSONArray authenticatorJSONArray = new JSONArray();
    for (final OneginiAuthenticator authenticator : authenticatorSet) {
      final JSONObject authenticatorJSON = authenticatorToJSONObject(authenticator);
      authenticatorJSONArray.put(authenticatorJSON);
    }

    return authenticatorJSONArray;
  }

  public static JSONObject authenticatorToJSONObject(final OneginiAuthenticator authenticator) throws JSONException {
    final JSONObject authenticatorJSON = new JSONObject();
    authenticatorJSON.put(PARAM_AUTHENTICATOR_TYPE, authenticatorTypeToString(authenticator.getType()));
    // TODO: Change getName() to getId() when it becomes available (e.g. for FIDO or custom authenticators)
    authenticatorJSON.put(PARAM_AUTHENTICATOR_ID, authenticator.getName());
    return authenticatorJSON;
  }

  public static String authenticatorTypeToString(final int authenticatorType) {
    switch (authenticatorType) {
      case 0:
        return AUTHENTICATOR_TYPE_PIN;
      case 1:
        return AUTHENTICATOR_TYPE_FINGERPRINT;
    }
    return null;
  }
}

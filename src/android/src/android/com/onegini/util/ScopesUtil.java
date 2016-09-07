package com.onegini.util;

import static com.onegini.OneginiCordovaPluginConstants.PARAM_SCOPES;

import org.json.JSONArray;
import org.json.JSONException;

public class ScopesUtil {
  public static String[] getScopesFromActionArguments(final JSONArray args) throws JSONException {
    final String[] scopesArray;
    final JSONArray scopesJSON;

    try {
      scopesJSON = args.getJSONObject(0).getJSONArray(PARAM_SCOPES);
    } catch (JSONException e) {
      return new String[0];
    }

    scopesArray = new String[scopesJSON.length()];
    for (int i = 0; i < scopesJSON.length(); i++) {
      scopesArray[i] = scopesJSON.getString(i);
    }

    return scopesArray;
  }

}

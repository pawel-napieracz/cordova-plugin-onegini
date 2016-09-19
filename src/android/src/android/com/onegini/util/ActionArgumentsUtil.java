package com.onegini.util;

import static com.onegini.OneginiCordovaPluginConstants.ERROR_METHOD_REQUIRES_REQUEST_BODY;
import static com.onegini.OneginiCordovaPluginConstants.PARAM_BODY;
import static com.onegini.OneginiCordovaPluginConstants.PARAM_HEADERS;
import static com.onegini.OneginiCordovaPluginConstants.PARAM_METHOD;
import static com.onegini.OneginiCordovaPluginConstants.PARAM_PIN;
import static com.onegini.OneginiCordovaPluginConstants.PARAM_SCOPES;
import static com.onegini.OneginiCordovaPluginConstants.PARAM_URL;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.support.annotation.Nullable;
import com.squareup.okhttp.internal.http.HttpMethod;
import retrofit.client.Header;
import retrofit.client.Request;
import retrofit.mime.TypedString;

public class ActionArgumentsUtil {
  public static String[] getScopesFromArguments(final JSONArray args) throws JSONException {
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

  public static String getPinFromArguments(final JSONArray args) throws JSONException {
    return args.getJSONObject(0).getString(PARAM_PIN);
  }

  public static boolean isFetchAnonymous(final JSONArray args) throws JSONException {
    return args.getJSONObject(0).getBoolean("anonymous");
  }

  public static Request getRequestFromArguments(final JSONArray args) throws JSONException, IllegalArgumentException {
    final String method = getMethodFromArguments(args);
    final String url = getURLFromArguments(args);
    final List<Header> headers = getHeadersFromArguments(args);
    TypedString body = getBodyFromArguments(args);

    if (methodDoesNotPermitRequestBody(method)) {
      body = null;
    }

    return new Request(method, url, headers, body);
  }

  private static boolean methodDoesNotPermitRequestBody(final String method) {
    return !HttpMethod.permitsRequestBody(method);
  }

  private static String getURLFromArguments(final JSONArray args) throws JSONException {
    return args.getJSONObject(0).getString(PARAM_URL);
  }

  private static String getMethodFromArguments(final JSONArray args) throws JSONException {
    return args.getJSONObject(0).getString(PARAM_METHOD);
  }

  private static List<Header> getHeadersFromArguments(final JSONArray args) throws JSONException {
    final JSONObject headersObj = args.getJSONObject(0).getJSONObject(PARAM_HEADERS);
    final List<Header> headersList = new ArrayList<Header>();
    final Iterator<String> keys = headersObj.keys();

    while (keys.hasNext()) {
      String name = keys.next();
      String value = headersObj.getString(name);
      final Header header = new Header(name, value);
      headersList.add(header);
    }

    return headersList;
  }

  private static TypedString getBodyFromArguments(final JSONArray args) {
    TypedString body;

    try {
      body = new TypedString(args.getJSONObject(0).getString(PARAM_BODY));
    } catch (JSONException e) {
      body = new TypedString("");
    }

    return body;
  }
}

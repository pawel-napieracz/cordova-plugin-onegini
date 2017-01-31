/*
 * Copyright (c) 2016 Onegini B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.onegini.mobile.sdk.cordova.util;

import static com.onegini.mobile.sdk.cordova.OneginiCordovaPluginConstants.PARAM_AUTHENTICATOR_ID;
import static com.onegini.mobile.sdk.cordova.OneginiCordovaPluginConstants.PARAM_AUTHENTICATOR_TYPE;
import static com.onegini.mobile.sdk.cordova.OneginiCordovaPluginConstants.PARAM_BODY;
import static com.onegini.mobile.sdk.cordova.OneginiCordovaPluginConstants.PARAM_HEADERS;
import static com.onegini.mobile.sdk.cordova.OneginiCordovaPluginConstants.PARAM_METHOD;
import static com.onegini.mobile.sdk.cordova.OneginiCordovaPluginConstants.PARAM_PIN;
import static com.onegini.mobile.sdk.cordova.OneginiCordovaPluginConstants.PARAM_SCOPES;
import static com.onegini.mobile.sdk.cordova.OneginiCordovaPluginConstants.PARAM_URL;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.support.annotation.Nullable;
import com.onegini.mobile.sdk.android.model.OneginiAuthenticator;
import com.onegini.mobile.sdk.cordova.mobileAuthentication.Callback;
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

  public static Callback.Method getCallbackMethodFromArguments(final JSONArray args) throws JSONException {
    final String methodName = args.getJSONObject(0).getString(PARAM_METHOD);
    return Callback.Method.valueOf(methodName.toUpperCase());
  }

  public static boolean isFetchAnonymous(final JSONArray args) throws JSONException {
    return args.getJSONObject(0).getBoolean("anonymous");
  }

  @Nullable
  public static OneginiAuthenticator getAuthenticatorFromArguments(final JSONArray args,
                                                                   final Set<OneginiAuthenticator> availableAuthenticators) throws JSONException {

    final JSONObject options = args.getJSONObject(0);
    final String authenticatorType = options.getString(PARAM_AUTHENTICATOR_TYPE);
    final boolean hasAuthenticatorId = options.has(PARAM_AUTHENTICATOR_ID);

    if (hasAuthenticatorId) {
      final String authenticatorId = options.getString(PARAM_AUTHENTICATOR_ID);
      return getAuthenticatorByTypeAndId(availableAuthenticators, authenticatorType, authenticatorId);
    } else {
      return getAuthenticatorByType(availableAuthenticators, authenticatorType);
    }
  }

  @Nullable
  private static OneginiAuthenticator getAuthenticatorByType(final Set<OneginiAuthenticator> availableAuthenticators, final String targetType) {
    for (OneginiAuthenticator authenticator : availableAuthenticators) {
      final String type = AuthenticatorUtil.authenticatorTypeToString(authenticator.getType());

      if (type == null) {
        continue;
      }

      if (type.equals(targetType)) {
        return authenticator;
      }
    }

    return null;
  }

  @Nullable
  private static OneginiAuthenticator getAuthenticatorByTypeAndId(final Set<OneginiAuthenticator> availableAuthenticators, final String targetType,
                                                                  final String targetId) {
    for (OneginiAuthenticator authenticator : availableAuthenticators) {
      final String type = AuthenticatorUtil.authenticatorTypeToString(authenticator.getType());
      final String id = authenticator.getId();

      if (type == null || id == null) {
        continue;
      }

      if (type.equals(targetType) && id.equals(targetId)) {
        return authenticator;
      }
    }

    return null;
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

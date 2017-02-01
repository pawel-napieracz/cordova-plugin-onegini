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

package com.onegini.mobile.sdk.cordova.client;

import static com.onegini.mobile.sdk.cordova.OneginiCordovaPluginConstants.ERROR_CODE_HTTP_ERROR;
import static com.onegini.mobile.sdk.cordova.OneginiCordovaPluginConstants.ERROR_CODE_ILLEGAL_ARGUMENT;
import static com.onegini.mobile.sdk.cordova.OneginiCordovaPluginConstants.ERROR_CODE_IO_EXCEPTION;
import static com.onegini.mobile.sdk.cordova.OneginiCordovaPluginConstants.ERROR_CODE_PLUGIN_INTERNAL_ERROR;
import static com.onegini.mobile.sdk.cordova.OneginiCordovaPluginConstants.ERROR_DESCRIPTION_HTTP_ERROR;
import static com.onegini.mobile.sdk.cordova.OneginiCordovaPluginConstants.PARAM_ERROR_CODE;
import static com.onegini.mobile.sdk.cordova.OneginiCordovaPluginConstants.PARAM_ERROR_DESCRIPTION;
import static com.onegini.mobile.sdk.cordova.OneginiCordovaPluginConstants.TAG;
import static org.apache.cordova.PluginResult.Status.ERROR;
import static org.apache.cordova.PluginResult.Status.OK;

import java.io.IOException;
import java.security.InvalidParameterException;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import com.onegini.mobile.sdk.cordova.OneginiSDK;
import com.onegini.mobile.sdk.cordova.util.ActionArgumentsUtil;
import com.onegini.mobile.sdk.cordova.util.OkHttpResponseUtil;
import com.onegini.mobile.sdk.cordova.util.PluginResultBuilder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@SuppressWarnings("unused")
public class ResourceClient extends CordovaPlugin {

  private static final String ACTION_FETCH = "fetch";

  private static final String PARAM_STATUS = "status";
  private static final String PARAM_STATUS_TEXT = "statusText";
  private static final String PARAM_BODY = "body";
  private static final String PARAM_HEADERS = "headers";
  private static final String PARAM_HTTP_RESPONSE = "httpResponse";

  @Override
  public boolean execute(final String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {
    if (ACTION_FETCH.equals(action)) {
      fetch(args, callbackContext);
      return true;
    }

    return false;
  }

  private void fetch(final JSONArray args, final CallbackContext callbackContext) throws JSONException {
    final Request request;
    final JSONObject options = args.getJSONObject(0);
    final boolean isAnonymous = ActionArgumentsUtil.isFetchAnonymous(options);

    try {
      request = ActionArgumentsUtil.getRequestFromArguments(options);
    } catch (InvalidParameterException e) {
      callbackContext.sendPluginResult(new PluginResultBuilder()
          .withPluginError(e.getMessage(), ERROR_CODE_PLUGIN_INTERNAL_ERROR)
          .build());

      return;
    } catch (IllegalArgumentException e) {
      callbackContext.sendPluginResult(new PluginResultBuilder()
          .withPluginError(e.getMessage(), ERROR_CODE_ILLEGAL_ARGUMENT)
          .build());

      return;
    }

    cordova.getThreadPool().execute(new Runnable() {
      @Override
      public void run() {
        final OkHttpClient okClient;
        final Response response;

        if (isAnonymous) {
          okClient = getOneginiClient().getDeviceClient().getAnonymousResourceOkHttpClient();
        } else {
          okClient = getOneginiClient().getUserClient().getResourceOkHttpClient();
        }

        try {
          response = okClient.newCall(request).execute();
        } catch (IOException e) {
          callbackContext.sendPluginResult(new PluginResultBuilder()
              .withPluginError(e.getMessage(), ERROR_CODE_IO_EXCEPTION)
              .build());
          return;
        }

        callbackContext.sendPluginResult(pluginResultFromOkHttpResponse(response));
      }
    });
  }

  private PluginResult pluginResultFromOkHttpResponse(final Response response) {
    final PluginResult.Status resultStatus;
    final JSONObject responseJSON;
    try {
      responseJSON = okHttpResponseToJsonObject(response);
    } catch (Exception e) {
      return new PluginResultBuilder()
          .withPluginError(e.getMessage(), ERROR_CODE_PLUGIN_INTERNAL_ERROR)
          .build();
    }

    final JSONObject resultPayload;

    if (response.isSuccessful()) {
      resultStatus = OK;
      resultPayload = responseJSON;
    } else {
      resultStatus = ERROR;
      resultPayload = new JSONObject();

      try {
        resultPayload.put(PARAM_ERROR_CODE, ERROR_CODE_HTTP_ERROR);
        resultPayload.put(PARAM_ERROR_DESCRIPTION, ERROR_DESCRIPTION_HTTP_ERROR);
        resultPayload.put(PARAM_HTTP_RESPONSE, responseJSON);
      } catch (JSONException e) {
        Log.d(TAG, "Could not parse http response to JSON object");
      }
    }

    return new PluginResult(resultStatus, resultPayload);
  }

  private JSONObject okHttpResponseToJsonObject(final Response response) {
    final JSONObject responseJSON = new JSONObject();
    final int statusCode = response.code();
    final String statusText = response.message();
    final JSONObject headers = OkHttpResponseUtil.getJsonHeadersFromResponse(response);
    final String body = OkHttpResponseUtil.getBodyStringFromResponse(response);

    try {
      responseJSON.put(PARAM_STATUS, statusCode);
      responseJSON.put(PARAM_STATUS_TEXT, statusText);
      responseJSON.put(PARAM_HEADERS, headers);
      responseJSON.put(PARAM_BODY, body);
    } catch (JSONException e) {
      Log.e(TAG, "Could not parse http response to JSON object");
    }

    return responseJSON;
  }

  private com.onegini.mobile.sdk.android.client.OneginiClient getOneginiClient() {
    return OneginiSDK.getInstance().getOneginiClient(cordova.getActivity().getApplicationContext());
  }
}

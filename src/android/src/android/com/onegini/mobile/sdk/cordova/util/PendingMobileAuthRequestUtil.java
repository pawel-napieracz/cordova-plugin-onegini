/*
 * Copyright (c) 2017 Onegini B.V.
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

import static com.onegini.mobile.sdk.cordova.OneginiCordovaPluginConstants.PARAM_MESSAGE;
import static com.onegini.mobile.sdk.cordova.OneginiCordovaPluginConstants.PARAM_PROFILE_ID;
import static com.onegini.mobile.sdk.cordova.OneginiCordovaPluginConstants.PARAM_TIMESTAMP;
import static com.onegini.mobile.sdk.cordova.OneginiCordovaPluginConstants.PARAM_TIME_TO_LIVE_SECONDS;
import static com.onegini.mobile.sdk.cordova.OneginiCordovaPluginConstants.PARAM_TRANSACTION_ID;

import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.onegini.mobile.sdk.android.model.entity.OneginiMobileAuthWithPushRequest;

public class PendingMobileAuthRequestUtil {

  public static JSONArray pendingMobileAuthRequestSetToJSONArray(final Set<OneginiMobileAuthWithPushRequest> oneginiMobileAuthWithPushRequests) throws
      JSONException {
    JSONArray authenticatorJSONArray = new JSONArray();
    for (final OneginiMobileAuthWithPushRequest authenticator : oneginiMobileAuthWithPushRequests) {
      final JSONObject authenticatorJSON = pendingMobileAuthRequestToJSONObject(authenticator);
      authenticatorJSONArray.put(authenticatorJSON);
    }

    return authenticatorJSONArray;
  }


  private static JSONObject pendingMobileAuthRequestToJSONObject(final OneginiMobileAuthWithPushRequest oneginiMobileAuthWithPushRequest) throws JSONException {
    final JSONObject authenticatorJSON = new JSONObject();
    authenticatorJSON.put(PARAM_PROFILE_ID, oneginiMobileAuthWithPushRequest.getUserProfileId());
    authenticatorJSON.put(PARAM_TRANSACTION_ID, oneginiMobileAuthWithPushRequest.getTransactionId());
    authenticatorJSON.put(PARAM_MESSAGE, oneginiMobileAuthWithPushRequest.getMessage());
    authenticatorJSON.put(PARAM_TIMESTAMP, oneginiMobileAuthWithPushRequest.getTimestamp());
    authenticatorJSON.put(PARAM_TIME_TO_LIVE_SECONDS, oneginiMobileAuthWithPushRequest.getTimeToLiveSeconds());
    return authenticatorJSON;
  }

}

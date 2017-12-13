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

import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.onegini.mobile.sdk.android.model.entity.OneginiMobileAuthWithPushRequest;

public class PendingMobileAuthRequestUtil {

  private static final Gson gson = new Gson();

  public static OneginiMobileAuthWithPushRequest pendingMobileAuthRequestFromJSON(final String json) throws JSONException {
    return gson.fromJson(json, OneginiMobileAuthWithPushRequest.class);
  }

  public static JSONArray pendingMobileAuthRequestSetToJSONArray(final Set<OneginiMobileAuthWithPushRequest> oneginiMobileAuthWithPushRequests) throws JSONException {
    final JSONArray authenticatorJSONArray = new JSONArray();
    for (final OneginiMobileAuthWithPushRequest pushRequest : oneginiMobileAuthWithPushRequests) {
      authenticatorJSONArray.put(new JSONObject(gson.toJson(pushRequest)));
    }

    return authenticatorJSONArray;
  }
}

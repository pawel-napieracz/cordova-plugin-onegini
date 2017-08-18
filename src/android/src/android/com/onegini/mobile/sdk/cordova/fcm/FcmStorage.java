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

package com.onegini.mobile.sdk.cordova.fcm;

import android.content.Context;
import android.content.SharedPreferences;

public class FcmStorage {

  private static final String FILENAME = "fcm_shared_preference";
  private static final String KEY_REGISTRATION_TOKEN = "registration_token";

  private final SharedPreferences sharedPreferences;
  private final SharedPreferences.Editor editor;

  public FcmStorage(final Context context) {
    sharedPreferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
    editor = sharedPreferences.edit();
  }

  public void setRegistrationToken(final String token) {
    editor.putString(KEY_REGISTRATION_TOKEN, token);
  }

  public String getRegistrationToken() {
    return sharedPreferences.getString(KEY_REGISTRATION_TOKEN, "");
  }

  public void save() {
    editor.apply();
  }
}

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

package com.onegini.mobile.sdk.cordova.gcm;

import static com.onegini.mobile.sdk.cordova.OneginiCordovaPluginConstants.EXTRA_MOBILE_AUTHENTICATION;
import static com.onegini.mobile.sdk.cordova.OneginiCordovaPluginConstants.PUSH_MSG_CONTENT;
import static com.onegini.mobile.sdk.cordova.OneginiCordovaPluginConstants.PUSH_MSG_TRANSACTION_ID;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.gms.gcm.GcmListenerService;

@SuppressLint("Registered")
public class OneginiGcmListenerService extends GcmListenerService {

  @Override
  public void onMessageReceived(final String s, final Bundle pushMessage) {
    if (pushMessage.isEmpty()) {
      return;
    }

    if (isMobileAuthenticationRequest(pushMessage)) {
      startMainActivityWithIntentExtra(pushMessage);
    }
  }

  private void startMainActivityWithIntentExtra(final Bundle extra) {
    final String packageName = this.getPackageName();
    final Intent launchIntent = this.getPackageManager().getLaunchIntentForPackage(packageName);
    launchIntent.putExtra(EXTRA_MOBILE_AUTHENTICATION, extra);
    startActivity(launchIntent);
  }

  private boolean isMobileAuthenticationRequest(final Bundle pushMessage) {
    try {
      final JSONObject messageContent = new JSONObject(pushMessage.getString(PUSH_MSG_CONTENT));
      return messageContent.has(PUSH_MSG_TRANSACTION_ID);
    } catch (JSONException e) {
      return false;
    }
  }
}

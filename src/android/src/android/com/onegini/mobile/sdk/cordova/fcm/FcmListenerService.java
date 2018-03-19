/*
 * Copyright (c) 2017-2018 Onegini B.V.
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

import static com.onegini.mobile.sdk.cordova.OneginiCordovaPluginConstants.EXTRA_MOBILE_AUTHENTICATION;
import static com.onegini.mobile.sdk.cordova.OneginiCordovaPluginConstants.PUSH_MSG_CONTENT;
import static com.onegini.mobile.sdk.cordova.OneginiCordovaPluginConstants.PUSH_MSG_MESSAGE;
import static com.onegini.mobile.sdk.cordova.OneginiCordovaPluginConstants.PUSH_MSG_TRANSACTION_ID;
import static java.util.Collections.emptyMap;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.onegini.mobile.sdk.cordova.util.AppLifecycleUtil;

@SuppressLint("Registered")
public class FcmListenerService extends FirebaseMessagingService {

  @Override
  public void onMessageReceived(final RemoteMessage message) {
    if (message == null) {
      return;
    }
    if (isMobileAuthenticationRequest(message)) {
      handleNotification(message);
    }
  }

  private void handleNotification(final RemoteMessage message) {
    final Intent launchIntent = getLaunchIntentWithExtra(message);
    if (AppLifecycleUtil.isAppInForeground()) {
      startActivity(launchIntent);
    } else {
      NotificationHelper.getInstance(this).showNotification(launchIntent, getMessageFromAuthenticationRequest(message));
    }
  }

  @NonNull
  private Intent getLaunchIntentWithExtra(final RemoteMessage message) {
    final String packageName = this.getPackageName();
    final Intent launchIntent = this.getPackageManager().getLaunchIntentForPackage(packageName);
    launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    launchIntent.putExtra(EXTRA_MOBILE_AUTHENTICATION, message);
    return launchIntent;
  }

  @Nullable
  private String getMessageFromAuthenticationRequest(final RemoteMessage message) {
    try {
      final Map<String, String> data = getRemoteMessageData(message);
      final JSONObject messageContent = new JSONObject(data.get(PUSH_MSG_CONTENT));
      return messageContent.getString(PUSH_MSG_MESSAGE);
    } catch (JSONException e) {
      return null;
    }
  }

  private boolean isMobileAuthenticationRequest(final RemoteMessage message) {
    try {
      final Map<String, String> data = getRemoteMessageData(message);
      final JSONObject messageContent = new JSONObject(data.get(PUSH_MSG_CONTENT));
      return messageContent.has(PUSH_MSG_TRANSACTION_ID);
    } catch (JSONException e) {
      return false;
    }
  }

  private Map<String, String> getRemoteMessageData(RemoteMessage message) {
    final Map<String, String> data = message.getData();
    if (data == null) {
      return emptyMap();
    }

    return data;
  }
}

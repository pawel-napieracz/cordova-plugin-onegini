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

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import com.onegini.mobile.sdk.cordova.model.NotificationId;

public class NotificationHelper {

  private final Context context;

  public NotificationHelper(final Context context) {
    this.context = context;
  }

  public void showNotification(final Intent intent, final String message) {
    final NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
        .setDefaults(Notification.DEFAULT_ALL)
        .setSmallIcon(context.getApplicationInfo().icon)
        .setContentText(message)
        .setContentIntent(getPendingIntent(intent))
        .setPriority(NotificationCompat.PRIORITY_MAX)
        .setAutoCancel(true);

    getManager().notify(NotificationId.getId(), builder.build());
  }

  private NotificationManager getManager() {
    return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
  }

  private PendingIntent getPendingIntent(final Intent intent) {
    return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
  }
}

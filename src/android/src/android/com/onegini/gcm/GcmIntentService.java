package com.onegini.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.onegini.action.PluginInitializer;
import com.onegini.mobile.sdk.android.library.OneginiClient;

public class GcmIntentService extends IntentService {

  static final String TAG = "Notifications";

  public GcmIntentService() {
    super("GcmIntentService");
  }

  @Override
  public void onCreate() {
    super.onCreate();
  }

  @Override
  protected void onHandleIntent(final Intent intent) {
    final Bundle extras = intent.getExtras();
    final GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
    final String messageType = gcm.getMessageType(intent);

    if (!extras.isEmpty()) {
      if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
        Log.i(TAG, "Error");
      } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
        Log.i(TAG, "Deleted");
        // If it's a regular GCM message, do some work.
      } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
        Log.i(TAG, "Received: " + extras.toString());
      }
      getOneginiClient().handlePushNotification(extras);
    }
    // Release the wake lock provided by the WakefulBroadcastReceiver.
    GcmBroadcastReceiver.completeWakefulIntent(intent);
  }

  private OneginiClient getOneginiClient() {
    if (OneginiClient.getInstance() == null) {
      initializePlugin();
    }
    return OneginiClient.getInstance();
  }

  private void initializePlugin() {
    final PluginInitializer pluginInitializer = new PluginInitializer();
    pluginInitializer.setup(getApplication());
  }

}

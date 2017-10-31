package com.onegini.mobile.sdk.cordova.fcm;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import com.onegini.mobile.sdk.cordova.model.NotificationId;
import com.onegini.mobile.sdk.cordova.util.AppLifecycleUtil;

public class NotificationHelper {

  private static final String CHANNEL_ID = "transactions";

  private final Context context;

  public NotificationHelper(final Context context) {
    this.context = context;
    /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      registerNotificationChannel();
    }*/
  }

  public void handleIntent(final Intent intent, final String message) {
    if (AppLifecycleUtil.isAppInForeground()) {
      context.startActivity(intent);
    } else {
      showNotification(intent, message);
    }
  }

  public void showNotification(final Intent intent, final String message) {
    final NotificationCompat.Builder builder = new NotificationCompat.Builder(context/*, CHANNEL_ID*/)
        //TODO: add ic_launcher to res
        //.setSmallIcon(R.mipmap.ic_launcher)
        .setContentTitle("Confirm the transaction")
        .setContentText(message)
        .setContentIntent(getPendingIntent(intent))
        .setPriority(NotificationCompat.PRIORITY_MAX)
        .setAutoCancel(true);

    getManager().notify(NotificationId.getId(), builder.build());
  }

  /*@RequiresApi(api = Build.VERSION_CODES.O)
  private void registerNotificationChannel() {
    final NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "Transactions", NotificationManager.IMPORTANCE_HIGH);
    notificationChannel.setDescription("Onegini SDK");
    notificationChannel.enableLights(true);
    notificationChannel.setLightColor(Color.BLUE);
    notificationChannel.enableVibration(true);
    notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

    getManager().createNotificationChannel(notificationChannel);
  }*/

  private NotificationManager getManager() {
    return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
  }

  private PendingIntent getPendingIntent(final Intent intent) {
    return PendingIntent.getActivity(context, 0, intent, 0);
  }
}

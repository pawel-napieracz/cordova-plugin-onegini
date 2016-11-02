package com.onegini.gcm;

import static com.onegini.OneginiCordovaPluginConstants.EXTRA_MOBILE_AUTHENTICATION;
import static com.onegini.OneginiCordovaPluginConstants.PUSH_MSG_CONTENT;
import static com.onegini.OneginiCordovaPluginConstants.PUSH_MSG_TRANSACTION_ID;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.gms.gcm.GcmListenerService;

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

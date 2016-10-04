package com.onegini.gcm;

import android.os.Bundle;
import com.google.android.gms.gcm.GcmListenerService;
import com.onegini.handler.MobileAuthenticationHandler;

public class OneginiGcmListenerService extends GcmListenerService {
  @Override
  public void onMessageReceived(final String s, final Bundle bundle) {
    if (!bundle.isEmpty()) {
      MobileAuthenticationHandler.getInstance().queueBundle(bundle, this);
    }
  }
}

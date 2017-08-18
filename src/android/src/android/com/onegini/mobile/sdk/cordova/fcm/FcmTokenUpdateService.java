package com.onegini.mobile.sdk.cordova.fcm;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;
import com.google.firebase.iid.FirebaseInstanceId;
import com.onegini.mobile.sdk.android.client.OneginiClient;
import com.onegini.mobile.sdk.android.handlers.OneginiRefreshMobileAuthPushTokenHandler;
import com.onegini.mobile.sdk.android.handlers.error.OneginiRefreshMobileAuthPushTokenError;
import com.onegini.mobile.sdk.cordova.OneginiSDK;

public class FcmTokenUpdateService extends IntentService {

  private static final String TAG = FcmTokenUpdateService.class.getSimpleName();

  public FcmTokenUpdateService() {
    super(TAG);
  }

  @Override
  protected void onHandleIntent(@Nullable final Intent intent) {
    final FcmTokenService fcmTokenService = new FcmTokenService(getApplicationContext());
    if (intent == null) {
      return;
    }

    if (isSdkNotStarted()) {
      return;
    }

    if (tokenIsTheSameAsStoredToken(fcmTokenService)) {
      return;
    }

    String newToken = FirebaseInstanceId.getInstance().getToken();
    getOneginiClient().getDeviceClient().refreshMobileAuthPushToken(newToken, new TokenUpdateHandler(newToken, fcmTokenService));
  }

  private boolean isSdkNotStarted() {
    return !OneginiSDK.getInstance().isStarted();
  }

  private boolean tokenIsTheSameAsStoredToken(final FcmTokenService fcmTokenService) {
    return !fcmTokenService.shouldUpdateToken(FirebaseInstanceId.getInstance().getToken());
  }

  private OneginiClient getOneginiClient() {
    return OneginiSDK.getInstance().getOneginiClient(getApplicationContext());
  }

  private class TokenUpdateHandler implements OneginiRefreshMobileAuthPushTokenHandler {

    private String newToken;
    private FcmTokenService fcmTokenService;

    public TokenUpdateHandler(final String newToken, final FcmTokenService fcmTokenService) {
      this.newToken = newToken;
      this.fcmTokenService = fcmTokenService;
    }

    @Override
    public void onSuccess() {
      Log.d(TAG, "The FCM push token has been updated.");
      fcmTokenService.storeToken(newToken);
    }

    @Override
    public void onError(final OneginiRefreshMobileAuthPushTokenError error) {
      Log.e(TAG, "The FCM push token update has failed: " + error.getMessage());
    }
  }
}

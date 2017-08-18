package com.onegini.mobile.sdk.cordova.fcm;

import static com.onegini.mobile.sdk.cordova.OneginiCordovaPluginConstants.EXTRA_FCM_TOKEN_REFRESHED;
import static com.onegini.mobile.sdk.cordova.OneginiCordovaPluginConstants.EXTRA_SDK_STARTED;

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
  private boolean sdkInitialized = false;
  private String updatedToken = null;

  public FcmTokenUpdateService() {
    super(TAG);
  }

  @Override
  protected void onHandleIntent(@Nullable final Intent intent) {
    if (intent == null) {
      return;
    }

    boolean sdkInitialized = intent.getBooleanExtra(EXTRA_SDK_STARTED, false);
    if (sdkInitialized) {
      this.sdkInitialized = true;
      sendUpdatedTokenToServer();
    }

    boolean tokenRefreshed = intent.getBooleanExtra(EXTRA_FCM_TOKEN_REFRESHED, false);
    if (tokenRefreshed) {
      onTokenRefresh();
    }
  }

  private void onTokenRefresh() {
    final String token = FirebaseInstanceId.getInstance().getToken();
    final FcmTokenService fcmTokenService = new FcmTokenService(getApplicationContext());
    if (fcmTokenService.shouldUpdateToken(token)) {
      this.updatedToken = token;
      sendUpdatedTokenToServer();
    } else {
      // the token is created for the first time
      fcmTokenService.storeToken(token);
    }
  }

  private void sendUpdatedTokenToServer() {
    boolean tokenUpdated = updatedToken != null && !"".equals(updatedToken);
    if (sdkInitialized && tokenUpdated) {
      getOneginiClient().getDeviceClient().refreshMobileAuthPushToken(updatedToken, new TokenUpdateHandler());
      this.updatedToken = null;
    }
  }

  private class TokenUpdateHandler implements OneginiRefreshMobileAuthPushTokenHandler {

    public TokenUpdateHandler() {
    }

    @Override
    public void onSuccess() {
      Log.d(TAG, "The FCM push token has been updated.");
    }

    @Override
    public void onError(final OneginiRefreshMobileAuthPushTokenError error) {
      Log.e(TAG, "The FCM push token update has failed: " + error.getMessage());
    }
  }

  private OneginiClient getOneginiClient() {
    return OneginiSDK.getInstance().getOneginiClient(getApplicationContext());
  }
}

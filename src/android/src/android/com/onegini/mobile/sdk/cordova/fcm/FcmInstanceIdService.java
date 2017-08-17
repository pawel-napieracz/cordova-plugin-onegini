package com.onegini.mobile.sdk.cordova.fcm;

import android.util.Log;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.onegini.mobile.sdk.android.client.OneginiClient;
import com.onegini.mobile.sdk.android.handlers.OneginiRefreshMobileAuthPushTokenHandler;
import com.onegini.mobile.sdk.android.handlers.error.OneginiRefreshMobileAuthPushTokenError;
import com.onegini.mobile.sdk.cordova.OneginiSDK;

public class FcmInstanceIdService extends FirebaseInstanceIdService {

  private static final String TAG = FcmInstanceIdService.class.getSimpleName();

  /**
   * Called if InstanceID token is created or updated. This may occur if the security of the previous token had been compromised.
   * This call is initiated by the InstanceID provider.
   */
  @Override
  public void onTokenRefresh() {
    final String token = FirebaseInstanceId.getInstance().getToken();
    final FcmRegistrationService fcmRegistrationService = new FcmRegistrationService(this);
    if (fcmRegistrationService.shouldUpdateToken(token)) {
      // the token was updated, notify the SDK
      sendUpdatedTokenToServer(token);
    } else {
      // the token is created for the first time
      fcmRegistrationService.storeToken(token);
    }
  }

  private void sendUpdatedTokenToServer(final String token) {
    getOneginiClient().getDeviceClient().refreshMobileAuthPushToken(token, new TokenUpdateHandler());
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

package com.onegini.mobile.sdk.cordova.fcm;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.onegini.mobile.sdk.android.handlers.OneginiRefreshMobileAuthPushTokenHandler;
import com.onegini.mobile.sdk.android.handlers.error.OneginiRefreshMobileAuthPushTokenError;
import com.onegini.mobile.sdk.cordova.OneginiSDK;

import static com.onegini.mobile.sdk.cordova.OneginiCordovaPluginConstants.TAG;

public class FcmRegistrationService {

  private final Context context;
  private final FcmStorage storage;

  public FcmRegistrationService(final Context context) {
    this.context = context;
     storage = new FcmStorage(context);
  }

  public void getFCMToken(final TokenReadHandler tokenReadHandler) {
    Log.d("ROBERT", "getFCMToken");
    FirebaseApp.initializeApp(context);
    FirebaseInstanceId.getInstance().getInstanceId()
        .addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
          @Override
          public void onSuccess(final InstanceIdResult instanceIdResult) {
            final String fcmRefreshToken = instanceIdResult.getToken();
            Log.d("ROBERT", "getFCMToken success "+fcmRefreshToken);
            tokenReadHandler.onSuccess(fcmRefreshToken);
          }
        })
        .addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull final Exception e) {
            Log.d("ROBERT", "getFCMToken error "+e.getMessage());
            tokenReadHandler.onError(e);
          }
        });
  }

  public boolean shouldUpdateRefreshToken(final String refreshToken) {
    Log.d("ROBERT", "shouldUpdateRefreshToken");
    final String previousRefreshToken = storage.getRegistrationToken();
    if (previousRefreshToken.isEmpty()) {
      return false;
    }
    return !previousRefreshToken.equals(refreshToken);
  }

  public void storeNewRefreshToken(final String newRefreshToken) {
    Log.d("ROBERT", "storeNewRefreshToken ("+newRefreshToken+")");
    storage.setRegistrationToken(newRefreshToken);
    storage.save();
  }

  public void updateRefreshToken(final String newRefreshToken) {
    Log.d("ROBERT", "updateRefreshToken "+newRefreshToken);
    getOneginiClient().getDeviceClient()
        .refreshMobileAuthPushToken(newRefreshToken, new TokenUpdateHandler(newRefreshToken));
  }

  private com.onegini.mobile.sdk.android.client.OneginiClient getOneginiClient() {
    return OneginiSDK.getInstance().getOneginiClient(context);
  }

  public interface TokenReadHandler {

    void onSuccess(String token);

    void onError(Exception e);
  }

  private class TokenUpdateHandler implements OneginiRefreshMobileAuthPushTokenHandler {

    private String newToken;

    public TokenUpdateHandler(final String newToken) {
      this.newToken = newToken;
    }

    @Override
    public void onSuccess() {
      Log.d(TAG, "The FCM push token has been updated.");
      storeNewRefreshToken(newToken);
    }

    @Override
    public void onError(final OneginiRefreshMobileAuthPushTokenError error) {
      Log.e(TAG, "The FCM push token update has failed: " + error.getMessage());
      storeNewRefreshToken("");
    }
  }
}

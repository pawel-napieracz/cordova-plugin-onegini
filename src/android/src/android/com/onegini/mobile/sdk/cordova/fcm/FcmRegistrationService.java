package com.onegini.mobile.sdk.cordova.fcm;

import android.content.Context;
import android.support.annotation.NonNull;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.onegini.mobile.sdk.android.handlers.OneginiMobileAuthWithPushEnrollmentHandler;
import com.onegini.mobile.sdk.cordova.OneginiSDK;
import com.onegini.mobile.sdk.cordova.handler.MobileAuthWithPushEnrollmentHandler;
import com.onegini.mobile.sdk.cordova.util.PluginResultBuilder;
import org.apache.cordova.CallbackContext;

import static com.onegini.mobile.sdk.cordova.OneginiCordovaPluginConstants.ERROR_CODE_CONFIGURATION;
import static com.onegini.mobile.sdk.cordova.OneginiCordovaPluginConstants.ERROR_CODE_PLUGIN_INTERNAL_ERROR;

public class FcmRegistrationService {

  private final Context context;

  public FcmRegistrationService(final Context context) {
    this.context = context;
  }

  public void enrollForPush(final CallbackContext callbackContext) {
    FirebaseApp.initializeApp(context);
    FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
      @Override
      public void onSuccess(final InstanceIdResult instanceIdResult) {
        final String fcmRefreshToken = instanceIdResult.getToken();
        if (fcmRefreshToken == null || fcmRefreshToken.isEmpty()) {
          callbackContext.sendPluginResult(new PluginResultBuilder()
              .withPluginError("Cannot enroll for push mobile auth: FCM Token is null. Please check your 'google-services.json'.", ERROR_CODE_CONFIGURATION)
              .build());
        } else {
          final OneginiMobileAuthWithPushEnrollmentHandler handler = new MobileAuthWithPushEnrollmentHandler(callbackContext);
          getOneginiClient().getUserClient().enrollUserForMobileAuthWithPush(fcmRefreshToken, handler);
        }
      }
    }).addOnFailureListener(new OnFailureListener() {
      @Override
      public void onFailure(@NonNull final Exception e) {
        callbackContext.sendPluginResult(new PluginResultBuilder()
            .withPluginError(e.getMessage(), ERROR_CODE_PLUGIN_INTERNAL_ERROR)
            .build());
      }
    });
  }

  private com.onegini.mobile.sdk.android.client.OneginiClient getOneginiClient() {
    return OneginiSDK.getInstance().getOneginiClient(context);
  }

}

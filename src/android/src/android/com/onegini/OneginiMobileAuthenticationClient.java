package com.onegini;

import static com.onegini.OneginiCordovaPluginConstants.ERROR_NO_USER_AUTHENTICATED;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.onegini.handler.MobileAuthenticationEnrollmentHandler;
import com.onegini.mobile.sdk.android.model.entity.UserProfile;
import com.onegini.util.PluginResultBuilder;

public class OneginiMobileAuthenticationClient extends CordovaPlugin {

  private static final String ACTION_ENROLL = "enroll";
  private static final String GCM_SENDER_ID = "586427927998";


  @Override
  public boolean execute(final String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {
    if (ACTION_ENROLL.equals(action)) {
      enroll(args, callbackContext);
      return true;
    }

    return false;
  }

  private void enroll(final JSONArray args, final CallbackContext callbackContext) {
    final UserProfile userProfile = getOneginiClient().getUserClient().getAuthenticatedUserProfile();

    if (userProfile == null) {
      callbackContext.sendPluginResult(new PluginResultBuilder()
          .withErrorDescription(ERROR_NO_USER_AUTHENTICATED)
          .build());

      return;
    }

    cordova.getThreadPool().execute(new Runnable() {
      @Override
      public void run() {
        final String registrationID;

        try {
          InstanceID instanceID = InstanceID.getInstance(getApplicationContext());
          registrationID = instanceID.getToken(GCM_SENDER_ID, GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
        } catch (Exception e) {
          callbackContext.sendPluginResult(new PluginResultBuilder()
              .withError()
              .build());

          return;
        }

        final MobileAuthenticationEnrollmentHandler mobileAuthenticationEnrollmentHandler = new MobileAuthenticationEnrollmentHandler(callbackContext);

        getOneginiClient().getUserClient().enrollUserForMobileAuthentication(registrationID, mobileAuthenticationEnrollmentHandler);
      }
    });
  }

  private com.onegini.mobile.sdk.android.client.OneginiClient getOneginiClient() {
    return OneginiSDK.getOneginiClient(getApplicationContext());
  }

  private Context getApplicationContext() {
    return cordova.getActivity().getApplicationContext();
  }
}

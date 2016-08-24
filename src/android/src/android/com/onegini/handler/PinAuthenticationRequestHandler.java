package com.onegini.handler;

import android.util.Log;
import com.onegini.mobile.android.sdk.handlers.request.OneginiPinAuthenticationRequestHandler;
import com.onegini.mobile.android.sdk.handlers.request.callback.OneginiPinCallback;
import com.onegini.mobile.android.sdk.model.entity.UserProfile;

public class PinAuthenticationRequestHandler implements OneginiPinAuthenticationRequestHandler {

  @Override
  public void startAuthentication(final UserProfile userProfile, final OneginiPinCallback oneginiPinCallback) {
    Log.v("OneginiCordovaPlugin", "startAuthentication");
  }

  @Override
  public void onNextAuthenticationAttempt(final int i, final int i1) {
    Log.v("OneginiCordovaPlugin", "onNextAuthenticationAttempt");
  }

  @Override
  public void finishAuthentication() {
    Log.v("OneginiCordovaPlugin", "finishAuthentication");
  }
}

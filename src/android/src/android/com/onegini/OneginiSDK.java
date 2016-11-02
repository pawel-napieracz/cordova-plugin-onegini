package com.onegini;

import android.content.Context;
import com.onegini.handler.CreatePinRequestHandler;
import com.onegini.handler.FingerprintAuthenticationHandler;
import com.onegini.handler.MobileAuthenticationHandler;
import com.onegini.handler.PinAuthenticationRequestHandler;
import com.onegini.mobile.sdk.android.client.OneginiClient;
import com.onegini.mobile.sdk.android.client.OneginiClientBuilder;
import com.onegini.mobile.sdk.android.handlers.OneginiInitializationHandler;

public class OneginiSDK {
  private static OneginiSDK instance;
  private boolean isStarted;

  protected OneginiSDK() {
  }

  public static OneginiSDK getInstance() {
    if(instance == null) {
      instance = new OneginiSDK();
    }

    return instance;
  }

  public OneginiClient getOneginiClient(final Context context) {
    OneginiClient oneginiClient = OneginiClient.getInstance();
    if (oneginiClient == null) {
      oneginiClient = buildSDK(context);
    }
    return oneginiClient;
  }

  private static OneginiClient buildSDK(final Context context) {
    final Context applicationContext = context.getApplicationContext();
    final CreatePinRequestHandler createPinRequestHandler = CreatePinRequestHandler.getInstance();
    final PinAuthenticationRequestHandler pinAuthenticationRequestHandler = PinAuthenticationRequestHandler.getInstance();
    final FingerprintAuthenticationHandler fingerprintAuthenticationHandler = FingerprintAuthenticationHandler.getInstance();
    final MobileAuthenticationHandler mobileAuthenticationHandler = MobileAuthenticationHandler.getInstance();

    return new OneginiClientBuilder(applicationContext, createPinRequestHandler, pinAuthenticationRequestHandler)
        .setMobileAuthenticationRequestHandler(mobileAuthenticationHandler)
        .setMobileAuthenticationPinRequestHandler(mobileAuthenticationHandler)
        .setFingerprintAuthenticatioRequestHandler(fingerprintAuthenticationHandler)
        .build();
  }

  public boolean isStarted() {
    return isStarted;
  }

  public void startSDK(final Context context, final OneginiInitializationHandler initializationHandler) {
    getOneginiClient(context).start(initializationHandler);
    this.isStarted = true;
  }
}

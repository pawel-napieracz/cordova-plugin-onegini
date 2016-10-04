package com.onegini;

import android.content.Context;
import com.onegini.handler.CreatePinRequestHandler;
import com.onegini.handler.MobileAuthenticationHandler;
import com.onegini.handler.PinAuthenticationRequestHandler;
import com.onegini.mobile.sdk.android.client.OneginiClient;
import com.onegini.mobile.sdk.android.client.OneginiClientBuilder;

public class OneginiSDK {
  public static OneginiClient getOneginiClient(final Context context) {
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
    final MobileAuthenticationHandler mobileAuthenticationHandler = MobileAuthenticationHandler.getInstance();

    return new OneginiClientBuilder(applicationContext, createPinRequestHandler, pinAuthenticationRequestHandler)
        .setMobileAuthenticationRequestHandler(mobileAuthenticationHandler)
        .build();
  }
}

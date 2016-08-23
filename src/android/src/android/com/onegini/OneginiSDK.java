package com.onegini;

import android.content.Context;
import com.onegini.handler.CreatePinRequestHandler;
import com.onegini.handler.PinAuthenticationRequestHandler;
import com.onegini.mobile.android.sdk.client.OneginiClient;
import com.onegini.mobile.android.sdk.client.OneginiClientBuilder;

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
    final CreatePinRequestHandler createPinRequestHandler = new CreatePinRequestHandler();
    final PinAuthenticationRequestHandler pinAuthenticationRequestHandler = new PinAuthenticationRequestHandler();

    return new OneginiClientBuilder(applicationContext, createPinRequestHandler, pinAuthenticationRequestHandler)
        .build();
  }
}

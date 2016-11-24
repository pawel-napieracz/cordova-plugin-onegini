/*
 * Copyright (c) 2016 Onegini B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.onegini;

import android.content.Context;
import com.onegini.handler.CreatePinRequestHandler;
import com.onegini.handler.FingerprintAuthenticationRequestHandler;
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
    final FingerprintAuthenticationRequestHandler fingerprintAuthenticationRequestHandler = FingerprintAuthenticationRequestHandler.getInstance();
    final MobileAuthenticationHandler mobileAuthenticationHandler = MobileAuthenticationHandler.getInstance();

    return new OneginiClientBuilder(applicationContext, createPinRequestHandler, pinAuthenticationRequestHandler)
        .setMobileAuthenticationRequestHandler(mobileAuthenticationHandler)
        .setMobileAuthenticationPinRequestHandler(mobileAuthenticationHandler)
        .setFingerprintAuthenticatioRequestHandler(fingerprintAuthenticationRequestHandler)
        .setMobileAuthenticationFingerprintRequestHandler(mobileAuthenticationHandler)
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

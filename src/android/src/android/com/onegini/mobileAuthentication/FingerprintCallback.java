package com.onegini.mobileAuthentication;

import com.onegini.mobile.sdk.android.handlers.request.callback.OneginiFingerprintCallback;
import com.onegini.mobile.sdk.android.model.entity.OneginiMobileAuthenticationRequest;

public class FingerprintCallback extends Callback {
  private final OneginiFingerprintCallback fingerprintCallback;

  public FingerprintCallback(final OneginiMobileAuthenticationRequest mobileAuthenticationRequest,
                             final OneginiFingerprintCallback fingerprintCallback) {
    super(mobileAuthenticationRequest, Method.FINGERPRINT);
    this.fingerprintCallback = fingerprintCallback;
  }

  public OneginiFingerprintCallback getFingerprintCallback() {
    return fingerprintCallback;
  }
}

package com.onegini.mobileAuthentication;

import com.onegini.mobile.sdk.android.handlers.request.callback.OneginiPinCallback;
import com.onegini.mobile.sdk.android.model.entity.AuthenticationAttemptCounter;
import com.onegini.mobile.sdk.android.model.entity.OneginiMobileAuthenticationRequest;

public class PinCallback extends Callback {
  private final OneginiPinCallback pinCallback;
  private final AuthenticationAttemptCounter authenticationAttemptCounter;

  public PinCallback(final OneginiMobileAuthenticationRequest mobileAuthenticationRequest,
                     final OneginiPinCallback pinCallback,
                     final AuthenticationAttemptCounter authenticationAttemptCounter) {
    super(mobileAuthenticationRequest, Callback.Method.PIN);
    this.authenticationAttemptCounter = authenticationAttemptCounter;
    this.pinCallback = pinCallback;
  }

  public AuthenticationAttemptCounter getAuthenticationAttemptCounter() {
    return authenticationAttemptCounter;
  }

  public OneginiPinCallback getPinCallback() {
    return pinCallback;
  }
}

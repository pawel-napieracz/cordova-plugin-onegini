package com.onegini.mobileAuthentication;

import com.onegini.mobile.sdk.android.handlers.request.callback.OneginiAcceptDenyCallback;
import com.onegini.mobile.sdk.android.model.entity.OneginiMobileAuthenticationRequest;

public class ConfirmationCallback extends Callback {
  private final OneginiAcceptDenyCallback acceptDenyCallback;

  public ConfirmationCallback(final OneginiMobileAuthenticationRequest mobileAuthenticationRequest,
                              final OneginiAcceptDenyCallback acceptDenyCallback) {
    super(mobileAuthenticationRequest, Callback.Method.CONFIRMATION);
    this.acceptDenyCallback = acceptDenyCallback;
  }

  public OneginiAcceptDenyCallback getAcceptDenyCallback() {
    return acceptDenyCallback;
  }

}

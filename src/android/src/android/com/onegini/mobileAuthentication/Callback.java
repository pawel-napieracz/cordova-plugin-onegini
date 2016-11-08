package com.onegini.mobileAuthentication;

import org.apache.cordova.CallbackContext;

import com.onegini.mobile.sdk.android.model.entity.OneginiMobileAuthenticationRequest;

public class Callback {

  private final OneginiMobileAuthenticationRequest mobileAuthenticationRequest;
  private final Method method;
  private CallbackContext challengeResponseCallbackContext;

  public Callback(final OneginiMobileAuthenticationRequest mobileAuthenticationRequest, final Method method) {
    this.mobileAuthenticationRequest = mobileAuthenticationRequest;
    this.method = method;
  }

  public OneginiMobileAuthenticationRequest getMobileAuthenticationRequest() {
    return mobileAuthenticationRequest;
  }

  public Method getMethod() {
    return method;
  }

  public CallbackContext getChallengeResponseCallbackContext() {
    return challengeResponseCallbackContext;
  }

  public void setChallengeResponseCallbackContext(final CallbackContext resultCallbackContext) {
    this.challengeResponseCallbackContext = resultCallbackContext;
  }

  public enum Method {
    CONFIRMATION,
    PIN,
    FINGERPRINT
  }

}

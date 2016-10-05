package com.onegini.mobileAuthentication;

import org.apache.cordova.CallbackContext;

import com.onegini.mobile.sdk.android.model.entity.OneginiMobileAuthenticationRequest;

public class Callback {

  private final OneginiMobileAuthenticationRequest mobileAuthenticationRequest;
  private final Method method;
  private CallbackContext resultCallbackContext;

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

  public CallbackContext getResultCallbackContext() {
    return resultCallbackContext;
  }

  public void setResultCallbackContext(final CallbackContext resultCallbackContext) {
    this.resultCallbackContext = resultCallbackContext;
  }

  public enum Method {
    CONFIRMATION,
    PIN
  }

}

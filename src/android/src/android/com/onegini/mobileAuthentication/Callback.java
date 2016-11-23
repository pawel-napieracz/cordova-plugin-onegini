package com.onegini.mobileAuthentication;

import static com.onegini.OneginiCordovaPluginConstants.AUTH_EVENT_CONFIRMATION_REQUEST;
import static com.onegini.OneginiCordovaPluginConstants.AUTH_EVENT_FINGERPRINT_REQUEST;
import static com.onegini.OneginiCordovaPluginConstants.AUTH_EVENT_PIN_REQUEST;

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

  public String getAuthenticationRequestEventName() {
    final String eventName;

    switch (method) {
      case CONFIRMATION:
        eventName = AUTH_EVENT_CONFIRMATION_REQUEST;
        break;
      case PIN:
        eventName = AUTH_EVENT_PIN_REQUEST;
        break;
      case FINGERPRINT:
        eventName = AUTH_EVENT_FINGERPRINT_REQUEST;
        break;
      default:
        eventName = null;
        break;
    }

    return eventName;
  }

  public enum Method {
    CONFIRMATION,
    PIN,
    FINGERPRINT
  }

}

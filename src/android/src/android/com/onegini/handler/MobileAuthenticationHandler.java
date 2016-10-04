package com.onegini.handler;

import java.util.LinkedList;
import java.util.Queue;

import org.apache.cordova.CallbackContext;

import android.content.Context;
import android.os.Bundle;
import com.onegini.OneginiSDK;
import com.onegini.mobile.sdk.android.handlers.OneginiMobileAuthenticationHandler;
import com.onegini.mobile.sdk.android.handlers.error.OneginiMobileAuthenticationError;
import com.onegini.mobile.sdk.android.handlers.request.OneginiMobileAuthenticationRequestHandler;
import com.onegini.mobile.sdk.android.handlers.request.callback.OneginiAcceptDenyCallback;
import com.onegini.mobile.sdk.android.model.entity.OneginiMobileAuthenticationRequest;
import com.onegini.util.PluginResultBuilder;

public class MobileAuthenticationHandler implements OneginiMobileAuthenticationHandler, OneginiMobileAuthenticationRequestHandler {

  private static MobileAuthenticationHandler instance = null;
  private final Queue<Bundle> bundleQueue = new LinkedList<Bundle>();
  private CallbackContext confirmationChallengeCallbackContext;
  private CallbackContext completeMobileAuthenticationCallbackContext;
  private OneginiAcceptDenyCallback acceptDenyCallback = null;
  private Context context;

  protected MobileAuthenticationHandler() {
  }

  public static MobileAuthenticationHandler getInstance() {
    if (instance == null) {
      instance = new MobileAuthenticationHandler();
    }

    return instance;
  }

  public void queueBundle(final Bundle bundle, final Context context) {
    if (bundleQueue.peek() == null) {
      this.context = context;
      handleBundle(bundle);
    }

    bundleQueue.add(bundle);
  }

  public void setConfirmationChallengeCallbackContext(final CallbackContext confirmationChallengeCallbackContext) {
    this.confirmationChallengeCallbackContext = confirmationChallengeCallbackContext;
  }

  public void setCompleteMobileAuthenticationCallbackContext(final CallbackContext completeMobileAuthenticationCallbackContext) {
    this.completeMobileAuthenticationCallbackContext = completeMobileAuthenticationCallbackContext;
  }

  public void replyToConfirmationChallenge(final Boolean shouldAccept) {
    if (shouldAccept) {
      acceptDenyCallback.acceptAuthenticationRequest();
    } else {
      acceptDenyCallback.denyAuthenticationRequest();
    }
  }

  @Override
  public void startAuthentication(final OneginiMobileAuthenticationRequest mobileAuthenticationRequest,
                                  final OneginiAcceptDenyCallback acceptDenyCallback) {
    if( confirmationChallengeCallbackContext == null) {
      acceptDenyCallback.denyAuthenticationRequest();
    }
    else {
      this.acceptDenyCallback = acceptDenyCallback;
      confirmationChallengeCallbackContext.sendPluginResult(new PluginResultBuilder()
          .withSuccess()
          .withOneginiMobileAuthenticationRequest(mobileAuthenticationRequest)
          .shouldKeepCallback()
          .build());
    }
  }

  @Override
  public void finishAuthentication() {
  }

  @Override
  public void onSuccess() {
    completeMobileAuthenticationCallbackContext.sendPluginResult(new PluginResultBuilder()
        .withSuccess()
        .build());

    handleNextBundle();
  }

  @Override
  public void onError(final OneginiMobileAuthenticationError oneginiMobileAuthenticationError) {
    completeMobileAuthenticationCallbackContext.sendPluginResult(new PluginResultBuilder()
        .withError()
        .build());

    handleNextBundle();
  }

  private void handleBundle(final Bundle bundle) {
    OneginiSDK.getOneginiClient(context).getUserClient().handleMobileAuthenticationRequest(bundle, this);
  }

  private void handleNextBundle() {
    bundleQueue.poll();
    if (bundleQueue.peek() != null) {
      handleBundle(bundleQueue.peek());
    }
  }
}

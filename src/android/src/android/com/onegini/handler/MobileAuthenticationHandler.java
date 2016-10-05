package com.onegini.handler;

import static com.onegini.OneginiCordovaPluginConstants.ERROR_NO_CONFIRMATION_CHALLENGE;
import static com.onegini.OneginiCordovaPluginConstants.ERROR_NO_PIN_CHALLENGE;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;

import com.onegini.mobile.sdk.android.handlers.OneginiMobileAuthenticationHandler;
import com.onegini.mobile.sdk.android.handlers.error.OneginiError;
import com.onegini.mobile.sdk.android.handlers.error.OneginiMobileAuthenticationError;
import com.onegini.mobile.sdk.android.handlers.request.OneginiMobileAuthenticationPinRequestHandler;
import com.onegini.mobile.sdk.android.handlers.request.OneginiMobileAuthenticationRequestHandler;
import com.onegini.mobile.sdk.android.handlers.request.callback.OneginiAcceptDenyCallback;
import com.onegini.mobile.sdk.android.handlers.request.callback.OneginiPinCallback;
import com.onegini.mobile.sdk.android.model.entity.AuthenticationAttemptCounter;
import com.onegini.mobile.sdk.android.model.entity.OneginiMobileAuthenticationRequest;
import com.onegini.mobileAuthentication.Callback;
import com.onegini.mobileAuthentication.ConfirmationCallback;
import com.onegini.mobileAuthentication.PinCallback;
import com.onegini.util.PluginResultBuilder;

public class MobileAuthenticationHandler
    implements OneginiMobileAuthenticationHandler, OneginiMobileAuthenticationRequestHandler, OneginiMobileAuthenticationPinRequestHandler {

  private static MobileAuthenticationHandler instance = null;
  private final HashMap<Callback.Method, CallbackContext> challengeReceivers = new HashMap<Callback.Method, CallbackContext>();
  private final Queue<Callback> callbackQueue = new LinkedList<Callback>();
  private boolean isRunning = false;

  protected MobileAuthenticationHandler() {
  }

  public static MobileAuthenticationHandler getInstance() {
    if (instance == null) {
      instance = new MobileAuthenticationHandler();
    }

    return instance;
  }

  public void registerAuthenticationChallengeReceiver(final Callback.Method method, final CallbackContext callbackContext) {
    challengeReceivers.put(method, callbackContext);
    handleNextAuthenticationRequest();
  }

  @Override
  public void onNextAuthenticationAttempt(final AuthenticationAttemptCounter authenticationAttemptCounter) {
    final CallbackContext callbackContext = getChallengeReceiverForCallbackMethod(Callback.Method.PIN);
    final PinCallback pinCallback = (PinCallback) callbackQueue.peek();

    callbackContext.sendPluginResult(new PluginResultBuilder()
        .withSuccess()
        .shouldKeepCallback()
        .withRemainingFailureCount(authenticationAttemptCounter.getRemainingAttempts())
        .withMaxFailureCount(authenticationAttemptCounter.getMaxAttempts())
        .withOneginiMobileAuthenticationRequest(pinCallback.getMobileAuthenticationRequest())
        .build());
  }

  @Override
  public void startAuthentication(final OneginiMobileAuthenticationRequest oneginiMobileAuthenticationRequest, final OneginiPinCallback oneginiPinCallback,
                                  final AuthenticationAttemptCounter authenticationAttemptCounter) {
    final PinCallback pinCallback = new PinCallback(oneginiMobileAuthenticationRequest, oneginiPinCallback, authenticationAttemptCounter);
    addAuthenticationRequestToQueue(pinCallback);
  }

  @Override
  public void startAuthentication(final OneginiMobileAuthenticationRequest mobileAuthenticationRequest,
                                  final OneginiAcceptDenyCallback acceptDenyCallback) {
    final ConfirmationCallback confirmationCallback = new ConfirmationCallback(mobileAuthenticationRequest, acceptDenyCallback);
    addAuthenticationRequestToQueue(confirmationCallback);
  }

  public void replyToPinChallenge(final CallbackContext callbackContext, final boolean shouldAccept, final char[] pin) {
    if (!(callbackQueue.peek() instanceof PinCallback)) {
      callbackContext.sendPluginResult(new PluginResultBuilder()
          .withErrorDescription(ERROR_NO_PIN_CHALLENGE)
          .build());

      return;
    }

    final PinCallback pinCallback = (PinCallback) callbackQueue.peek();
    pinCallback.setResultCallbackContext(callbackContext);

    if (shouldAccept) {
      pinCallback.getPinCallback().acceptAuthenticationRequest(pin);
    } else {
      pinCallback.getPinCallback().denyAuthenticationRequest();
    }
  }

  public void replyToConfirmationChallenge(final CallbackContext callbackContext, final boolean shouldAccept) {
    if (!(callbackQueue.peek() instanceof ConfirmationCallback)) {
      callbackContext.sendPluginResult(new PluginResultBuilder()
          .withErrorDescription(ERROR_NO_CONFIRMATION_CHALLENGE)
          .build());

      return;
    }

    final ConfirmationCallback confirmationCallback = (ConfirmationCallback) callbackQueue.peek();
    confirmationCallback.setResultCallbackContext(callbackContext);

    if (shouldAccept) {
      confirmationCallback.getAcceptDenyCallback().acceptAuthenticationRequest();
    } else {
      confirmationCallback.getAcceptDenyCallback().denyAuthenticationRequest();
    }
  }

  @Override
  public void finishAuthentication() {
  }

  @Override
  public void onSuccess() {
    finishAuthenticationRequest(null);
  }

  @Override
  public void onError(final OneginiMobileAuthenticationError oneginiMobileAuthenticationError) {
    finishAuthenticationRequest(oneginiMobileAuthenticationError);
  }

  private void handleNextAuthenticationRequest() {
    final Callback callback = callbackQueue.peek();

    if (!isRunning && callback != null) {
      final CallbackContext callbackContext = getChallengeReceiverForCallbackMethod(callback.getMethod());

      if (callbackContext != null) {
        isRunning = true;
        final OneginiMobileAuthenticationRequest mobileAuthenticationRequest = callback.getMobileAuthenticationRequest();
        final PluginResultBuilder pluginResultBuilder = new PluginResultBuilder();

        pluginResultBuilder
            .withSuccess()
            .shouldKeepCallback()
            .withOneginiMobileAuthenticationRequest(mobileAuthenticationRequest);

        if (callback instanceof PinCallback) {
          final PinCallback pinCallback = (PinCallback) callback;
          pluginResultBuilder
              .withRemainingFailureCount(pinCallback.getAuthenticationAttemptCounter().getRemainingAttempts())
              .withMaxFailureCount(pinCallback.getAuthenticationAttemptCounter().getRemainingAttempts());
        }

        callbackContext.sendPluginResult(pluginResultBuilder.build());
      }
    }
  }

  private void addAuthenticationRequestToQueue(final Callback callback) {
    callbackQueue.add(callback);
    handleNextAuthenticationRequest();
  }

  private void finishAuthenticationRequest(final OneginiError oneginiError) {
    final CallbackContext callbackContext = callbackQueue.poll().getResultCallbackContext();
    final PluginResult pluginResult;

    if (oneginiError == null) {
      pluginResult = new PluginResultBuilder()
          .withSuccess()
          .build();
    } else {
      pluginResult = new PluginResultBuilder()
          .withOneginiError(oneginiError)
          .build();
    }

    callbackContext.sendPluginResult(pluginResult);

    isRunning = false;
    handleNextAuthenticationRequest();
  }

  private CallbackContext getChallengeReceiverForCallbackMethod(final Callback.Method method) {
    return challengeReceivers.get(method);
  }
}

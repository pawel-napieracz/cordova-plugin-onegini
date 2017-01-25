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

package com.onegini.handler;

import static com.onegini.OneginiCordovaPluginConstants.AUTH_EVENT_CREATE_PIN_REQUEST;
import static com.onegini.OneginiCordovaPluginConstants.PIN_LENGTH;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;

import com.onegini.mobile.sdk.android.handlers.error.OneginiPinValidationError;
import com.onegini.mobile.sdk.android.handlers.request.OneginiCreatePinRequestHandler;
import com.onegini.mobile.sdk.android.handlers.request.callback.OneginiPinCallback;
import com.onegini.mobile.sdk.android.model.entity.UserProfile;
import com.onegini.util.PluginResultBuilder;

public class CreatePinRequestHandler implements OneginiCreatePinRequestHandler {

  private static CreatePinRequestHandler instance = null;
  private OneginiPinCallback oneginiPinCallback = null;
  private CallbackContext onStartPinCreationCallback;
  private CallbackContext onNextPinCreationAttemptCallback;

  protected CreatePinRequestHandler() {
  }

  public static CreatePinRequestHandler getInstance() {
    if (instance == null) {
      instance = new CreatePinRequestHandler();
    }

    return instance;
  }

  public OneginiPinCallback getOneginiPinCallback() {
    return oneginiPinCallback;
  }

  public void setOnStartPinCreationCallback(final CallbackContext registrationCallback) {
    this.onStartPinCreationCallback = registrationCallback;
  }

  public void setOnNextPinCreationAttemptCallback(final CallbackContext createPinCallback) {
    this.onNextPinCreationAttemptCallback = createPinCallback;
  }

  @Override
  public void startPinCreation(final UserProfile userProfile, final OneginiPinCallback oneginiPinCallback) {
    this.oneginiPinCallback = oneginiPinCallback;

    PluginResult pluginResult = new PluginResultBuilder()
        .withSuccess()
        .shouldKeepCallback()
        .withAuthenticationEvent(AUTH_EVENT_CREATE_PIN_REQUEST)
        .withPinLength(PIN_LENGTH)
        .withProfileId(userProfile)
        .build();

    sendStartPinCreationResult(pluginResult);
  }

  @Override
  public void onNextPinCreationAttempt(final OneginiPinValidationError oneginiPinValidationError) {
    PluginResult pluginResult = new PluginResultBuilder()
        .withOneginiError(oneginiPinValidationError)
        .withSuccess()
        .shouldKeepCallback()
        .withAuthenticationEvent(AUTH_EVENT_CREATE_PIN_REQUEST)
        .withPinLength(PIN_LENGTH)
        .build();

    sendStartPinCreationResult(pluginResult);
  }

  @Override
  public void finishPinCreation() {
    this.oneginiPinCallback = null;
    this.onStartPinCreationCallback = null;
  }

  private void sendStartPinCreationResult(final PluginResult pluginResult) {
    if (onStartPinCreationCallback != null) {
      onStartPinCreationCallback.sendPluginResult(pluginResult);
    }
  }

  private void sendOnNextPinCreationAttemptResult(final PluginResult pluginResult) {
    if (onNextPinCreationAttemptCallback != null) {
      onNextPinCreationAttemptCallback.sendPluginResult(pluginResult);
    }
  }
}

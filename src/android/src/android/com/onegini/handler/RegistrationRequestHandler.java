/*
 * Copyright (c) 2017 Onegini B.V.
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

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.onegini.mobile.sdk.android.handlers.request.OneginiRegistrationRequestHandler;
import com.onegini.mobile.sdk.android.handlers.request.callback.OneginiRegistrationCallback;

public class RegistrationRequestHandler implements OneginiRegistrationRequestHandler {

  private static RegistrationRequestHandler instance = null;
  private final Context context;
  private OneginiRegistrationCallback callback;

  protected RegistrationRequestHandler(final Context context) {
    this.context = context;
  }

  public static RegistrationRequestHandler getInstance() {
    return getInstance(null);
  }

  public static RegistrationRequestHandler getInstance(final Context context) {
    if (instance == null) {
      instance = new RegistrationRequestHandler(context);
    }

    return instance;
  }

  public void handleRegistrationCallback(final Uri uri) {
    if (callback != null) {
      callback.handleRegistrationCallback(uri);
      callback = null;
    }
  }

  @Override
  public void startRegistration(final Uri uri, final OneginiRegistrationCallback oneginiRegistrationCallback) {
    callback = oneginiRegistrationCallback;

    final Intent intent = new Intent(Intent.ACTION_VIEW, uri);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

    context.startActivity(intent);
  }
}

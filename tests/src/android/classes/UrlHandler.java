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

package com.onegini.tests;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;

import com.onegini.mobile.sdk.android.handlers.OneginiURLHandler;

public class UrlHandler implements OneginiURLHandler {

  private final static String PARAMETER_KEY_USER_ID = "user_id";

  private Context context;

  public void setUserId(final String userId) {
    this.userId = userId;
  }

  private String userId;

  public UrlHandler(final Context context) {
    this.context = context;
  }

  @Override
  public void onOpenURL(Uri uri) {
    if (userId != null) {
      uri = addParameter(uri, PARAMETER_KEY_USER_ID, userId);
    }

    final Intent intent = new Intent(Intent.ACTION_VIEW, uri);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(intent);
  }

  private Uri addParameter(final Uri uri, final String key, final String value) {
    Builder builder = uri.buildUpon();
    builder.appendQueryParameter(key, value);
    return builder.build();
  }
}

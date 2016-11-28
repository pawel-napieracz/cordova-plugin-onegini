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

import android.content.Intent;
import android.net.Uri;

import com.onegini.mobile.sdk.android.handlers.OneginiURLHandler;

public class URLHandler implements OneginiURLHandler {

  private final static String PARAMETER_KEY_USER_ID = "user_id";
  private final static String PARAMETER_DEFAULT_USER_ID = "devnull";

  private String userId;

  public URLHandler(final String userId) {
    if (userId == null || userId.isEmpty()) {
      this.userId = PARAMETER_DEFAULT_USER_ID;
    } else {
      this.userId = userId;
    }
  }

  @Override
  public void onOpenURL(final Uri uri) {
    final Uri newUri = addParameter(uri, PARAMETER_KEY_USER_ID, userId);
    final Intent intent = new Intent(Intent.ACTION_VIEW, uri);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(intent);
  }

  private Uri addParameter(final Uri uri, final String key, final String value) {
    String newQuery = uri.getQuery();
    if (newQuery == null) {
      newQuery = key + "=" + value;
    } else {
      newQuery += "&" + key + "=" + value;
    }
    return new Uri(uri.getScheme(), uri.getAuthority(), uri.getPath(), newQuery, uri.getFragment());
  }
}

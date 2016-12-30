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

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;

import com.onegini.OneginiSDK;
import com.onegini.util.PluginResultBuilder;

import com.onegini.tests.UrlHandler;

public class OneginiUrlClient extends CordovaPlugin {

  private static final String ACTION_SET_USER_ID = "setUserId";
  private static final String PARAM_USER_ID = "userId";

  private UrlHandler urlHandler;

  @Override
  protected void pluginInitialize() {
    final Context context = cordova.getActivity().getApplicationContext();
    urlHandler = new UrlHandler(context);

    cordova.getThreadPool().execute(new Runnable() {
      @Override
      public void run() {
        OneginiSDK.getInstance().setUrlHandler(context, urlHandler);
      }
    });

    super.pluginInitialize();
  }

  @Override
  public boolean execute(final String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {
    if (ACTION_SET_USER_ID.equals(action)) {
      setUserId(args, callbackContext);
      return true;
    }

    return false;
  }

  private void setUserId(final JSONArray args, final CallbackContext callbackContext) throws JSONException {
    final String userId = args.getJSONObject(0).getString(PARAM_USER_ID);
    urlHandler.setUserId(userId);

    callbackContext.sendPluginResult(new PluginResultBuilder()
        .withSuccess()
        .build());
  }
}

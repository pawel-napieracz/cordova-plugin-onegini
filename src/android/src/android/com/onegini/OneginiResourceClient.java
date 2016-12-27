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

package com.onegini;

import static com.onegini.OneginiCordovaPluginConstants.ERROR_CODE_ILLEGAL_ARGUMENT;
import static com.onegini.OneginiCordovaPluginConstants.ERROR_CODE_IO_EXCEPTION;

import java.io.IOException;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import com.onegini.util.ActionArgumentsUtil;
import com.onegini.util.PluginResultBuilder;
import retrofit.client.OkClient;
import retrofit.client.Request;
import retrofit.client.Response;

public class OneginiResourceClient extends CordovaPlugin {

  private static final String ACTION_FETCH = "fetch";

  @Override
  public boolean execute(final String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {
    if (ACTION_FETCH.equals(action)) {
      fetch(args, callbackContext);
      return true;
    }

    return false;
  }

  private void fetch(final JSONArray args, final CallbackContext callbackContext) throws JSONException {
    final Request request;
    final boolean isAnonymous = ActionArgumentsUtil.isFetchAnonymous(args);

    try {
      request = ActionArgumentsUtil.getRequestFromArguments(args);
    } catch (IllegalArgumentException e) {
      callbackContext.sendPluginResult(new PluginResultBuilder()
          .withPluginError(e.getMessage(), ERROR_CODE_ILLEGAL_ARGUMENT)
          .build());

      return;
    }

    cordova.getThreadPool().execute(new Runnable() {
      @Override
      public void run() {
        final OkClient okClient;
        final Response response;

        if (isAnonymous) {
          okClient = getOneginiClient().getDeviceClient().getAnonymousResourceRetrofitClient();
        } else {
          okClient = getOneginiClient().getUserClient().getResourceRetrofitClient();
        }

        try {
          response = okClient.execute(request);
        } catch (IOException e) {
          callbackContext.sendPluginResult(new PluginResultBuilder()
              .withPluginError(e.getMessage(), ERROR_CODE_IO_EXCEPTION)
              .build());
          return;
        }

        callbackContext.sendPluginResult(new PluginResultBuilder()
            .withRetrofitResponse(response)
            .build());
      }
    });
  }

  private com.onegini.mobile.sdk.android.client.OneginiClient getOneginiClient() {
    return OneginiSDK.getInstance().getOneginiClient(cordova.getActivity().getApplicationContext());
  }
}

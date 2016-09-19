package com.onegini;

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
          .withErrorDescription(e.getMessage())
          .build());

      return;
    }

    cordova.getThreadPool().execute(new Runnable() {
      @Override
      public void run() {
        final OkClient okClient;
        final Response response;

        if (isAnonymous) {
          okClient = getOneginiClient().getDeviceClient().getRetrofitClient();
        } else {
          okClient = getOneginiClient().getUserClient().getResourceRetrofitClient();
        }

        try {
          response = okClient.execute(request);
        } catch (IOException e) {
          callbackContext.sendPluginResult(new PluginResultBuilder()
              .withErrorDescription(e.getMessage())
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
    return OneginiSDK.getOneginiClient(cordova.getActivity().getApplicationContext());
  }
}

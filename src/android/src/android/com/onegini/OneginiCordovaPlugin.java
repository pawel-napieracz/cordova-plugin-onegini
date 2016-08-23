package com.onegini;

import static com.onegini.OneginiConstants.ACTION_START;

import java.util.HashMap;
import java.util.Map;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;

import com.onegini.action.OneginiPluginAction;
import com.onegini.action.StartAction;
import com.onegini.mobile.android.sdk.client.OneginiClient;

public class OneginiCordovaPlugin extends CordovaPlugin {

  private static Map<String, Class<? extends OneginiPluginAction>> actions = new HashMap<String, Class<? extends OneginiPluginAction>>();
  private static OneginiClient oneginiClient;


  @Override
  public void initialize(final CordovaInterface cordova, final CordovaWebView webView) {
    super.initialize(cordova, webView);

    mapActions();

    oneginiClient = OneginiSDK.getOneginiClient(cordova.getActivity().getApplicationContext());
  }

  public CordovaInterface getCordova() {
    return cordova;
  }

  public OneginiClient getOneginiClient() {
    return oneginiClient;
  }

  @Override
  public boolean execute(final String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {
    if (actions.containsKey(action)) {
      final OneginiPluginAction actionInstance = buildActionClassFor(action);
      if (actionInstance == null) {
        callbackContext.error("Failed to create action class for \"" + action + "\"");
        return false;
      }
      actionInstance.execute(args, callbackContext, this);
      return true;
    }
    callbackContext.error("OneginiCordovaPlugin: \"" + action + "\" is not supported");
    return false;
  }

  private void mapActions() {
    actions.put(ACTION_START, StartAction.class);
  }

  private OneginiPluginAction buildActionClassFor(final String action) {
    Class<? extends OneginiPluginAction> actionClass = actions.get(action);
    try {
      return actionClass.newInstance();
    } catch (Exception e) {
    }
    return null;
  }
}

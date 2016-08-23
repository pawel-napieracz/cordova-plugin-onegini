package com.onegini.action;

import org.apache.cordova.CallbackContext;
import org.json.JSONArray;

import com.onegini.OneginiCordovaPlugin;
import com.onegini.OneginiSDK;
import com.onegini.handler.InitializationHandler;

public class StartAction implements OneginiPluginAction {

  @Override
  public void execute(final JSONArray args, final CallbackContext callbackContext, final OneginiCordovaPlugin client) {
    OneginiSDK.getOneginiClient(client.getCordova().getActivity().getApplicationContext());
    client.getOneginiClient().start(new InitializationHandler(callbackContext));
  }
}

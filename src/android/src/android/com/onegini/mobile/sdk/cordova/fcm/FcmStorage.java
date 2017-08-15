package com.onegini.mobile.sdk.cordova.fcm;

import android.content.Context;
import android.content.SharedPreferences;

public class FcmStorage {

  private static final String FILENAME = "fcm_shared_preference";
  private static final String KEY_REGISTRATION_TOKEN = "registration_token";

  private final SharedPreferences sharedPreferences;
  private final SharedPreferences.Editor editor;

  public FcmStorage(final Context context) {
    sharedPreferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
    editor = sharedPreferences.edit();
  }

  public void setRegistrationToken(final String token) {
    editor.putString(KEY_REGISTRATION_TOKEN, token);
  }

  public String getRegistrationToken() {
    return sharedPreferences.getString(KEY_REGISTRATION_TOKEN, "");
  }

  public void save() {
    editor.apply();
  }
}

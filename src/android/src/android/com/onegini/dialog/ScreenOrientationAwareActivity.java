package com.onegini.dialog;

import org.apache.cordova.CordovaActivity;

import android.content.pm.ActivityInfo;
import com.onegini.util.DeviceUtil;

public class ScreenOrientationAwareActivity extends CordovaActivity {

  protected void lockScreenOrientation() {
    if (!DeviceUtil.isTablet(this)) {
      setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
  }
}

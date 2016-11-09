package com.onegini.util;

import static com.onegini.OneginiCordovaPluginConstants.PARAM_PROFILE_ID;

import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.support.annotation.Nullable;
import com.onegini.mobile.sdk.android.model.entity.UserProfile;

public class UserProfileUtil {

  @Nullable
  public static UserProfile findUserProfileById(final String id, final Set<UserProfile> userProfiles) {
    for (UserProfile userProfile : userProfiles) {
      if (id.equals(userProfile.getProfileId())) {
        return userProfile;
      }
    }

    return null;
  }

  public static JSONArray profileSetToJSONArray(final Set<UserProfile> userProfileSet) throws JSONException {
    JSONArray userProfileJSONArray = new JSONArray();
    for (final UserProfile userProfile : userProfileSet) {
      final JSONObject userProfileJSON = new JSONObject();
      userProfileJSON.put(PARAM_PROFILE_ID, userProfile.getProfileId());
      userProfileJSONArray.put(userProfileJSON);
    }

    return userProfileJSONArray;
  }
}

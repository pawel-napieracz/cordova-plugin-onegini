package com.onegini.util;

import java.util.Set;

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

}

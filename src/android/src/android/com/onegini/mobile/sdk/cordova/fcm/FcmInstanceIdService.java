/*
 * Copyright (c) 2017-2018 Onegini B.V.
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

package com.onegini.mobile.sdk.cordova.fcm;

import android.content.Intent;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class FcmInstanceIdService extends FirebaseInstanceIdService {

  /**
   * Called if InstanceID token is created or updated. This may occur if the security of the previous token had been compromised.
   * This call is initiated by the InstanceID provider.
   */
  @Override
  public void onTokenRefresh() {
    final Intent intent = new Intent(getApplicationContext(), FcmTokenUpdateService.class);
    getApplicationContext().startService(intent);
  }
}

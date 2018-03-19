/*
 * Copyright (c) 2018 Onegini B.V.
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

package com.onegini.mobile.sdk.cordova.customregistration;

import com.onegini.mobile.sdk.android.handlers.action.OneginiCustomRegistrationAction;
import com.onegini.mobile.sdk.android.model.OneginiCustomIdentityProvider;

public class CustomIdentityProvider implements OneginiCustomIdentityProvider {

  private final String id;
  private OneginiCustomRegistrationAction customRegistrationAction;

  public CustomIdentityProvider(final String id, final String flowType) {
    this.id = id;
    if (isTwoStep(flowType)) {
      customRegistrationAction = new CustomTwoStepRegistrationAction(id);
    } else {
      customRegistrationAction = new CustomOneStepRegistrationAction(id);
    }
  }

  private boolean isTwoStep(final String flowType) {
    return "TWO_STEP".equalsIgnoreCase(flowType);
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public OneginiCustomRegistrationAction getRegistrationAction() {
    return customRegistrationAction;
  }

  public CustomRegistrationAction getCustomRegistrationAction() {
    return (CustomRegistrationAction) customRegistrationAction;
  }
}

//  Copyright © 2016 Onegini. All rights reserved.

#import "OGCDVUserClientHelper.h"

@implementation OGCDVUserClientHelper

+ (ONGUserProfile*) getRegisteredUserProfile:(NSString*)profileId
{
  NSArray<ONGUserProfile *> *profiles = [[ONGUserClient sharedInstance] userProfiles].allObjects;
  for (ONGUserProfile *profile in profiles) {
    if ([profile.profileId isEqualToString:profileId]) {
      return profile;
    }
  }
  return nil;
}

@end

//
//  MessagesModel.h
//  OneginiCordovaPlugin
//
//  Created by Stanisław Brzeski on 27/01/16.
//  Copyright © 2016 Onegini. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface MessagesModel : NSObject

+(MessagesModel*)sharedInstance;
+(NSString*)messageForKey:(NSString*)key;

@end

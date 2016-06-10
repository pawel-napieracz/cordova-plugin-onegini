//
//  UIColor+OGNHex.h
//  OneginiCordovaPlugin
//
//  Created by Stanisław Brzeski on 02/06/16.
//  Copyright © 2016 Onegini. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface UIColor (OGNHex)

+ (UIColor *)ogn_colorWithHexString:(NSString *)hexString;
    
@end

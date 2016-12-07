//
//  OGNColorFileParser.h
//  OneginiCordovaPlugin
//
//  Created by Stanisław Brzeski on 02/06/16.
//  Copyright © 2016 Onegini. All rights reserved.
//

#import <Foundation/Foundation.h>

extern NSString *const kOGNPopupHeaderBackground;

extern NSString *const kOGNPopupHeaderText;

extern NSString *const kOGNPopupBodyText;

extern NSString *const kOGNPopupBodyBackground;

extern NSString *const kOGNPopupButtonText;

extern NSString *const kOGNPopupButtonBackground;

extern NSString *const kOGNPinscreenTitle;

extern NSString *const kOGNPinscreenHeaderBackground;

extern NSString *const kOGNPinscreenHeaderHelpLabelText;

extern NSString *const kOGNPinscreenBackground;

extern NSString *const kOGNPinKeyboardBackground;

extern NSString *const kOGNPinKeyboardButtonBackground;

extern NSString *const kOGNPinKeyboardButtonText;

extern NSString *const kOGNPinscreenForgottenLabel;

extern NSString *const kOGNPinscreenErrorText;

@interface OGNColorFileParser : NSObject

+ (UIColor *)colorForKey:(NSString *)key;

@end

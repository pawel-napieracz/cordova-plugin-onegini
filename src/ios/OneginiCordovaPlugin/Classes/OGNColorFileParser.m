//
//  OGNColorFileParser.m
//  OneginiCordovaPlugin
//
//  Created by Stanisław Brzeski on 02/06/16.
//  Copyright © 2016 Onegini. All rights reserved.
//

#import "OGNColorFileParser.h"
#import "UIColor+OGNHex.h"

NSString * const kOGNPopupHeaderBackground = @"popup_header_background";
NSString * const kOGNPopupHeaderText = @"popup_header_text";
NSString * const kOGNPopupBodyText = @"popup_body_text";
NSString * const kOGNPopupBodyBackground = @"popup_body_background";
NSString * const kOGNPopupButtonText = @"popup_button_text";
NSString * const kOGNPopupButtonBackground = @"popup_button_background";

NSString * const kOGNPinscreenTitle = @"pinscreen_title";
NSString * const kOGNPinscreenHeaderBackground = @"pinscreen_header_background";
NSString * const kOGNPinscreenHeaderHelpLabelText = @"pinscreen_header_help_label_text";
NSString * const kOGNPinscreenBackground = @"pinscreen_background";
NSString * const kOGNPinKeyboardBackground = @"pin_keyboard_background";
NSString * const kOGNPinKeyboardButtonBackground = @"pin_keyboard_button_background";
NSString * const kOGNPinKeyboardButtonText = @"pin_keyboard_button_text";
NSString * const kOGNPinscreenForgottenLabel = @"pinscreen_forgotten_label";

@interface OGNColorFileParser() <NSXMLParserDelegate>

@property (nonatomic) NSMutableDictionary<NSString*,UIColor*> *parsedColors;
@property (nonatomic, copy) NSString* currentKey;

@end

@implementation OGNColorFileParser

+ (OGNColorFileParser *)sharedInstance {
    static OGNColorFileParser* singleton;
    
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        singleton = [[self alloc] init];
        singleton.parsedColors = [NSMutableDictionary dictionary];
        [singleton loadColorsFromFile:@"colors.xml"];
    });
    
    return singleton;
}

+(UIColor *)colorForKey:(NSString *)key{
    return [[OGNColorFileParser sharedInstance].parsedColors objectForKey:key];
}

-(NSDictionary*)loadColorsFromFile:(NSString*)colorFile{
    NSError *error = nil;
    NSData *data = [NSData dataWithContentsOfFile:[[NSBundle mainBundle] pathForResource:colorFile ofType:nil]
                                          options:NSDataReadingUncached
                                            error:&error];
    if(error) return nil;
    NSXMLParser *parser = [[NSXMLParser alloc] initWithData:data];
    parser.delegate = self;
    
    [parser parse];
    error = [parser parserError];
    if(error) return nil;
    
    return self.parsedColors;
}

- (void)parser:(NSXMLParser *)parser didStartElement:(NSString *)elementName namespaceURI:(NSString *)namespaceURI qualifiedName:(NSString *)qName attributes:(NSDictionary *)attributeDict {
    if ([elementName isEqualToString:@"color"]){
        self.currentKey = [attributeDict objectForKey:@"name"];
    }
}

-(void)parser:(NSXMLParser *)parser foundCharacters:(NSString *)string{
    if (self.currentKey){
        UIColor *color = [UIColor ogn_colorWithHexString:string];
        [self.parsedColors setObject:color forKey:self.currentKey];
        self.currentKey = nil;
    }
}

@end

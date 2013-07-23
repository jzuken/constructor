//
//  UIColor+hex.m
//  QRealWeb_iOSApp
//
//  Created by Ivan Afanasiev on 7/17/13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import "UIColor+hex.h"

@implementation UIColor (hex)

+ (UIColor *)colorFromHex: (NSInteger) hexValue
{
   return [UIColor colorWithRed:((float)((hexValue & 0xFF0000) >> 16))/255.0 green:((float)((hexValue & 0xFF00) >> 8))/255.0 blue:((float)(hexValue & 0xFF))/255.0 alpha:1.0];
}

@end

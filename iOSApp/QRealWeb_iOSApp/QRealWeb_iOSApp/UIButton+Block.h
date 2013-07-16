//
//  UIButton+Block.h
//  QRealWebDemoProject
//
//  Created by Ivan Afanasiev on 7/8/13.
//  Copyright (c) 2013 Иван Афанасьев. All rights reserved.
//

#import <UIKit/UIKit.h>


#define kUIButtonBlockTouchUpInside @"TouchInside"


@interface UIButton (Block)

@property (nonatomic, strong) NSMutableDictionary *actions;

- (void) setAction:(NSString*)action withBlock:(void(^)())block;

@end

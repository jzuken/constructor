//
//  QRWTopCategories.h
//  QRealWeb_iOSApp
//
//  Created by Ivan Afanasiev on 7/31/13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "QRWCategoryInTop.h"

@interface QRWTopCategories : NSObject

@property (nonatomic, strong) NSArray *lastLoginTopArray;
@property (nonatomic, strong) NSArray *todayTopArray;
@property (nonatomic, strong) NSArray *weekTopArray;
@property (nonatomic, strong) NSArray *monthTopArray;

@end

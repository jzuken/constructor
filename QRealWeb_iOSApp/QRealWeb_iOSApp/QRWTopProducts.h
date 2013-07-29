//
//  QRWTopProducts.h
//  QRealWeb_iOSApp
//
//  Created by Ivan Afanasiev on 7/29/13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface QRWProductInTopArray : NSObject

@property (nonatomic, strong) NSNumber *productid;
@property (nonatomic, strong) NSNumber *productcode;
@property (nonatomic, strong) NSNumber *count;
@property (nonatomic, strong) NSString *product;


@end



@interface QRWTopProducts : NSObject

@property (nonatomic, strong) NSArray *lastLoginTopArray;
@property (nonatomic, strong) NSArray *todayTopArray;
@property (nonatomic, strong) NSArray *weekTopArray;
@property (nonatomic, strong) NSArray *monthTopArray;

@end

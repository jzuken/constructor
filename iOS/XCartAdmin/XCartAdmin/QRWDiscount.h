//
//  QRWDiscount.h
//  QRealWeb_iOSApp
//
//  Created by Ivan Afanasiev on 8/5/13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface QRWDiscount : NSObject

@property (nonatomic, strong) NSNumber *discountid;
@property (nonatomic, strong) NSNumber *minprice;
@property (nonatomic, strong) NSString *discountType;
@property (nonatomic, strong) NSNumber *discount;
@property (nonatomic, strong) NSNumber *membershipid;
@property (nonatomic, strong) NSNumber *provider;

@end

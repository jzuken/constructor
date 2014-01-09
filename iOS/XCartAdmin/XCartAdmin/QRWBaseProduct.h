//
//  QRWBaseProduct.h
//  XCartAdmin
//
//  Created by Ivan Afanasiev on 09/01/14.
//  Copyright (c) 2014 Ivan Afanasiev. All rights reserved.
//

#import "QRWBaseEntety.h"

@interface QRWBaseProduct : QRWBaseEntety

@property (nonatomic, strong) NSNumber *productid;
@property (nonatomic, strong) NSString *productcode;
@property (nonatomic, strong) NSNumber *price;
@property (nonatomic, strong) NSString *product;

@end

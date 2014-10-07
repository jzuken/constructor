//
//  QRWProductWithInfo.h
//  XCartAdmin
//
//  Created by Ivan Afanasiev on 10/01/14.
//  Copyright (c) 2014 Ivan Afanasiev. All rights reserved.
//

#import "QRWBaseProduct.h"

@interface QRWProductWithInfo : QRWBaseProduct


@property (nonatomic, copy) NSString *productDescription;
@property (nonatomic, copy) NSString *fullDescription;
@property (nonatomic, copy) NSString *imageURL;
@property (nonatomic, strong) NSNumber *available;
@property (nonatomic, copy) NSString *forSale;
@property (nonatomic, copy) NSArray *variants;

@end


@interface QRWProductVariant : QRWBaseEntety

@property (nonatomic, copy) NSString *SKUOfVariant;
@property (nonatomic, copy) NSArray *options;

@end


@interface QRWProductVariantOption : QRWBaseEntety

@property (nonatomic, copy) NSString *optionName;
@property (nonatomic, copy) NSString *optionValue;

@end
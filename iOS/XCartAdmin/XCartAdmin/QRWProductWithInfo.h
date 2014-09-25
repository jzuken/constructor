//
//  QRWProductWithInfo.h
//  XCartAdmin
//
//  Created by Ivan Afanasiev on 10/01/14.
//  Copyright (c) 2014 Ivan Afanasiev. All rights reserved.
//

#import "QRWBaseProduct.h"

@interface QRWProductWithInfo : QRWBaseProduct


@property (nonatomic, strong) NSString *productDescription;
@property (nonatomic, strong) NSString *fullDescription;
@property (nonatomic, strong) NSString *imageURL;
@property (nonatomic, strong) NSNumber *available;
@property (nonatomic, strong) NSString *forSale;

@end

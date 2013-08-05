//
//  QRWProductInOrder.h
//  QRealWeb_iOSApp
//
//  Created by Ivan Afanasiev on 8/1/13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import "QRWProductInTop.h"

@interface QRWProductInOrder : QRWProductInTop

@property (nonatomic, strong) NSNumber *price;
@property (nonatomic, strong) NSString *provider;
@property (nonatomic, strong) NSNumber *itemid;
@property (nonatomic, strong) NSString *productOptions;


@end

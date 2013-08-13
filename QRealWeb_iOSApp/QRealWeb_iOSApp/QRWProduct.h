//
//  QRWProduct.h
//  QRealWeb_iOSApp
//
//  Created by Ivan Afanasiev on 8/13/13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "QRWProductInOrder.h"

@interface QRWProduct : QRWProductInOrder

@property (nonatomic, strong) NSNumber *avaliable;
@property (nonatomic, strong) NSString *freeShiping;

@end

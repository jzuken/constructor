//
//  QRWLastOrder.h
//  QRealWeb_iOSApp
//
//  Created by Ivan Afanasiev on 7/23/13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "QRWProductInOrder.h"


@interface QRWLastOrder : NSObject

@property (nonatomic, strong) NSNumber *orderid;
@property (nonatomic, strong) NSNumber *userid;
@property (nonatomic, strong) NSString *date;
@property (nonatomic, strong) NSString *firstname;
@property (nonatomic, strong) NSString *lastname;
@property (nonatomic, strong) NSString *email;
@property (nonatomic, strong) NSString *status;
@property (nonatomic, strong) NSString *title;
@property (nonatomic, strong) NSNumber *total;
@property (nonatomic, strong) NSArray *products;

@end

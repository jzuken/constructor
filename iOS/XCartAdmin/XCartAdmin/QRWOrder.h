//
//  QRWLastOrder.h
//  QRealWeb_iOSApp
//
//  Created by Ivan Afanasiev on 7/23/13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import "QRWBaseEntety.h"


@interface QRWOrder : QRWBaseEntety

@property (nonatomic, strong) NSNumber *orderid;
@property (nonatomic, strong) NSNumber *date;
@property (nonatomic, strong) NSString *firstname;
@property (nonatomic, strong) NSString *lastname;
@property (nonatomic, strong) NSString *status;
@property (nonatomic, strong) NSString *title;
@property (nonatomic, strong) NSNumber *total;
@property (nonatomic, strong) NSNumber *items;
@property (nonatomic, strong) NSString *day;
@property (nonatomic, strong) NSString *month;

@end

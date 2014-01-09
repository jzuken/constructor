//
//  QRWDashboardEntety.h
//  XCartAdmin
//
//  Created by Ivan Afanasiev on 24/12/13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import "QRWBaseEntety.h"

@interface QRWDashboardEntety : QRWBaseEntety

@property (nonatomic, strong) NSNumber *todaySales;
@property (nonatomic, strong) NSNumber *lowStock;
@property (nonatomic, strong) NSNumber *todayVisitors;
@property (nonatomic, strong) NSNumber *todaySold;
@property (nonatomic, strong) NSNumber *reviewsToday;
@property (nonatomic, strong) NSArray *todayOrders;
@property (nonatomic, strong) NSNumber *todayOrdersCount;


@end

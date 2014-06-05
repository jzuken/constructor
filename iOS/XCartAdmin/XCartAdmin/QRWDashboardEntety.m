//
//  QRWDashboardEntety.m
//  XCartAdmin
//
//  Created by Ivan Afanasiev on 24/12/13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import "QRWDashboardEntety.h"

@implementation QRWDashboardEntety

- (void) buildDataByJson:(NSDictionary *)JSON
{
    [super buildDataByJson:JSON];
    
    _todaySales = [JSON objectForKey:@"today_sales"];
    _lowStock = [JSON objectForKey:@"low_stock"];
    _todayVisitors = [JSON objectForKey:@"today_visitors"];
    _todaySold = [JSON objectForKey:@"today_sold"];
    _reviewsToday = [JSON objectForKey:@"reviews_today"];
    _todayOrdersCount = [JSON objectForKey:@"today_orders_count"];
    
    NSArray *items = [JSON objectForKey:@"today_orders"];
    NSMutableArray *itemsSet = [NSMutableArray new];
    
    for (NSDictionary *itemDict in items) {
        QRWOrder *item = [QRWOrder new];
        [item buildDataByJson:itemDict];
        [itemsSet addObject:item];
    }
    
    _todayOrders = itemsSet;
    
}




@end

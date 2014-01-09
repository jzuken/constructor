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
//    [jsonData objectForKey:@"today_orders"];
    _todayOrdersCount = [JSON objectForKey:@"today_orders_count"];
    
}




@end

//
//  QRWLastOrder.m
//  QRealWeb_iOSApp
//
//  Created by Ivan Afanasiev on 7/23/13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import "QRWOrder.h"

@implementation QRWOrder


- (void)buildDataByJson:(NSDictionary *)JSON
{
    [super buildDataByJson:JSON];
    
    self.title = [JSON objectForKey:@"title"];
    self.firstname = [JSON objectForKey:@"firstname"];
    self.lastname = [JSON objectForKey:@"lastname"];
    self.day = [JSON objectForKey:@"day"];
    self.month = [JSON objectForKey:@"month"];
    self.status = [JSON objectForKey:@"status"];
    
    self.total = [self.formatter numberFromString: (NSString *)[JSON objectForKey:@"total"]];
    self.orderid = [self.formatter numberFromString: (NSString *)[JSON objectForKey:@"orderid"]];
    self.items = [self.formatter numberFromString: (NSString *)[JSON objectForKey:@"items"]];
    self.date = [self.formatter numberFromString: (NSString *)[JSON objectForKey:@"date"]];
}



@end

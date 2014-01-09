//
//  QRWUser.m
//  QRealWeb_iOSApp
//
//  Created by Ivan Afanasiev on 8/2/13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import "QRWUser.h"

@implementation QRWUser


- (void)buildDataByJson:(NSDictionary *)JSON
{
    [super buildDataByJson:JSON];
    
    self.login = [JSON objectForKey:@"login"];
    self.username = [JSON objectForKey:@"username"];
    self.usertype = [JSON objectForKey:@"usertype"];
    self.title = [JSON objectForKey:@"title"];
    self.firstname = [JSON objectForKey:@"firstname"];
    self.lastname = [JSON objectForKey:@"lastname"];
    self.email = [JSON objectForKey:@"email"];
    self.lastLogin = [JSON objectForKey:@"last_login"];
    self.phone = [JSON objectForKey:@"phone"];
    self.ordersCount = [JSON objectForKey:@"total_orders"];
    
    self.userID = [self.formatter numberFromString: (NSString *)[JSON objectForKey:@"id"]];
}



@end

//
//  QRWUserInfo.m
//  XCartAdmin
//
//  Created by Иван Афанасьев on 11.01.14.
//  Copyright (c) 2014 Ivan Afanasiev. All rights reserved.
//

#import "QRWUserInfo.h"

@implementation QRWUserInfo

- (void)buildDataByJson:(NSDictionary *)JSON
{
    [super buildDataByJson:JSON];
    
    NSDictionary *address = [(NSDictionary *)[JSON objectForKey:@"address"] objectForKey:@"S"];
    self.address = [NSString stringWithFormat:@"%@ \n %@ %@ %@\n %@",
                    [self elementOfAddreess:address byKey:@"address"],
                    [self elementOfAddreess:address byKey:@"city"],
                    [self elementOfAddreess:address byKey:@"state"],
                    [self elementOfAddreess:address byKey:@"zipcode"],
                    [self elementOfAddreess:address byKey:@"country"]];
    
    self.phone = [address objectForKey:@"phone"];
    self.fax = [address objectForKey:@"fax"];

}

- (NSString *) elementOfAddreess: (NSDictionary *)address byKey: (NSString *)key
{
    return [address objectForKey:key] ? [address objectForKey:key]:@"";
}

@end

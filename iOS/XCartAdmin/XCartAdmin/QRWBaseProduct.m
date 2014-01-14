//
//  QRWBaseProduct.m
//  XCartAdmin
//
//  Created by Ivan Afanasiev on 09/01/14.
//  Copyright (c) 2014 Ivan Afanasiev. All rights reserved.
//

#import "QRWBaseProduct.h"

@implementation QRWBaseProduct

- (void)buildDataByJson:(NSDictionary *)JSON
{
    [super buildDataByJson:JSON];
    
    self.productcode = [JSON objectForKey:@"productcode"];
    self.product = [JSON objectForKey:@"product"];
    self.price = [JSON objectForKey:@"price"];
}

@end

//
//  QRWProductInTop.m
//  QRealWeb_iOSApp
//
//  Created by Ivan Afanasiev on 7/31/13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import "QRWProduct.h"

@implementation QRWProduct


- (void)buildDataByJson:(NSDictionary *)JSON
{
    [super buildDataByJson:JSON];
    
    self.available = [JSON objectForKey:@"avail"];
    self.productid = [JSON objectForKey:@"productid"];
    
}


@end

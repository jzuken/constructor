//
//  QRWReview.m
//  QRealWeb_iOSApp
//
//  Created by Иван Афанасьев on 06.08.13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import "QRWReview.h"

@implementation QRWReview


- (void)buildDataByJson:(NSDictionary *)JSON
{
    [super buildDataByJson:JSON];
    
    self.email = [JSON objectForKey:@"email"];
    self.product = [JSON objectForKey:@"product"];
    self.message = [JSON objectForKey:@"message"];
    self.reviewID = [JSON objectForKey:@"review_id"];
    self.productID = [JSON objectForKey:@"productid"];
}

@end

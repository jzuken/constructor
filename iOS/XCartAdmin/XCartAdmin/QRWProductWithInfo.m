//
//  QRWProductWithInfo.m
//  XCartAdmin
//
//  Created by Ivan Afanasiev on 10/01/14.
//  Copyright (c) 2014 Ivan Afanasiev. All rights reserved.
//

#import "QRWProductWithInfo.h"

@implementation QRWProductWithInfo


- (void)buildDataByJson:(NSDictionary *)JSON
{
    [super buildDataByJson:JSON];
    
    self.description = [JSON objectForKey:@"descr"];
    self.fullDescription = [JSON objectForKey:@"fulldescr"];
    self.imageURL = [JSON objectForKey:@"image_url"];
    self.productid = [NSNumber numberWithLong:(long )[JSON objectForKey:@"productid"]];
    self.available = [self.formatter numberFromString:(NSString *)[JSON objectForKey:@"avail"]];
}

@end

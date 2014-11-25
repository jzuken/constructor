//
//  NSDictionary+QRWSwap.m
//  XCartAdmin
//
//  Created by Ivan Afanasyev on 26.11.14.
//  Copyright (c) 2014 Ivan Afanasiev. All rights reserved.
//

#import "NSDictionary+QRWSwap.h"

@implementation NSDictionary (QRWSwap)

- (NSDictionary *)qrw_swapKeyValue
{
    NSMutableDictionary *dict = [self mutableCopy];
    
    for (NSString *key in [dict allKeys]) {
        dict[dict[key]] = key;
        [dict removeObjectForKey:key];
    }
    
    return dict;
}


@end

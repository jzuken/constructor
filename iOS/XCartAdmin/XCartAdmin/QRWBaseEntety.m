//
//  QRWBaseEntety.m
//  XCartAdmin
//
//  Created by Ivan Afanasiev on 24/12/13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import "QRWBaseEntety.h"

@implementation QRWBaseEntety


- (void) buildDataByJson:(NSDictionary *)JSON
{
    _JSONdata = JSON;
    
    self.formatter = [[NSNumberFormatter alloc] init];
    [self.formatter setNumberStyle:NSNumberFormatterDecimalStyle];
}


@end

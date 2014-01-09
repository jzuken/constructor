//
//  QRWBaseEntety.h
//  XCartAdmin
//
//  Created by Ivan Afanasiev on 24/12/13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import <Foundation/Foundation.h>


@protocol  QRWBaseEntetyProtocol

@required

- (void) buildDataByJson:(NSDictionary *)JSON;

@end


@interface QRWBaseEntety : NSObject<QRWBaseEntetyProtocol>

@property (nonatomic, strong) NSDictionary *JSONdata;
@property (nonatomic, strong) NSNumberFormatter *formatter;



@end

//
//  QRWReview.h
//  QRealWeb_iOSApp
//
//  Created by Иван Афанасьев on 06.08.13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface QRWReview : NSObject


@property (nonatomic, strong) NSNumber *review_id;
@property (nonatomic, strong) NSString *email;
@property (nonatomic, strong) NSString *message;
@property (nonatomic, strong) NSString *product;

@end

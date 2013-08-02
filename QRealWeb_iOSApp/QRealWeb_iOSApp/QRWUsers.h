//
//  QRWUsers.h
//  QRealWeb_iOSApp
//
//  Created by Ivan Afanasiev on 8/2/13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "QRWUser.h"

@interface QRWUsers : NSObject


@property (nonatomic, strong) NSArray *users;
@property (nonatomic, strong) NSNumber *registered;
@property (nonatomic, strong) NSNumber *nonregistered;
@property BOOL isFirstLoad;


@end

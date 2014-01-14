//
//  QRWUserInfo.h
//  XCartAdmin
//
//  Created by Иван Афанасьев on 11.01.14.
//  Copyright (c) 2014 Ivan Afanasiev. All rights reserved.
//

#import "QRWUser.h"

@interface QRWUserInfo : QRWUser

@property (nonatomic, strong) NSString *address;
@property (nonatomic, strong) NSString *fax;


@property (nonatomic, strong) NSString *invalidLoginAttempts;
@property (nonatomic, strong) NSString *company;
@property (nonatomic, strong) NSString *url;
@property (nonatomic, strong) NSString *firstLogin;
@property (nonatomic, strong) NSString *status;
@property (nonatomic, strong) NSString *language;
@property (nonatomic, strong) NSString *activity;
@property (nonatomic, strong) NSString *trustedProvider;

@end

//
//  QRWUser.h
//  QRealWeb_iOSApp
//
//  Created by Ivan Afanasiev on 8/2/13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface QRWUser : NSObject


@property (nonatomic, strong) NSString *login;
@property (nonatomic, strong) NSString *username;
@property (nonatomic, strong) NSString *usertype;
@property (nonatomic, strong) NSString *invalidLoginAttempts;
@property (nonatomic, strong) NSString *title;
@property (nonatomic, strong) NSString *firstname;
@property (nonatomic, strong) NSString *lastname;
@property (nonatomic, strong) NSString *company;
@property (nonatomic, strong) NSString *email;
@property (nonatomic, strong) NSString *url;
@property (nonatomic, strong) NSString *lastLogin;
@property (nonatomic, strong) NSString *firstLogin;
@property (nonatomic, strong) NSString *status;
@property (nonatomic, strong) NSString *language;
@property (nonatomic, strong) NSString *activity;
@property (nonatomic, strong) NSString *trustedProvider;
@property (nonatomic, strong) NSNumber *ordersCount;



@end

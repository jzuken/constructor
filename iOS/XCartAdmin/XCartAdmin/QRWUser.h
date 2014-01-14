//
//  QRWUser.h
//  QRealWeb_iOSApp
//
//  Created by Ivan Afanasiev on 8/2/13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import "QRWBaseEntety.h"

@interface QRWUser : QRWBaseEntety


@property (nonatomic, strong) NSNumber *userID;
@property (nonatomic, strong) NSString *login;
@property (nonatomic, strong) NSString *username;
@property (nonatomic, strong) NSString *usertype;
@property (nonatomic, strong) NSString *title;
@property (nonatomic, strong) NSString *firstname;
@property (nonatomic, strong) NSString *lastname;
@property (nonatomic, strong) NSString *email;
@property (nonatomic, strong) NSString *phone;
@property (nonatomic, strong) NSString *lastLogin;
@property (nonatomic, strong) NSNumber *ordersCount;





@end

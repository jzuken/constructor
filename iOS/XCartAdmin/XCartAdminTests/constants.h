//
//  constants.h
//  FirstLineSoftwareTestProject
//
//  Created by Иван Афанасьев on 06.04.13.
//  Copyright (c) 2013 Иван Афанасьев. All rights reserved.
//

#import <Foundation/Foundation.h>


//CONSTANTS
static const NSInteger kKeyboardHeight = 216;
static const NSInteger kLoginViewUpHeight = 200;

static const NSInteger kHeightOfListRows = 70;

//XCart5
static NSString *const kTestUsername = @"phoenix.qtmsoft.com";
static NSString *const kTestPassword = @"G7J0AO5Y";

//XCart4
//static NSString *const kTestUsername = @"mobileadmin.x-cart.com";
//static NSString *const kTestPassword = @"8WXE1NGH";

static const NSInteger kSideOfToolView = 140;
static const NSInteger kheightOfEditPriceView = 36;


static NSString *const kUserDefaults_isLogInKey = @"isLoginIn";
static NSString *const kUserDefaults_isLogInObject = @"Login=YES";
static NSString *const kUserDefaults_isLogOutObject = @"Login=NO";


static const NSInteger kNumberOfLoadedItems = 10;


#define NSStringFromInt(int_val)  [NSString stringWithFormat: @"%d", (int)int_val]
#define NSStringFromFloat(float_val)  [NSString stringWithFormat: @"%.2f", float_val]

#define RGB(r, g, b) [UIColor colorWithRed:(r)/255.0 green:(g)/255.0 blue:(b)/255.0 alpha:1]

#define NSMoneyString(currency, value)  [NSString stringWithFormat: @"%@%@", currency, value]


#define kYellowColor RGB(255, 175, 25)
#define kGreyColor RGB(150, 155, 160)
#define kBlueColor RGB(115, 155, 180)
#define kRedColor RGB(245, 130, 25)

#define kTextBlueColor RGB(20, 145, 255)


#define QRWLoc(text) NSLocalizedString(text , nil)
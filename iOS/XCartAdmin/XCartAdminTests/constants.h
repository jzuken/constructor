//
//  constants.h
//  FirstLineSoftwareTestProject
//
//  Created by Иван Афанасьев on 06.04.13.
//  Copyright (c) 2013 Иван Афанасьев. All rights reserved.
//

#import <Foundation/Foundation.h>


//CONSTANTS
static const NSInteger kTopYCoordinateLoginBoxFor4 = 55;
static const NSInteger kTopYCoordinateLoginBoxFor3_5 = 47;
static const NSInteger kBottomYCoordinateLoginBoxFor4 = 164;
static const NSInteger kBottomYCoordinateLoginBoxFor3_5 = 120;

static const NSInteger kHeightOfListRows = 70;


//static NSString *const kTestUsername = @"elengor91@gmail.com";
static NSString *const kTestUsername = @"mobileadmin.x-cart.com";
static NSString *const kTestPassword = @"FQMTED8L";
//static NSString *const kTestPassword = @"hgD4pH0";

static const NSInteger kSideOfToolView = 140;


static NSString *const kUserDefaults_isLogInKey = @"isLoginIn";
static NSString *const kUserDefaults_isLogInObject = @"Login=YES";
static NSString *const kUserDefaults_isLogOutObject = @"Login=NO";


#define NSStringFromInt(int_val)  [NSString stringWithFormat: @"%d", int_val]
#define NSStringFromFloat(float_val)  [NSString stringWithFormat: @"%.2f", float_val]
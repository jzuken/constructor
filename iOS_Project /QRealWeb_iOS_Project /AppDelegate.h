//
//  AppDelegate.h
//  FirstLineSoftwareTestProject
//
//  Created by Иван Афанасьев on 05.04.13.
//  Copyright (c) 2013 Иван Афанасьев. All rights reserved.
//

#import <UIKit/UIKit.h>

@class QWDLoginScrinViewController;
@class QWDFastAuthViewController;

@interface AppDelegate : UIResponder <UIApplicationDelegate>

@property (strong, nonatomic) UIWindow *window;

@property (strong, nonatomic) QWDLoginScrinViewController *firstEnterViewController;
@property (strong, nonatomic) QWDFastAuthViewController *fastAuthViewController;

@property (strong, nonatomic) UINavigationController *appNavigationController;

@end

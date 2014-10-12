//
//  QRWAppDelegate.h
//  XCartAdmin
//
//  Created by Ivan Afanasiev on 10/21/13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import <UIKit/UIKit.h>

@class QRWDashboardViewController;


@interface QRWAppDelegate : UIResponder <UIApplicationDelegate>

@property (strong, nonatomic) UIWindow *window;

@property (strong, nonatomic) QRWDashboardViewController *mainViewController;

@property (strong, nonatomic) UINavigationController *appNavigationController;

@end

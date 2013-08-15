//
//  QRWMainStatsViewController.h
//  QRealWeb_iOSApp
//
//  Created by Ivan Afanasiev on 7/16/13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "QRWBaseViewController.h"

#import "QRWOrdersInfoDashboardViewController.h"
#import "QRWLastOrderDashboardViewController.h"



@interface QRWMainStatsViewController : QRWBaseViewController

@property (strong, nonatomic) IBOutlet UIScrollView *mainStatisticsPagesScrollView;


@property (nonatomic, strong) QRWLastOrderDashboardViewController *lastOrderDashboardViewController;
@property (nonatomic, strong) QRWOrdersInfoDashboardViewController *ordersInfoDashboardViewController;

@property (nonatomic, strong) UIViewController *forNavigationPushViewController;

@end

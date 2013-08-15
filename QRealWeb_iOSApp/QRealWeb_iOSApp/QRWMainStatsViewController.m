//
//  QRWMainStatsViewController.m
//  QRealWeb_iOSApp
//
//  Created by Ivan Afanasiev on 7/16/13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import "QRWMainStatsViewController.h"


@interface QRWMainStatsViewController ()

@end


@implementation QRWMainStatsViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (id)init
{
    return [self initWithNibName:@"QRWMainStatsViewController" bundle:nil];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    _lastOrderDashboardViewController = [[QRWLastOrderDashboardViewController alloc] initWithNameOfPageImage:@"subtitle_last_order.png" nibName:@"QRWLastOrderDashboardViewController"];
    _ordersInfoDashboardViewController = [[QRWOrdersInfoDashboardViewController alloc] initWithNameOfPageImage:@"subtitle_orders_info.png" nibName:@"QRWOrdersInfoDashboardViewController"];
    _ordersInfoDashboardViewController.fullInfoMode = NO;
    _lastOrderDashboardViewController.mainStatsInfoMode = YES;
    
    _lastOrderDashboardViewController.controllerForModalPresent = _forNavigationPushViewController;
    
    _mainStatisticsPagesScrollView.contentSize = CGSizeMake(self.view.frame.size.width, _lastOrderDashboardViewController.view.frame.size.height + _ordersInfoDashboardViewController.view.frame.size.height);
    
    
    CGRect frame = _lastOrderDashboardViewController.view.frame;
    frame.origin.y = _ordersInfoDashboardViewController.view.frame.size.height;
    _lastOrderDashboardViewController.view.frame = frame;
    
    [_mainStatisticsPagesScrollView addSubview:_lastOrderDashboardViewController.view];
    [_mainStatisticsPagesScrollView addSubview:_ordersInfoDashboardViewController.view];
    
    [dataManager sendLastOrderRequest];
    [dataManager sendOrdersStatisticRequest];
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    self.navigationController.navigationBarHidden = YES;
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}


//
//- (void)respondsForLastOrderRequest:(QRWLastOrder *)lastOrder
//{
//    [self stopLoadingAnimation];
//    [_lastOrderDashboardViewController setLastOrder:lastOrder];
//}
//
//- (void)respondsForOrdersStatisticRequest:(NSDictionary *)statistic withArratOfKeys:(NSArray *)keys
//{
//    [self stopLoadingAnimation];
//    [_ordersInfoDashboardViewController setStatistic:statistic];
//}

@end

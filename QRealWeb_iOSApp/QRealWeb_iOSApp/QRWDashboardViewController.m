//
//  QRWStatisticViewController.m
//  QRealWeb_iOSApp
//
//  Created by Ivan Afanasiev on 7/17/13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import "QRWDashboardViewController.h"

#import "QRWTopSellersDashboardViewController.h"
#import "QRWOrdersInfoDashboardViewController.h"
#import "QRWLastOrderDashboardViewController.h"
#import "QRWInformationDashboardViewController.h"


@interface QRWDashboardViewController ()

@property (nonatomic, strong) QRWTopSellersDashboardViewController *topSellersDashboardViewController;
@property (nonatomic, strong) QRWLastOrderDashboardViewController *lastOrderDashboardViewController;
@property (nonatomic, strong) QRWInformationDashboardViewController *informationDashboardViewController;
@property (nonatomic, strong) QRWOrdersInfoDashboardViewController *ordersInfoDashboardViewController;

@end

@implementation QRWDashboardViewController

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
    return [self initWithNibName:@"QRWDashboardViewController" bundle:nil];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    self.navigationController.navigationBarHidden = NO;
    self.navigationItem.title = @"Dashboard";
    
    _dashboardPagesScrollView.pagingEnabled = YES;
    
    _dashboardPagesScrollView.contentSize = CGSizeMake(_dashboardPagesScrollView.frame.size.width * 4, _dashboardPagesScrollView.frame.size.height);
    
    _dashboardPagesScrollView.showsHorizontalScrollIndicator = NO;
    _dashboardPagesScrollView.showsVerticalScrollIndicator = NO;
    _dashboardPagesScrollView.scrollsToTop = NO;
    
    _topSellersDashboardViewController = [[QRWTopSellersDashboardViewController alloc] initWithNameOfPageImage:@"subtitle_topsellers.png" nibName:@"QRWTopSellersDashboardViewController"];
    _lastOrderDashboardViewController = [[QRWLastOrderDashboardViewController alloc] initWithNameOfPageImage:@"subtitle_last_order.png" nibName:@"QRWLastOrderDashboardViewController"];
    _informationDashboardViewController = [[QRWInformationDashboardViewController alloc] initWithNameOfPageImage:@"subtitle_information.png" nibName:@"QRWInformationDashboardViewController"];
    _ordersInfoDashboardViewController = [[QRWOrdersInfoDashboardViewController alloc] initWithNameOfPageImage:@"subtitle_orders_info.png" nibName:@"QRWOrdersInfoDashboardViewController"];
    
    NSArray *controllersArray = @[_ordersInfoDashboardViewController, _lastOrderDashboardViewController, _topSellersDashboardViewController, _informationDashboardViewController];
    CGRect frame = _ordersInfoDashboardViewController.view.frame;
    for (UIViewController *pageViewController in controllersArray) {
        frame.origin.x =+ frame.size.width * [controllersArray indexOfObject:pageViewController];
        pageViewController.view.frame = frame;
        [_dashboardPagesScrollView addSubview:pageViewController.view];
    }
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}



- (void)respondsForLastOrderRequest:(QRWLastOrder *)lastOrder
{
    [_lastOrderDashboardViewController setLastOrder:lastOrder];
}

- (void)respondsForTopProductsRequest:(QRWTopProducts *)topProducts
{
    [_topSellersDashboardViewController setTopProducts:topProducts];
}

- (void)respondsForTopCategoriesRequest:(QRWTopCategories *)topCategories
{
    [_topSellersDashboardViewController setTopCategories:topCategories];
}


@end

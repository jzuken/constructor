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

@property (nonatomic, strong) QRWTopSellersDashboardViewController *topProductsDashboardViewController;
@property (nonatomic, strong) QRWTopSellersDashboardViewController *topCategoriesDashboardViewController;
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
    return [self initWithNibName:@"QRWDashboardViewController" oldNibName:@"QRWDashboardViewControllerOld"];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    self.navigationController.navigationBarHidden = NO;
    self.navigationItem.title = @"Dashboard";
    
    _dashboardPagesScrollView.pagingEnabled = YES;
    
    _dashboardPagesScrollView.showsHorizontalScrollIndicator = NO;
    _dashboardPagesScrollView.showsVerticalScrollIndicator = NO;
    _dashboardPagesScrollView.scrollsToTop = NO;
    
    _topCategoriesDashboardViewController = [[QRWTopSellersDashboardViewController alloc] initWithNameOfPageImage:@"subtitle_topsellers.png" nibName:@"QRWTopSellersDashboardViewController" oldNibName:@"QRWTopSellersDashboardViewControllerOld" viewControllerForPresent:nil];
    _topProductsDashboardViewController = [[QRWTopSellersDashboardViewController alloc] initWithNameOfPageImage:@"subtitle_topsellers.png" nibName:@"QRWTopSellersDashboardViewController" oldNibName:@"QRWTopSellersDashboardViewControllerOld" viewControllerForPresent:nil];
    _informationDashboardViewController = [[QRWInformationDashboardViewController alloc] initWithNameOfPageImage:@"subtitle_information.png" nibName:@"QRWInformationDashboardViewController" oldNibName:nil viewControllerForPresent:nil];
    _ordersInfoDashboardViewController = [[QRWOrdersInfoDashboardViewController alloc] initWithNameOfPageImage:@"subtitle_orders_info.png" nibName:@"QRWOrdersInfoDashboardViewController" oldNibName:nil viewControllerForPresent:nil];
    
    _ordersInfoDashboardViewController.fullInfoMode = YES;
    [_topProductsDashboardViewController setTypeOfData:  @"products"];
    [_topCategoriesDashboardViewController setTypeOfData: @"categories"];
    
    NSArray *controllersArray = @[_ordersInfoDashboardViewController, _topProductsDashboardViewController, _topCategoriesDashboardViewController];//, _informationDashboardViewController];
    CGRect frame = _ordersInfoDashboardViewController.view.frame;
    _dashboardPagesScrollView.contentSize = CGSizeMake(_dashboardPagesScrollView.frame.size.width * controllersArray.count, _dashboardPagesScrollView.frame.size.height);
    
    for (UIViewController *pageViewController in controllersArray) {
        frame.origin.x =+ frame.size.width * [controllersArray indexOfObject:pageViewController];
        pageViewController.view.frame = frame;
        [_dashboardPagesScrollView addSubview:pageViewController.view];
    }
    
    
    [dataManager sendTopCategoriesRequest];
    [dataManager sendTopProductsRequest];
    [dataManager sendOrdersStatisticRequest];
    
    [self startLoadingAnimation];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}



- (void)respondsForTopProductsRequest:(QRWTopProducts *)topProducts
{
    [self stopLoadingAnimation];
    [_topProductsDashboardViewController setTopProducts:topProducts];
}

- (void)respondsForTopCategoriesRequest:(QRWTopCategories *)topCategories
{
    [self stopLoadingAnimation];
    [_topCategoriesDashboardViewController setTopCategories:topCategories];
}

- (void)respondsForOrdersStatisticRequest:(NSDictionary *)statistic withArratOfKeys:(NSArray *)keys
{
    [self stopLoadingAnimation];
    [_ordersInfoDashboardViewController setStatistic:statistic];
}

@end

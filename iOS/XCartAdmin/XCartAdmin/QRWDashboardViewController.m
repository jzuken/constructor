//
//  QRWdashboardViewController.m
//  XCartAdmin
//
//  Created by Ivan Afanasiev on 10/21/13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import "QRWDashboardViewController.h"

#import "QRWUsersViewController.h"
#import "QRWReviewsViewController.h"
#import "QRWProductsViewController.h"

@interface QRWDashboardViewController ()

@property (nonatomic, strong) NSTimer *updateDasboardTimer;
@property (nonatomic, strong) NSArray *todayOrders;

@property (nonatomic, strong) QRWUsersViewController *usersViewController;
@property (nonatomic, strong) QRWReviewsViewController *reviewsViewController;
@property (nonatomic, strong) QRWProductsViewController *productsViewController;

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
    [QRWDataManager sendDashboardRequestWithBlock:^(QRWDashboardEntety *dashboardEntety, NSError *error) {
        [self responseFromTheServer:dashboardEntety];
    }];
}


- (void)viewWillAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    [self.navigationController setNavigationBarHidden:YES animated:YES];
}


- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}



- (void) responseFromTheServer:(QRWBaseEntety *) entety
{
    _reviewsTodayLabel.text = NSStringFromInt([[(QRWDashboardEntety *)entety reviewsToday] intValue]);
    _lowStockProducts.text = NSStringFromInt([[(QRWDashboardEntety *)entety lowStock] intValue]);
    _productsSoldTodayLabel.text = NSStringFromInt([[(QRWDashboardEntety *)entety todaySold] intValue]);
    _visitorsToday.text = NSStringFromInt([[(QRWDashboardEntety *)entety todayVisitors] intValue]);
    
    _todaySalesLabel.text = NSStringFromFloat([[(QRWDashboardEntety *)entety todaySales] floatValue]);
    
    _numberOfOrdersLable.text = NSStringFromInt([[(QRWDashboardEntety *)entety todayOrdersCount] intValue]);
    
    self.dataArray= [(QRWDashboardEntety *)entety todayOrders];
//    [self.tableView reloadData];
}




#pragma mark - TableView methods




#pragma mark - Buttons pressed


- (IBAction)todaySalesClicked:(id)sender
{
    
}

- (IBAction)visitorsTodayClicked:(id)sender
{
    if (!_usersViewController) {
        _usersViewController = [[QRWUsersViewController alloc] init];
    }
    [self.navigationController pushViewController:_usersViewController animated:YES];
}

- (IBAction)productsSoldTodayClicked:(id)sender
{
    if (!_productsViewController) {
        _productsViewController = [[QRWProductsViewController alloc] init];
    }
    [self.navigationController pushViewController:_productsViewController animated:YES];
}

- (IBAction)reviewsTodayClicked:(id)sender
{
    if (!_reviewsViewController) {
        _reviewsViewController = [[QRWReviewsViewController alloc] init];
    }
    [self.navigationController pushViewController:_reviewsViewController animated:YES];
}

- (IBAction)lowStockProductsClicked:(id)sender
{
    if (!_productsViewController) {
    _productsViewController = [[QRWProductsViewController alloc] init];
    }
    [self.navigationController pushViewController:_productsViewController animated:YES];
}
@end

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
#import "QRWOrdersViewController.h"

@interface QRWDashboardViewController ()

@property (nonatomic, strong) NSTimer *updateDasboardTimer;
@property (nonatomic, strong) NSArray *todayOrders;

@property (nonatomic, strong) QRWUsersViewController *usersViewController;
@property (nonatomic, strong) QRWReviewsViewController *reviewsViewController;
@property (nonatomic, strong) QRWProductsViewController *productsViewController;
@property (nonatomic, strong) QRWOrdersViewController *ordersViewController;

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
    
    self.baseCell = [UITableViewCell new];
    
    [self startLoadingAnimation];
    [QRWDataManager sendDashboardRequestWithBlock:^(QRWDashboardEntety *dashboardEntety, NSError *error) {
        _reviewsTodayLabel.text = NSStringFromInt([[dashboardEntety reviewsToday] intValue]);
        _lowStockProducts.text = NSStringFromInt([[dashboardEntety lowStock] intValue]);
        _productsSoldTodayLabel.text = NSStringFromInt([[dashboardEntety todaySold] intValue]);
        _visitorsToday.text = NSStringFromInt([[dashboardEntety todayVisitors] intValue]);
        
        _todaySalesLabel.text = NSStringFromFloat([[dashboardEntety todaySales] floatValue]);
        
        _numberOfOrdersLable.text = [NSString stringWithFormat:@"(%d)", [[dashboardEntety todayOrdersCount] intValue]];
        
        self.dataArray= [dashboardEntety todayOrders];
        [self.tableView reloadData];
        
        [self stopLoadingAnimation];
    }];
    
    self.tableView.showsInfiniteScrolling = NO;
    self.tableView.showsPullToRefresh = NO;
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





#pragma mark - TableView methods


- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 60;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    return 0;
}


- (void)configureCell:(UITableViewCell *)cell atIndexPath:(NSIndexPath *)indexPath
{
    QRWOrder *order = [self.dataArray objectAtIndex:indexPath.section];
    
    [cell setAccessoryType:UITableViewCellAccessoryDisclosureIndicator];
    
    cell.textLabel.text = [NSString stringWithFormat:@"%@ %@ (#%d)", order.firstname, order.lastname, [order.orderid intValue]];
    cell.detailTextLabel.text = NSMoneyString(@"$", NSStringFromInt([order.total intValue]));

}




#pragma mark - Buttons pressed


- (IBAction)todaySalesClicked:(id)sender
{
    if (!_ordersViewController) {
        _ordersViewController = [[QRWOrdersViewController alloc] init];
    }
    [self.navigationController pushViewController:_ordersViewController animated:YES];
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
    [_productsViewController setIsLowStock:NO];
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
    [_productsViewController setIsLowStock:YES];
    [self.navigationController pushViewController:_productsViewController animated:YES];
}
@end

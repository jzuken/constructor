//
//  QRWUserOrdersViewController.m
//  QRealWeb_iOSApp
//
//  Created by Ivan Afanasiev on 8/14/13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import "QRWUserOrdersViewController.h"
#import "QRWProductsViewControllerForModalPresent.h"
#import "QRWUserOrderCell.h"

@interface QRWUserOrdersViewController ()
{
    int numberOfRegisteredUsers;
    BOOL loadMoreDataAvaliable;
}

@property (nonatomic, strong) QRWUser *user;
@property (nonatomic, strong) NSMutableArray *orders;

@property (nonatomic, strong) QRWProductsViewControllerForModalPresent *productsViewController;

@end

@implementation QRWUserOrdersViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}


- (id)initWithUser:(QRWUser *)user
{
    self = [self initWithNibName:@"QRWUserOrdersViewController" oldNibName:@"QRWUserOrdersViewControllerOld"];
    _user = user;
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    self.navigationController.navigationBarHidden = NO;
    self.navigationItem.title = NSLocalizedString(@"USER ORDERS", nil);
    
    __weak QRWUserOrdersViewController *weakSelf = self;
    
    [_ordersTableView addPullToRefreshWithActionHandler:^{
        [weakSelf reloadsTableViewData];
    }];
    
    [_ordersTableView addInfiniteScrollingWithActionHandler:^{
        [weakSelf addsDataAndReloadsTableView];
    }];
    
    [self addsDataAndReloadsTableView];
    
    _userLable.text = [NSString stringWithFormat:@"%@ %@", _user.firstname, _user.lastname];
}

- (void)respondsForUserOrdersRequest:(NSArray *)userOrders
{
    if (_orders.count == 0) {
        _orders = [NSMutableArray arrayWithArray:userOrders];
    } else {
        [_orders addObjectsFromArray:userOrders];
    }
    [_ordersTableView reloadData];
    [_ordersTableView.pullToRefreshView stopAnimating];
    [self stopLoadingAnimation];
}

- (void) addsDataAndReloadsTableView
{
    [dataManager sendOrdersOfUserRequestWithUser: _user startPoint:[_ordersTableView numberOfRowsInSection:0] lenght:10];
    if (isFirstDataLoading) {
        isFirstDataLoading = NO;
        [self startLoadingAnimation];
    }
}

- (void) reloadsTableViewData
{
    if (isFirstDataLoading) {
        isFirstDataLoading = NO;
        [self startLoadingAnimation];
    }
    [_orders removeAllObjects];
    [_ordersTableView reloadData];
    [self addsDataAndReloadsTableView];
}

#pragma mark Table view methods


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    _productsViewController = [[QRWProductsViewControllerForModalPresent alloc] initWithProducts:[[_orders objectAtIndex:indexPath.row] products]];
    [self presentViewController:_productsViewController animated:YES completion:nil];
}


- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}


- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return _orders.count;
}


- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 70;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSArray *topLevelObjects = [[NSBundle mainBundle] loadNibNamed:@"QRWUserOrderCell" owner:self options:nil];
    QRWUserOrderCell *cell = [topLevelObjects objectAtIndex:0];
    
    [self configureProductCell:cell atIndexPath:indexPath];
    
    return cell;
}

- (void)configureProductCell:(QRWUserOrderCell *)cell atIndexPath:(NSIndexPath *)indexPath
{
    cell.totalLable.text = [NSString stringWithFormat: @"%.2f$", [[(QRWLastOrder *)_orders[indexPath.row] total] floatValue]];
    cell.statusLable.text = [NSString stringWithFormat: @"Status: %@", [(QRWLastOrder *)_orders[indexPath.row] status]];
    cell.dateLable.text = [NSString stringWithFormat: @"Date: %@", [(QRWLastOrder *)_orders[indexPath.row] date]];
    cell.productsLable.text = [NSString stringWithFormat: @"%d items >", [[(QRWLastOrder *)_orders[indexPath.row] products] count]];
}



@end

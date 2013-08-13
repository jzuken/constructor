//
//  QRWUsersViewController.m
//  QRealWeb_iOSApp
//
//  Created by Ivan Afanasiev on 8/2/13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import "QRWUsersViewController.h"

#import "QRWUsers.h"
#import "QRWUserCell.h"

#import "FPPopoverController.h"

@interface QRWUsersViewController ()
{
    int numberOfRegisteredUsers;
    BOOL loadMoreDataAvaliable;
}

@property (nonatomic, strong) NSMutableArray *users;
@property (nonatomic, strong) NSString *currentSort;

@property (nonatomic, strong) FPPopoverController *sortedByPopoverController;
@property (nonatomic, strong) QRWUsersSortedByTableViewController *sortedByViewController;

@end

@implementation QRWUsersViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
    }
    return self;
}

- (id)init
{
    return [self initWithNibName:@"QRWUsersViewController" bundle:nil];
}


- (void)viewDidLoad
{
    [super viewDidLoad];
    loadMoreDataAvaliable = YES;
    
    self.navigationController.navigationBarHidden = NO;
    self.navigationItem.title = NSLocalizedString(@"USERS", nil);
    _currentSort = @"orders";
    [self.navigationItem setRightBarButtonItem:[[UIBarButtonItem alloc] initWithTitle:NSLocalizedString(@"SORT_BY", nil) style:UIBarButtonItemStyleBordered target:self action:@selector(openSortedByPopover)]];
    
    __weak QRWUsersViewController *weakSelf = self;
    
    [_usersTableView addPullToRefreshWithActionHandler:^{
        [weakSelf reloadsTableViewData];
    }];
    
    [_usersTableView addInfiniteScrollingWithActionHandler:^{
        [weakSelf addsDataAndReloadsTableView];
    }];
    
    [self addsDataAndReloadsTableView];
}

- (void)respondsForUserRequest:(QRWUsers *)usersObject
{
    numberOfRegisteredUsers = [usersObject.registered intValue];
    if (_users.count == 0) {
        _users = [NSMutableArray arrayWithArray:usersObject.users];
    } else {
        [_users addObjectsFromArray:usersObject.users];
    }
    [_usersTableView reloadData];
    loadMoreDataAvaliable = YES;
    [_usersTableView.pullToRefreshView stopAnimating];
    [self stopLoadingAnimation];
}

- (void) openSortedByPopover
{
    _sortedByViewController = [[QRWUsersSortedByTableViewController alloc] initWithCurrentSortType:_currentSort];
    _sortedByViewController.delegate = self;
    
    _sortedByPopoverController = [[FPPopoverController alloc] initWithViewController:_sortedByViewController];
    _sortedByPopoverController.contentSize = _sortedByViewController.view.frame.size;
    _sortedByPopoverController.border = NO;
    _sortedByPopoverController.tint = FPPopoverWhiteTint;
    [_sortedByPopoverController presentPopoverFromView:_pointForPopoverPresent];

}


- (void) addsDataAndReloadsTableView
{
    [dataManager sendUsersRequestWithSort:_currentSort startPoint:[_usersTableView numberOfRowsInSection:0] lenght:10];
    if (isFirstDataLoading) {
        isFirstDataLoading = NO;
        [self startLoadingAnimation];
    }
}


- (void) reloadsTableViewData
{
    [dataManager sendUsersRequestWithSort:_currentSort startPoint:[_usersTableView numberOfRowsInSection:0] lenght:10];
    if (isFirstDataLoading) {
        isFirstDataLoading = NO;
        [self startLoadingAnimation];
    }
    [_users removeAllObjects];
    [_usersTableView reloadData];
}

#pragma mark QRWUsersSortedByTableViewControllerDelegate

- (void)useSortWithName:(NSString *)sortType
{
    if (![_currentSort isEqualToString:sortType]) {
        [_users removeAllObjects];
        [_usersTableView reloadData];
        _currentSort = sortType;
        [dataManager sendUsersRequestWithSort:_currentSort startPoint:0 lenght:10];
    }
    [_sortedByPopoverController dismissPopoverAnimated:YES];
}

#pragma mark Table view methods


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    UIActionSheet *userActionSheet = [[UIActionSheet alloc] initWithTitle:nil
                                                                 delegate:self
                                                        cancelButtonTitle:NSLocalizedString(@"CANCEL", nil)
                                                   destructiveButtonTitle:nil
                                                        otherButtonTitles:NSLocalizedString(@"FULL_INFO", nil), NSLocalizedString(@"ORDERS_LIST", nil), NSLocalizedString(@"SEND_A_MESSAGE", nil), NSLocalizedString(@"TO_THE_BLACK_LIST_WITH_A_MESSAGE", nil), nil];
    [userActionSheet showInView:self.view];
}


- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}


- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return _users.count;
}


- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 105;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSArray *topLevelObjects = [[NSBundle mainBundle] loadNibNamed:@"QRWUserCell" owner:self options:nil];
    QRWUserCell *cell = [topLevelObjects objectAtIndex:0];
    
    [self configureProductCell:cell atIndexPath:indexPath];
    
    return cell;
}

- (void)configureProductCell:(QRWUserCell *)cell atIndexPath:(NSIndexPath *)indexPath
{
    cell.userLable.text = [NSString stringWithFormat:NSLocalizedString(@"USER_LABLE_TEXT", nil), [(QRWUser *)_users[indexPath.row] firstname], [(QRWUser *)_users[indexPath.row] lastname]];
    cell.loginLable.text = [NSString stringWithFormat:NSLocalizedString(@"LOGIN_LABLE_TEXT", nil),[(QRWUser *)_users[indexPath.row] login]];
    cell.typeLable.text = [NSString stringWithFormat:NSLocalizedString(@"TYPE_LABLE_TEXT", nil),[(QRWUser *)_users[indexPath.row] usertype]];
    cell.firstLoginLable.text = [NSString stringWithFormat:NSLocalizedString(@"FIRST_LOGIN_LABLE_TEXT", nil), [(QRWUser *)_users[indexPath.row] firstLogin]];
    cell.ordersCountLable.text = [NSString stringWithFormat:NSLocalizedString(@"ORDERS_COUNT_LABLE_TEXT", nil), [[(QRWUser *)_users[indexPath.row] ordersCount] intValue]];
}



@end

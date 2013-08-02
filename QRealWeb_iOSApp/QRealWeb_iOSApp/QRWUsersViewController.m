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
    self.navigationItem.title = @"Users";
    _currentSort = @"orders";
    [self.navigationItem setRightBarButtonItem:[[UIBarButtonItem alloc] initWithTitle:@"Sort by:" style:UIBarButtonItemStyleBordered target:self action:@selector(openSortedByPopover)]];
    
    [dataManager sendUsersRequestWithSort:_currentSort startPoint:0 lenght:10];
}

- (void)respondsForUserRequest:(QRWUsers *)usersObject
{
    numberOfRegisteredUsers = [usersObject.registered intValue];
    if (_users.count == 0) {
        _users = [NSMutableArray arrayWithArray:usersObject.users];
    } else {
        [_users addObject:usersObject.users];
    }
    [_usersTableView reloadData];
    loadMoreDataAvaliable = YES;
}

- (void) openSortedByPopover
{
    _sortedByViewController = [[QRWUsersSortedByTableViewController alloc] initWithCurrentSortType:_currentSort];
    _sortedByViewController.delegate = self;
    
    _sortedByPopoverController = [[FPPopoverController alloc] initWithViewController:_sortedByViewController];
    _sortedByPopoverController.contentSize = _sortedByViewController.view.frame.size;
    _sortedByPopoverController.border = NO;
    _sortedByPopoverController.tint = FPPopoverWhiteTint;
//    [_sortedByPopoverController presentPopoverFromView:[[UIView alloc] initWithFrame:CGRectMake(0, 5, 40, 20)]];
    [_sortedByPopoverController presentPopoverFromView:_pointForPopoverPresent];

}


- (void) addsDataAndReloadsTableView
{
    if (loadMoreDataAvaliable) {
        loadMoreDataAvaliable = NO;
        [dataManager sendUsersRequestWithSort:_currentSort startPoint:[_usersTableView numberOfRowsInSection:0] lenght:10];
    }
}


#pragma mark QRWUsersSortedByTableViewControllerDelegate

- (void)useSortWithName:(NSString *)sortType
{
    if (![_currentSort isEqualToString:sortType]) {
        [_users removeAllObjects];
        [_usersTableView reloadData];
    }
    _currentSort = sortType;
    [dataManager sendUsersRequestWithSort:_currentSort startPoint:0 lenght:10];
}

#pragma mark Table view methods

- (void)scrollViewDidScroll: (UIScrollView *)scroll
{
    NSInteger currentOffset = scroll.contentOffset.y;
    NSInteger maximumOffset = scroll.contentSize.height - scroll.frame.size.height;

    if (maximumOffset - currentOffset <= -40.0) {
        [self addsDataAndReloadsTableView];
    }
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    UIActionSheet *userActionSheet = [[UIActionSheet alloc] initWithTitle:nil
                                                                 delegate:self
                                                        cancelButtonTitle:@"Cencal"
                                                   destructiveButtonTitle:nil
                                                        otherButtonTitles:@"Full info", @"Orders list", @"Send a message", @"To the black list with a message", nil];
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
    cell.userLable.text = [NSString stringWithFormat:@"%@ %@", [(QRWUser *)_users[indexPath.row] firstname], [(QRWUser *)_users[indexPath.row] lastname]];
    cell.loginLable.text = [NSString stringWithFormat:@"Login: %@",[(QRWUser *)_users[indexPath.row] login]];
    cell.typeLable.text = [NSString stringWithFormat:@"User type: %@",[(QRWUser *)_users[indexPath.row] usertype]];
    cell.firstLoginLable.text = [NSString stringWithFormat:@"First login: %@", [(QRWUser *)_users[indexPath.row] firstLogin]];
    cell.ordersCountLable.text = [NSString stringWithFormat:@"Oreders count: %d", [[(QRWUser *)_users[indexPath.row] ordersCount] intValue]];
}



@end

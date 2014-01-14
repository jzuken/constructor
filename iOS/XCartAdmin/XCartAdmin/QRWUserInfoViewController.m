//
//  QRWUserInfoViewController.m
//  XCartAdmin
//
//  Created by Иван Афанасьев on 11.01.14.
//  Copyright (c) 2014 Ivan Afanasiev. All rights reserved.
//

#import "QRWUserInfoViewController.h"
#import "QRWOrdersCell.h"
#import "QRWUserFormCell.h"


@interface QRWUserInfoViewController ()

@property (nonatomic, strong) QRWUserInfo *userInfo;

@property (nonatomic, strong) MFMailComposeViewController *mailViewController;

@end

@implementation QRWUserInfoViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
    }
    return self;
}


-(id)initWithUserInfo:(QRWUserInfo *)userInfo
{
    self = [self init];
    _userInfo = userInfo;
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];

    self.tableView.showsPullToRefresh = NO;
    [self loadObjectsWithSearchString:@"" asEmptyArray:YES];
}


- (void)viewWillAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    [self.navigationController setNavigationBarHidden:NO animated:YES];
    [self setNavigationBarColor:kBlueColor title: QRWLoc(@"USERS")];
}


- (void)loadObjectsWithSearchString:(NSString *)searchString asEmptyArray:(BOOL)asEmpty
{
    [self startLoadingAnimation];
    [QRWDataManager sendUserOrderRequestWithUserID:[_userInfo.userID integerValue]
                                         fromPoint:asEmpty? 0 : self.dataArray.count
                                           toPoint:kNumberOfLoadedItems
                                             block:^(NSArray *orders, NSError *error) {
                                                 [self smartAddObjectToDataArrayAsNew:asEmpty withLoaddedArray:orders];
                                             }];
}


#pragma mark - Actions

- (IBAction)callButtonClicked:(id)sender
{
    if (![@"" isEqual:_userInfo.phone]) {
        [[UIApplication sharedApplication] openURL:[NSURL URLWithString: NSMoneyString(@"tel:", _userInfo.phone)]];
    }
}

- (IBAction)emailButtonClicked:(id)sender
{
    if (![@"" isEqual:_userInfo.email]) {
        _mailViewController = [[MFMailComposeViewController alloc] init];
        _mailViewController.mailComposeDelegate = self;
        [_mailViewController setToRecipients:@[_userInfo.email]];
        [self presentViewController:_mailViewController animated:YES completion:nil];
    }
}


#pragma mark - tableView

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [self.tableView deselectRowAtIndexPath:indexPath animated:YES];
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (indexPath.section != 0) {
        self.baseCell = [QRWOrdersCell new];
        return [super tableView:tableView cellForRowAtIndexPath:indexPath];
    } else {
        self.baseCell = [QRWUserFormCell new];
        return [super tableView:tableView cellForRowAtIndexPath:indexPath];
    }
}

- (void)configureProductCell:(UITableViewCell *)cell atIndexPath:(NSIndexPath *)indexPath
{
    if (indexPath.section == 0) {
        NSString *userInfoFieldText;
        NSString *detailText;
        
        switch (indexPath.row) {
            case 0:{
                userInfoFieldText = QRWLoc(@"FIRST_NAME");
                detailText = _userInfo.firstname;
            }
            break;
                
            case 1:{
                userInfoFieldText = QRWLoc(@"LAST_NAME");
                detailText = _userInfo.lastname;
            }
            break;
                
            case 2:{
                userInfoFieldText = QRWLoc(@"EMAIL");
                detailText = _userInfo.email;
            }
            break;
                
            case 3:{
                userInfoFieldText = QRWLoc(@"ADDRESS");
                detailText = _userInfo.address;
            }
            break;
                
            case 4:{
                userInfoFieldText = QRWLoc(@"PHONE");
                detailText = _userInfo.phone;
            }
                break;
                
            case 5:{
                userInfoFieldText = QRWLoc(@"FAX");
                detailText = _userInfo.fax;
            }
                break;
        }
        
        
        [(QRWUserFormCell *)cell userFieldLabel].text = userInfoFieldText;
        [(QRWUserFormCell *)cell detailLabel].text = detailText;
    } else {
        [cell setAccessoryType:UITableViewCellAccessoryDisclosureIndicator];
        
        QRWOrder *order = [self.dataArray objectAtIndex:indexPath.row];
        
        [(QRWOrdersCell *)cell nameLabel].text = [NSString stringWithFormat:@"%@ %@ (#%d)", order.firstname, order.lastname, [order.orderid intValue]];
        [(QRWOrdersCell *)cell dateLabel].text = [NSString stringWithFormat:@"%@\n%@", order.month, order.day];
        [(QRWOrdersCell *)cell priceLabel].text = NSMoneyString(@"$", NSStringFromInt([order.total intValue]));
        
        [(QRWOrdersCell *)cell statusLabel].text = QRWLoc(order.status);
        [(QRWOrdersCell *)cell statusLabel].textColor = [_statusColorsDictionary objectForKey: order.status];
    }
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    return 30;
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    UIView *headerView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 320, 30)];
    headerView.backgroundColor = kBlueColor;
    
    UILabel *nameLabel = [[UILabel alloc] initWithFrame:CGRectMake(1, 2, 320, 27)];
    nameLabel.text = section == 0 ? QRWLoc(@"USER_INFO"): QRWLoc(@"ORDERS_LIST");
    
    nameLabel.textColor = kTextBlueColor;
    nameLabel.backgroundColor = [UIColor whiteColor];
    
    [headerView addSubview:nameLabel];
    
    return headerView;
}


- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return indexPath.section == 0 ? 40.0: 60.0;
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 2;
}


- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return section == 0 ? 6: self.dataArray.count;
}

#pragma mark - MFMailComposeViewControllerDelegate

- (void)mailComposeController:(MFMailComposeViewController *)controller didFinishWithResult:(MFMailComposeResult)result error:(NSError *)error
{
    [controller dismissViewControllerAnimated:YES completion:nil];
}


@end

//
//  QRWUsersViewController.m
//  XCartAdmin
//
//  Created by Ivan Afanasiev on 24/12/13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import "QRWUsersViewController.h"
#import "QRWUserInfoViewController.h"
#import "QRWUserCell.h"
#import "QRWSettingsClient.h"

@interface QRWUsersViewController ()

@end

@implementation QRWUsersViewController



- (void)viewDidLoad
{
    [super viewDidLoad];
    
    self.baseCell = [QRWUserCell new];
    self.noResultsText = QRWLoc(@"NORES_VISITORS");
    
    self.requestSearchBar.backgroundColor = kBlueColor;
    
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
    [QRWDataManager sendUserRequestWithSearchString:searchString
                                          fromPoint:asEmpty? 0 : self.dataArray.count
                                            toPoint:kNumberOfLoadedItems
                                              block:^(NSArray *users, NSError *error) {
                                                  [self smartAddObjectToDataArrayAsNew:asEmpty withLoaddedArray:users];
                                              }];
}


#pragma mark - TableView methods


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [self startLoadingAnimation];
    [self.tableView deselectRowAtIndexPath:indexPath animated:YES];
    
    QRWUser *user = [self.dataArray objectAtIndex:indexPath.section];
    
    [QRWDataManager sendUserInfoRequestWithID:[user.userID intValue]
                                        block:^(QRWUserInfo *userInfo, NSError *error) {
                                            [self stopLoadingAnimation];
                                            QRWUserInfoViewController *userInfoViewController = [[QRWUserInfoViewController alloc] initWithUserInfo:userInfo];
                                            [self.navigationController pushViewController:userInfoViewController animated:YES];
                                        }];
}


- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 60;
}



- (void)configureCell:(UITableViewCell *)cell atIndexPath:(NSIndexPath *)indexPath
{
    QRWUser *user = [self.dataArray objectAtIndex:indexPath.section];
    [(QRWUserCell *)cell emailLabel].text = user.email;
    [(QRWUserCell *)cell userTypeLabel].text = [QRWSettingsClient getUserTypeByKey:user.usertype];
    [(QRWUserCell *)cell lastLoginLabel].text = user.lastLogin;
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    UIView *headerView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 320, 30)];
    headerView.backgroundColor = kBlueColor;
    
    UILabel *nameLabel = [[UILabel alloc] initWithFrame:CGRectMake(0, 1, 320, 29)];
    nameLabel.font = [UIFont systemFontOfSize:15];
    QRWUser *user = [self.dataArray objectAtIndex:section];
    nameLabel.text = [NSString stringWithFormat:@"%@ %@ %@", user.title, user.firstname, user.lastname];
    
    nameLabel.textColor = kTextBlueColor;
    nameLabel.adjustsFontSizeToFitWidth=YES;
    nameLabel.minimumScaleFactor = 0.5;
    nameLabel.backgroundColor = [UIColor whiteColor];
    
    [headerView addSubview:nameLabel];
    
    return headerView;
}

@end

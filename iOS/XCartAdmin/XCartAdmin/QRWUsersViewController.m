//
//  QRWUsersViewController.m
//  XCartAdmin
//
//  Created by Ivan Afanasiev on 24/12/13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import "QRWUsersViewController.h"

#import "QRWUserCell.h"

@interface QRWUsersViewController ()

@end

@implementation QRWUsersViewController



- (void)viewDidLoad
{
    [super viewDidLoad];
    
    self.baseCell = [QRWUserCell new];
    [QRWDataManager sendUserRequestWithSearchString:@""
                                          fromPoint:0
                                            toPoint:10
                                              block:^(NSArray *users, NSError *error) {
                                                  self.dataArray = [NSArray arrayWithArray:users];
                                                  [self.tableView reloadData];
                                              }];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    [self.navigationController setNavigationBarHidden:NO animated:YES];
}


- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 60;
}



- (void)configureProductCell:(UITableViewCell *)cell atIndexPath:(NSIndexPath *)indexPath
{
    QRWUser *user = [self.dataArray objectAtIndex:indexPath.section];
    [(QRWUserCell *)cell emailLabel].text = user.email;
    [(QRWUserCell *)cell userTypeLabel].text = user.usertype;
    [(QRWUserCell *)cell lastLoginLabel].text = user.lastLogin;
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    UIView *headerView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 320, 30)];
    headerView.backgroundColor = [UIColor blueColor];
    
    UILabel *nameLabel = [[UILabel alloc] initWithFrame:CGRectMake(5, 5, 310, 20)];
    nameLabel.font = [UIFont systemFontOfSize:15];
    QRWUser *user = [self.dataArray objectAtIndex:section];
    nameLabel.text = [NSString stringWithFormat:@"%@ %@ %@", user.title, user.firstname, user.lastname];
    
    [headerView addSubview:nameLabel];
    
    return headerView;
}

@end
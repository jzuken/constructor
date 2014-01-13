//
//  QRWUserInfoViewController.m
//  XCartAdmin
//
//  Created by Иван Афанасьев on 11.01.14.
//  Copyright (c) 2014 Ivan Afanasiev. All rights reserved.
//

#import "QRWUserInfoViewController.h"
#import "QRWOrdersCell.h"

@interface QRWUserInfoViewController ()

@end

@implementation QRWUserInfoViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    self.baseCell = [QRWOrdersCell new];
}


- (void)viewWillAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    [self.navigationController setNavigationBarHidden:NO animated:YES];
    [self setNavigationBarColor:kBlueColor title: QRWLoc(@"USERS")];
}


#pragma mark - tableView

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 2;
}


- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return section == 0 ? 6: self.dataArray.count;
}


- (void)configureProductCell:(UITableViewCell *)cell atIndexPath:(NSIndexPath *)indexPath
{
    if (indexPath.section == 0) {
        cell = [[UITableViewCell alloc] initWithFrame:cell.frame];
        
        switch (indexPath.row) {
            case 0:{
                cell.textLabel.text = @"FIRST_NAME";
                cell.detailTextLabel.text = @"Name";
            }
                break;
                

        }
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



@end

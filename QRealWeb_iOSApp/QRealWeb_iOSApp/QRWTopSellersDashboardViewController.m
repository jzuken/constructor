//
//  QRWTopSellersDashboardViewController.m
//  QRealWeb_iOSApp
//
//  Created by Иван Афанасьев on 23.07.13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import "QRWTopSellersDashboardViewController.h"

#import "QRWTopSellersTopProductsTableViewCell.h"
#import "QRWTopSellersTopCategoriesTableViewCell.h"


@interface QRWTopSellersDashboardViewController ()
{
    NSArray *segmentImageNamesArray;
}

@property (nonatomic, strong) NSDictionary *topProducts;
@property (nonatomic, strong) NSDictionary *topCategories;


@end

@implementation QRWTopSellersDashboardViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}


#pragma mark Table view methods

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 2;
}


- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return 10;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    return 30;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (indexPath.section == 0) {
        return 70;
    } else {
        return 60;
    }
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    UIImageView *headerView = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, 320, 30)];
    if (section == 0) {
        [headerView setImage:[UIImage imageNamed:@"subtitle_top10products.png"]];
    } else {
        [headerView setImage:[UIImage imageNamed:@"subtitle_top10categories.png"]];
    }
    return headerView;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    UITableViewCell *cell;
    
    if (indexPath.section == 0) {
        NSArray *topLevelObjects = [[NSBundle mainBundle] loadNibNamed:@"QRWTopSellersTopProductsTableViewCell" owner:self options:nil];
        QRWTopSellersTopProductsTableViewCell *cell = [topLevelObjects objectAtIndex:0];
        
        [self configureProductCell:cell atIndexPath:indexPath];
    } else {
        NSArray *topLevelObjects = [[NSBundle mainBundle] loadNibNamed:@"QRWTopSellersTopCategoriesTableViewCell" owner:self options:nil];
        QRWTopSellersTopCategoriesTableViewCell *cell = [topLevelObjects objectAtIndex:0];
        
        [self configureCategoriesCell:cell atIndexPath:indexPath];
    }
    
    return cell;
}

- (void)configureProductCell:(QRWTopSellersTopProductsTableViewCell *)cell atIndexPath:(NSIndexPath *)indexPath
{
    
}

- (void)configureCategoriesCell:(QRWTopSellersTopCategoriesTableViewCell *)cell atIndexPath:(NSIndexPath *)indexPath
{
    
}

@end

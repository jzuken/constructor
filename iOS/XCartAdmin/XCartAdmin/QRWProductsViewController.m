//
//  QRWProductsViewController.m
//  XCartAdmin
//
//  Created by Ivan Afanasiev on 09/01/14.
//  Copyright (c) 2014 Ivan Afanasiev. All rights reserved.
//

#import "QRWProductsViewController.h"
#import "QRWProductCell.h"

@interface QRWProductsViewController ()

@end

@implementation QRWProductsViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
//        [self setEdgesForExtendedLayout:UIRectEdgeAll];
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    self.baseCell = [QRWProductCell new];
    [QRWDataManager sendProductsRequestWithSearchString:@""
                                              fromPoint:0
                                                toPoint:10
                                               lowStock:NO
                                                  block:^(NSArray *products, NSError *error) {
                                                      self.dataArray = [NSArray arrayWithArray:products];
                                                      [self.tableView reloadData];
                                                  }];
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
    QRWProduct *product = [self.dataArray objectAtIndex:indexPath.section];
    [(QRWProductCell *)cell SKULabel].text = product.productcode;
    [(QRWProductCell *)cell inStockTypeLabel].text = NSStringFromInt([product.available intValue]);
    [(QRWProductCell *)cell priceLabel].text = NSStringFromInt([product.price intValue]);
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    UIView *headerView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 320, 30)];
    headerView.backgroundColor = [UIColor blueColor];
    
    UILabel *nameLabel = [[UILabel alloc] initWithFrame:CGRectMake(5, 5, 310, 20)];
    nameLabel.font = [UIFont systemFontOfSize:15];
    QRWProduct *product = [self.dataArray objectAtIndex:section];
    nameLabel.text = product.product;
    
    [headerView addSubview:nameLabel];
    
    return headerView;
}

@end

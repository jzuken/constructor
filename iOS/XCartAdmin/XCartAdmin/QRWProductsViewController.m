//
//  QRWProductsViewController.m
//  XCartAdmin
//
//  Created by Ivan Afanasiev on 09/01/14.
//  Copyright (c) 2014 Ivan Afanasiev. All rights reserved.
//

#import "QRWProductsViewController.h"
#import "QRWProductCell.h"
#import "QRWProductInfoViewController.h"

@interface QRWProductsViewController ()
{
    BOOL _isLowStock;
}

@end

@implementation QRWProductsViewController

-(id)initAsLowStock:(BOOL)isLowStock
{
    self = [self init];
    _isLowStock = isLowStock;
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    self.baseCell = [QRWProductCell new];
    [QRWDataManager sendProductsRequestWithSearchString:@""
                                              fromPoint:self.dataArray.count
                                                toPoint:10
                                               lowStock:_isLowStock
                                                  block:^(NSArray *products, NSError *error) {
                                                      NSMutableArray *oldDataArray = [NSMutableArray arrayWithArray: self.dataArray];
                                                      [oldDataArray addObjectsFromArray:products];
                                                      self.dataArray = oldDataArray;
                                                      [self.tableView reloadData];
                                                  }];
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    [self.navigationController setNavigationBarHidden:NO animated:YES];
}



- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    __weak QRWProductsViewController *weakSelf = self;
    [QRWDataManager sendProductInfoRequestWithID:[[(QRWProduct *)self.dataArray[indexPath.section] productid] integerValue]
                                           block:^(QRWProductWithInfo *product, NSError *error) {
                                               QRWProductInfoViewController *productInfoViewController = [[QRWProductInfoViewController alloc] initWithProduct:product];
                                               [weakSelf.navigationController pushViewController:productInfoViewController animated:YES];
                                           }];
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

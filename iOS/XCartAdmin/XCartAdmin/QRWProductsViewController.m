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
    self.noResultsText = QRWLoc(@"NORES_PRODUCTS");
    
    [_productsTypeSegmentedControl addTarget:self action:@selector(segmentedControlTaped) forControlEvents:UIControlEventValueChanged];

    self.requestSearchBar.backgroundColor = kGreyColor;
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    [self.navigationController setNavigationBarHidden:NO animated:YES];
    [self setNavigationBarColor:kGreyColor title: QRWLoc(@"PRODUCTS")];
}


- (void)loadObjectsWithSearchString:(NSString *)searchString asEmptyArray:(BOOL)asEmpty
{
    [self startLoadingAnimation];
    [QRWDataManager sendProductsRequestWithSearchString:searchString
                                              fromPoint:asEmpty? 0 : self.dataArray.count
                                                toPoint:kNumberOfLoadedItems
                                               lowStock:_isLowStock
                                                  block:^(NSArray *products, NSError *error) {
                                                      [self smartAddObjectToDataArrayAsNew:asEmpty withLoaddedArray:products];
                                                  }];
}

- (void) setIsLowStock: (BOOL)isLowStock
{
    _isLowStock = isLowStock;
    
    [_productsTypeSegmentedControl setSelectedSegmentIndex:_isLowStock ? 0: 1];
    
    [self loadObjectsWithSearchString:@"" asEmptyArray:YES];
}


- (void) segmentedControlTaped
{
    _isLowStock = _productsTypeSegmentedControl.selectedSegmentIndex == 0;
    [self loadObjectsWithSearchString:self.requestSearchBar.text asEmptyArray:YES];
}


#pragma mark - TableView methods


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [self startLoadingAnimation];
    [QRWDataManager sendProductInfoRequestWithID:[[(QRWProduct *)self.dataArray[indexPath.section] productid] integerValue]
                                           block:^(QRWProductWithInfo *product, NSError *error) {
                                               [self stopLoadingAnimation];
                                               QRWProductInfoViewController *productInfoViewController = [[QRWProductInfoViewController alloc] initWithProduct:product];
                                               [self.navigationController pushViewController:productInfoViewController animated:YES];
                                           }];
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
}



- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 60;
}



- (void)configureCell:(UITableViewCell *)cell atIndexPath:(NSIndexPath *)indexPath
{
    [cell setAccessoryType:UITableViewCellAccessoryDisclosureIndicator];
    
    QRWProduct *product = [self.dataArray objectAtIndex:indexPath.section];
    [(QRWProductCell *)cell SKULabel].text = product.productcode;
    [(QRWProductCell *)cell inStockTypeLabel].text = NSStringFromInt([product.available intValue]);
    [(QRWProductCell *)cell priceLabel].text = NSMoneyString(@"$", NSStringFromFloat([product.price floatValue]));
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    UIView *headerView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 320, 30)];
    headerView.backgroundColor = kGreyColor;
    
    UILabel *nameLabel = [[UILabel alloc] initWithFrame:CGRectMake(0, 1, 320, 29)];
    nameLabel.font = [UIFont systemFontOfSize:15];
    QRWProduct *product = [self.dataArray objectAtIndex:section];
    nameLabel.text = product.product;
    nameLabel.textColor = kTextBlueColor;
    nameLabel.adjustsFontSizeToFitWidth=YES;
    nameLabel.minimumScaleFactor = 0.5;
    nameLabel.backgroundColor = [UIColor whiteColor];
    
    [headerView addSubview:nameLabel];
    
    return headerView;
}

@end

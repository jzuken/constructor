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
    BOOL loadingProdusctsFinished;
    BOOL loadingCategoriesFinished;
}

@property (nonatomic, strong) QRWTopProducts *topProducts;
@property (nonatomic, strong) QRWTopCategories *topCategories;

@property (nonatomic, strong) NSArray *productsArray;
@property (nonatomic, strong) NSArray *categoriesArray;

@property (nonatomic, strong) HMSegmentedControl *segmentedControl;

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
    _topSellersTableView.hidden = YES;
    
    _segmentedControl = [[HMSegmentedControl alloc] initWithSectionImages:self.segmentImageNamesArray sectionSelectedImages:self.segmentSelectedImageNamesArray];
    _segmentedControl.frame = self.timeAndTypeSegmentedControlArea.frame;
    _segmentedControl.segmentEdgeInset = UIEdgeInsetsMake(0, 0, 0, 0);
    [_segmentedControl setSelectionStyle:HMSegmentedControlSelectionStyleTextWidthStrip];
    [_segmentedControl setSelectionLocation:HMSegmentedControlSelectionLocationUp];
    [_segmentedControl setSelectedSegmentIndex:0];
    
    __weak QRWTopSellersDashboardViewController *weakSelf = self;
    [_segmentedControl setIndexChangeBlock:^(NSInteger index) {
        [weakSelf selectedControlEvent];
        [weakSelf.segmentedControl setSelectedSegmentIndex:index animated:YES];
    }];
    [self.view addSubview:_segmentedControl];

    self.currentSegment = @"last_login";
    
    loadingCategoriesFinished = NO;
    loadingProdusctsFinished = NO;
}


-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
}


-(void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}


- (void) setTopProducts: (QRWTopProducts *) topProducts
{
    _topProducts = topProducts;

    switch (self.segmentedControl.selectedSegmentIndex) {
        case 0:
            _productsArray = [NSArray arrayWithArray:_topProducts.lastLoginTopArray];
            break;
            
        case 1:
            _productsArray = [NSArray arrayWithArray:_topProducts.todayTopArray];
            break;
            
        case 2:
            _productsArray = [NSArray arrayWithArray:_topProducts.weekTopArray];
            break;
            
        case 3:
            _productsArray = [NSArray arrayWithArray:_topProducts.monthTopArray];
            break;
    }
    [self reloadDataInTableView];
    loadingProdusctsFinished = YES;
}

- (void) setTopCategories: (QRWTopCategories *) topCategories
{
    _topCategories = topCategories;
    switch (self.segmentedControl.selectedSegmentIndex) {
        case 0:
            _categoriesArray = [NSArray arrayWithArray:_topCategories.lastLoginTopArray];
            break;
            
        case 1:
            _categoriesArray = [NSArray arrayWithArray:_topCategories.todayTopArray];
            break;
            
        case 2:
            _categoriesArray = [NSArray arrayWithArray:_topCategories.weekTopArray];
            break;
            
        case 3:
            _categoriesArray = [NSArray arrayWithArray:_topCategories.monthTopArray];
            break;
    }
    
    [self reloadDataInTableView];
    loadingCategoriesFinished = YES;
}



- (void) reloadDataInTableView
{
    if ((_categoriesArray.count == 0) && (_productsArray.count == 0)) {
        if (loadingCategoriesFinished || loadingProdusctsFinished) {
            _topSellersTableView.hidden = YES;
            
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:NSLocalizedString(@"ERROR_ALERT_TITLE", nil)
                                                            message:NSLocalizedString(@"NO_DATA_FOR_PERIOD_ALERT_MESSAGE", nil)
                                                           delegate:nil cancelButtonTitle:NSLocalizedString(@"OK", nil)
                                                  otherButtonTitles:nil, nil];
            [alert show];
        }
    } else {
        _topSellersTableView.hidden = NO;
    }
    [_topSellersTableView reloadData];
}

- (void) selectedControlEvent
{
    loadingProdusctsFinished = NO;
    loadingCategoriesFinished = NO;
    [self setTopCategories:_topCategories];
    [self setTopProducts:_topProducts];
}


#pragma mark Table view methods

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}


- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return [_typeOfData isEqualToString:@"products"] ? _productsArray.count : _categoriesArray.count;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    return 30;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if ([_typeOfData isEqualToString:@"products"]) {
        return 70;
    } else {
        return 50;
    }
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    UIImageView *headerView = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, 320, 30)];
    if ([_typeOfData isEqualToString:@"products"]) {
        [headerView setImage:[UIImage imageNamed:@"subtitle_top10products.png"]];
    } else {
        [headerView setImage:[UIImage imageNamed:@"subtitle_top10categories.png"]];
    }
    return headerView;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if ([_typeOfData isEqualToString:@"products"]) {
        NSArray *topLevelObjects = [[NSBundle mainBundle] loadNibNamed:@"QRWTopSellersTopProductsTableViewCell" owner:self options:nil];
        QRWTopSellersTopProductsTableViewCell *cell = [topLevelObjects objectAtIndex:0];
        
        [self configureProductCell:cell atIndexPath:indexPath];
        
        return cell;
    } else {
        NSArray *topLevelObjects = [[NSBundle mainBundle] loadNibNamed:@"QRWTopSellersTopCategoriesTableViewCell" owner:self options:nil];
        QRWTopSellersTopCategoriesTableViewCell *cell = [topLevelObjects objectAtIndex:0];
        
        [self configureCategoriesCell:cell atIndexPath:indexPath];
        
        return cell;
    }
}

- (void)configureProductCell:(QRWTopSellersTopProductsTableViewCell *)cell atIndexPath:(NSIndexPath *)indexPath
{
    QRWProductInTop *productInTop = [[QRWProductInTop alloc] init];
    productInTop = [_productsArray objectAtIndex:indexPath.row];
    
    cell.countLabel.text = [NSString stringWithFormat:@"%d", [productInTop.count intValue]];
    cell.namelabel.text = productInTop.product;
    cell.codeLabel.text = [NSString stringWithFormat:@"Code: %@", productInTop.productcode];
    cell.idLabel.text = [NSString stringWithFormat:@"ID: %d", [productInTop.productid intValue]];
}

- (void)configureCategoriesCell:(QRWTopSellersTopCategoriesTableViewCell *)cell atIndexPath:(NSIndexPath *)indexPath
{
    QRWCategoryInTop *categoryInTop = [[QRWCategoryInTop alloc] init];
    categoryInTop = [_categoriesArray objectAtIndex:indexPath.row];
    
    cell.countLable.text = [NSString stringWithFormat:@"%d", [categoryInTop.count intValue]];
    cell.idLabel.text = [NSString stringWithFormat:@"ID: %d", [categoryInTop.categoryid intValue]];
    cell.nameLable.text = categoryInTop.category;
}

@end

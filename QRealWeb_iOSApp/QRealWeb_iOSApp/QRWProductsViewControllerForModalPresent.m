//
//  QRWProductsLastorderDashboardViewController.m
//  QRealWeb_iOSApp
//
//  Created by Ivan Afanasiev on 8/1/13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import "QRWProductsViewControllerForModalPresent.h"
#import "QRWProductInLastOrderCell.h"
#import "QRWProductInOrder.h"

@interface QRWProductsViewControllerForModalPresent ()

@property (nonatomic, strong) NSNumber *userID;

@end

@implementation QRWProductsViewControllerForModalPresent

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}


- (id)initWithProducts: (NSArray *) products
{
    self = [self initWithNibName:@"QRWProductsViewControllerForModalPresent" bundle:nil];
    self.productsArray = [NSArray arrayWithArray:products];
    return self;
}


- (id)initWithUserID: (NSNumber *) userID
{
    self = [self initWithNibName:@"QRWProductsViewControllerForModalPresent" bundle:nil];
    self.userID = [NSNumber numberWithInt:[userID intValue]];
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


- (void)exitButtonClicked:(id)sender
{
    [self dismissViewControllerAnimated:YES completion:nil];
}

#pragma mark Table view methods

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}


- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return _productsArray.count;
}


- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 70;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSArray *topLevelObjects = [[NSBundle mainBundle] loadNibNamed:@"QRWProductInLastOrderCell" owner:self options:nil];
    QRWProductInLastOrderCell *cell = [topLevelObjects objectAtIndex:0];
    
    [self configureProductCell:cell atIndexPath:indexPath];
    
    return cell;
}

- (void)configureProductCell:(QRWProductInLastOrderCell *)cell atIndexPath:(NSIndexPath *)indexPath
{
    QRWProductInOrder *productInTop = [[QRWProductInOrder alloc] init];
    productInTop = [_productsArray objectAtIndex:indexPath.row];
    
    cell.countLabel.text = [NSString stringWithFormat:@"%d", [productInTop.count intValue]];
    cell.namelabel.text = productInTop.product;
    cell.codeLabel.text = [NSString stringWithFormat:@"Code: %@", productInTop.productcode];
    cell.idLabel.text = [NSString stringWithFormat:@"ID: %d", [productInTop.productid intValue]];
    cell.priceLabel.text = [NSString stringWithFormat:@"%.2f$", [productInTop.price floatValue]];
}

@end

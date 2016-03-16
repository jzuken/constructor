//
//  QRWOrdersViewController.m
//  XCartAdmin
//
//  Created by Ivan Afanasiev on 09/01/14.
//  Copyright (c) 2014 Ivan Afanasiev. All rights reserved.
//

#import "QRWOrdersViewController.h"
#import "QRWOrdersCell.h"
#import "QRWOrderInfoViewController.h"
#import "QRWSettingsClient.h"
#import "NSDictionary+QRWSwap.h"

@interface QRWOrdersViewController ()

@property(strong, nonatomic) NSArray *datesTypeArray;

@end

@implementation QRWOrdersViewController


- (void)viewDidLoad
{
    [super viewDidLoad];
    self.baseCell = [QRWOrdersCell new];
    self.noResultsText = QRWLoc(@"NORES_ORDERS");
    self.datesTypeArray = @[@"today", @"week", @"month", @"all"];
    
    self.requestSearchBar.backgroundColor = kRedColor;
    self.ordersTypeSegmentedControl.tintColor = [UIColor blackColor];
    self.ordersTypeSegmentedControl.backgroundColor = kRedColor;
    [self.ordersTypeSegmentedControl addTarget:self action:@selector(segmentedControlValueDidChange) forControlEvents:UIControlEventValueChanged];
    
    self.tableView.separatorColor = kRedColor;
}


- (void)viewWillAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    [self.navigationController setNavigationBarHidden:NO animated:YES];
    [self setNavigationBarColor:kRedColor title: QRWLoc(@"ORDERS")];
    
    [self loadObjectsWithSearchString:@"" asEmptyArray:YES];
}



- (void)loadObjectsWithSearchString:(NSString *)searchString asEmptyArray:(BOOL)asEmpty
{
    [self startLoadingAnimation];
    [QRWDataManager sendLastOrderRequestWithSearchString:searchString
                                               fromPoint:asEmpty? 0 : self.dataArray.count
                                                 toPoint:kNumberOfLoadedItems
                                                  status:@""
                                                    date: self.datesTypeArray[self.ordersTypeSegmentedControl.selectedSegmentIndex]
                                                   block:^(NSArray *orders, NSError *error) {
                                                        [self smartAddObjectToDataArrayAsNew:asEmpty withLoaddedArray:orders];
                                                    }];
}



- (void)segmentedControlValueDidChange
{
    [self startLoadingAnimation];
    [QRWDataManager sendLastOrderRequestWithSearchString:self.requestSearchBar.text
                                               fromPoint:0
                                                 toPoint:kNumberOfLoadedItems
                                                  status:@""
                                                    date:self.datesTypeArray[self.ordersTypeSegmentedControl.selectedSegmentIndex]
                                                   block:^(NSArray *orders, NSError *error) {
                                                       [self smartAddObjectToDataArrayAsNew:YES withLoaddedArray:orders];
                                                   }];
}


#pragma mark - TableView

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [self.tableView deselectRowAtIndexPath:indexPath animated:YES];
    [QRWDataManager sendOrderInfoRequestWithID:[[(QRWOrder *)self.dataArray[indexPath.section] orderid] intValue] block:^(QRWOrderInfo *order, NSError *error) {
        QRWOrderInfoViewController *orderInfoViewController = [[UIStoryboard storyboardWithName:@"MainStoryboard" bundle:nil] instantiateViewControllerWithIdentifier:@"QRWOrderInfoViewController"];
        [self.navigationController pushViewController:orderInfoViewController animated:YES];
        [orderInfoViewController setOrderInfo:order];
    }];
}


- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 60;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    return 0;
}


- (void)configureCell:(UITableViewCell *)cell atIndexPath:(NSIndexPath *)indexPath
{
    QRWOrder *order = [self.dataArray objectAtIndex:indexPath.section];
    
    [cell setAccessoryType:UITableViewCellAccessoryDisclosureIndicator];
    
    [(QRWOrdersCell *)cell nameLabel].text = [NSString stringWithFormat:@"%@ %@ (#%d)", order.firstname, order.lastname, [order.orderid intValue]];
    [(QRWOrdersCell *)cell dateLabel].text = [NSString stringWithFormat:@"%@\n%@", order.month, order.day];
    [(QRWOrdersCell *)cell priceLabel].text = NSMoneyString([QRWSettingsClient getCurrency], NSStringFromFloat([order.total floatValue]));
    
    [(QRWOrdersCell *)cell statusLabel].text = [[QRWSettingsClient paymentStatusesCodeDictionary] qrw_swapKeyValue][order.status];
    [(QRWOrdersCell *)cell statusLabel].textColor = [[QRWSettingsClient statusesColors] objectForKey: order.status];
}




@end

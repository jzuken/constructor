//
//  QRWOrdersViewController.m
//  XCartAdmin
//
//  Created by Ivan Afanasiev on 09/01/14.
//  Copyright (c) 2014 Ivan Afanasiev. All rights reserved.
//

#import "QRWOrdersViewController.h"
#import "QRWOrdersCell.h"

@interface QRWOrdersViewController ()
{
    NSDictionary *_statusColorsDictionary;
}

@end

@implementation QRWOrdersViewController




- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        _statusColorsDictionary = @{@"I": [UIColor redColor],
                                    @"D": [UIColor redColor],
                                    @"F": [UIColor redColor],
                                    @"Q": [UIColor blueColor],
                                    @"B": [UIColor blueColor],
                                    @"P": kTextBlueColor,
                                    @"C": [UIColor greenColor],
                                    @"A": [UIColor blueColor],
                                    };
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    self.baseCell = [QRWOrdersCell new];
    
    self.requestSearchBar.backgroundColor = kRedColor;
    self.ordersTypeSegmentedControl.tintColor = [UIColor blackColor];
    self.ordersTypeSegmentedControl.backgroundColor = kRedColor;
    
    self.tableView.separatorColor = kRedColor;
    
    [QRWDataManager sendLastOrderRequestWithSearchString:@""
                                               fromPoint:self.dataArray.count
                                                 toPoint:kNumberOfLoadedItems
                                                  status:@""
                                                    date:@"all" block:^(NSArray *orders, NSError *error) {
                                                        NSMutableArray *oldDataArray = [NSMutableArray arrayWithArray: self.dataArray];
                                                        [oldDataArray addObjectsFromArray:orders];
                                                        self.dataArray = oldDataArray;
                                                        [self.tableView reloadData];
                                                    }];
}



- (void)viewWillAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    [self.navigationController setNavigationBarHidden:NO animated:YES];
    [self setNavigationBarColor:kRedColor title: QRWLoc(@"ORDERS")];
}




#pragma mark - TableView

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 60;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    return 0;
}


- (void)configureProductCell:(UITableViewCell *)cell atIndexPath:(NSIndexPath *)indexPath
{
    QRWOrder *order = [self.dataArray objectAtIndex:indexPath.section];
    
    [cell setAccessoryType:UITableViewCellAccessoryDisclosureIndicator];
    
    [(QRWOrdersCell *)cell nameLabel].text = [NSString stringWithFormat:@"%@ %@ (#%d)", order.firstname, order.lastname, [order.orderid intValue]];
    [(QRWOrdersCell *)cell dateLabel].text = [NSString stringWithFormat:@"%@\n%@", order.month, order.day];
    [(QRWOrdersCell *)cell priceLabel].text = NSMoneyString(@"$", NSStringFromInt([order.total intValue]));
    
    [(QRWOrdersCell *)cell statusLabel].text = QRWLoc(order.status);
    [(QRWOrdersCell *)cell statusLabel].textColor = [_statusColorsDictionary objectForKey: order.status];
}




@end

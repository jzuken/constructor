//
//  QRWOrdersInfoDashboardViewController.m
//  QRealWeb_iOSApp
//
//  Created by Иван Афанасьев on 23.07.13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import "QRWOrdersInfoDashboardViewController.h"
#import "QRWOrdersStatisticCell.h"

@interface QRWOrdersInfoDashboardViewController ()


@property (nonatomic, strong) NSDictionary *statistic;

@property (nonatomic, strong) NSArray *titlesArray;
@property (nonatomic, strong) NSArray *timesArray;

@end

@implementation QRWOrdersInfoDashboardViewController

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
    _titlesArray = @[@"C", @"P", @"F", @"D", @"I", @"X", @"Q", @"Total"];
    _timesArray = @[@"last_login", @"today", @"week", @"month"];
    
    [dataManager sendOrdersStatisticRequest];
    [self.timeAndTypeSegmentedControl removeFromSuperview];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}


#pragma mark Table view methods

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}


- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return _titlesArray.count;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    return 20;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 42;
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    UIImageView *headerView = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, 320, 30)];
    [headerView setImage:[UIImage imageNamed:@"subtitle_top10categories.png"]];
    return headerView;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSArray *topLevelObjects = [[NSBundle mainBundle] loadNibNamed:@"QRWOrdersStatisticCell" owner:self options:nil];
    QRWOrdersStatisticCell *cell = [topLevelObjects objectAtIndex:0];
    
    [self configureProductCell:cell atIndexPath:indexPath];
    
    return cell;
}

- (void)configureProductCell:(QRWOrdersStatisticCell *)cell atIndexPath:(NSIndexPath *)indexPath
{
    
//    cell.countLabel.text = [NSString stringWithFormat:@"%d", [productInTop.count intValue]];
//    cell.namelabel.text = productInTop.product;
//    cell.codeLabel.text = [NSString stringWithFormat:@"ID: %@", productInTop.productcode];
//    cell.idLabel.text = [NSString stringWithFormat:@"ID: %d", [productInTop.productid intValue]];
}



@end

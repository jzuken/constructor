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
//    [self.timeAndTypeSegmentedControl removeFromSuperview];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}


- (void)setStatistic:(NSDictionary *)statistic
{
    _statistic = [NSDictionary dictionaryWithDictionary:statistic];
    [_ordersStatisticTableView reloadData];
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

//- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
//{
//    return 20;
//}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 42;
}

//- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
//{
//    UIImageView *headerView = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, 320, 30)];
//    [headerView setImage:[UIImage imageNamed:@"subtitle_top10categories.png"]];
//    return headerView;
//}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSArray *topLevelObjects = [[NSBundle mainBundle] loadNibNamed:@"QRWOrdersStatisticCell" owner:self options:nil];
    QRWOrdersStatisticCell *cell = [topLevelObjects objectAtIndex:0];
    
    [self configureProductCell:cell atIndexPath:indexPath];
    
    return cell;
}

- (void)configureProductCell:(QRWOrdersStatisticCell *)cell atIndexPath:(NSIndexPath *)indexPath
{
    cell.nameLabel.text = NSLocalizedString(_titlesArray[indexPath.row], nil);
    cell.lastLoginLabel.text = [_statistic objectForKey:[NSString stringWithFormat:@"%@_%@", _timesArray[0], _titlesArray[indexPath.row]]];
    cell.todayLabel.text = [_statistic objectForKey:[NSString stringWithFormat:@"%@_%@", _timesArray[1], _titlesArray[indexPath.row]]];
    cell.weekLabel.text = [_statistic objectForKey:[NSString stringWithFormat:@"%@_%@", _timesArray[2], _titlesArray[indexPath.row]]];
    cell.monthLabel.text = [_statistic objectForKey:[NSString stringWithFormat:@"%@_%@", _timesArray[3], _titlesArray[indexPath.row]]];
}



@end

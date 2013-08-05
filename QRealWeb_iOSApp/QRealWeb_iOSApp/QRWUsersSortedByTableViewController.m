//
//  QRWUsersSortedByTableView.m
//  QRealWeb_iOSApp
//
//  Created by Ivan Afanasiev on 8/2/13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import "QRWUsersSortedByTableViewController.h"


@interface QRWUsersSortedByTableViewController ()

@property (nonatomic, strong) NSString *currentSort;

@property (nonatomic, strong) NSArray *avaliableSorts;
@property (nonatomic, strong) NSArray *avaliableSortsTitle;

@end

@implementation QRWUsersSortedByTableViewController


- (id)initWithCurrentSortType: (NSString *) sortType
{
    self = [self initWithNibName:@"QRWUsersSortedByTableViewController" bundle:nil];
    _currentSort = [NSString stringWithString:sortType];
    return self;
}


- (void)viewDidLoad
{
    [super viewDidLoad];
    _avaliableSorts = @[@"login_date", @"order_date", @"orders", @"none"];
    _avaliableSortsTitle = @[@"Login date", @"Order date", @"Orders", @"None"];
}

-(void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
}




#pragma mark Table view methods

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    [_sortedByTableView cellForRowAtIndexPath:[NSIndexPath indexPathForItem:[_avaliableSorts indexOfObject:_currentSort] inSection:0]].accessoryType = UITableViewCellAccessoryNone;
    _currentSort = [_avaliableSorts objectAtIndex:indexPath.row];
    [_sortedByTableView cellForRowAtIndexPath:indexPath].accessoryType = UITableViewCellAccessoryCheckmark;
    [_delegate useSortWithName:_currentSort];
}


- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}


- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return _avaliableSorts.count;
}


- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 40;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"UserCell";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault
                                      reuseIdentifier:CellIdentifier];
        if (indexPath.row == [_avaliableSorts indexOfObject:_currentSort]) {
            cell.accessoryType = UITableViewCellAccessoryCheckmark;
        } else {
            cell.accessoryType = UITableViewCellAccessoryNone;
        }
    }
    
    cell.textLabel.text = [_avaliableSortsTitle objectAtIndex:indexPath.row];
    
    return cell;
}

@end

//
//  QRWBaseViewControllerWithTableView.m
//  XCartAdmin
//
//  Created by Ivan Afanasiev on 24/12/13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import "QRWBaseViewControllerWithTableView.h"

@interface QRWBaseViewControllerWithTableView ()

@property (strong, nonatomic) UILabel *noResultsLabel;

@end



@implementation QRWBaseViewControllerWithTableView


- (void)viewDidLoad
{
    [super viewDidLoad];
    
    _statusColorsDictionary = @{@"I": [UIColor redColor],
                                @"D": [UIColor redColor],
                                @"F": [UIColor redColor],
                                @"Q": [UIColor blueColor],
                                @"B": [UIColor blueColor],
                                @"P": kTextBlueColor,
                                @"C": [UIColor greenColor],
                                @"A": [UIColor blueColor],
                                };
    
    __weak typeof(self) weakSelf = self;
    
    [self.tableView addPullToRefreshWithActionHandler:^{
        [weakSelf loadObjectsWithSearchString:weakSelf.requestSearchBar.text asEmptyArray:YES];
    }];
    [self.tableView addInfiniteScrollingWithActionHandler:^{
        [weakSelf loadObjectsWithSearchString:weakSelf.requestSearchBar.text asEmptyArray:NO];
    }];

    self.requestSearchBar.delegate = self;
}



- (void)loadObjectsWithSearchString:(NSString *)searchString asEmptyArray: (BOOL)asEmpty
{
    
}


- (void) smartAddObjectToDataArrayAsNew:(BOOL) asNew withLoaddedArray:(NSArray *)array
{
    NSMutableArray *oldDataArray = [NSMutableArray arrayWithArray: self.dataArray];
    [oldDataArray addObjectsFromArray:array];
    self.dataArray = asNew ? array: oldDataArray;
    [self.tableView reloadData];
    [self stopAllAnimations];
    self.tableView.showsInfiniteScrolling = (array.count == kNumberOfLoadedItems);
    
    if (0 == array.count) {
        [self addNoResultsLabel];
    } else {
        [self.noResultsLabel removeFromSuperview];
        self.tableView.hidden = NO;
    }
}

- (void)stopAllAnimations
{
    [self stopLoadingAnimation];
    [self.tableView.pullToRefreshView stopAnimating];
    [self.tableView.infiniteScrollingView stopAnimating];
}

- (void)addNoResultsLabel
{
    [self.noResultsLabel removeFromSuperview];
    self.noResultsLabel = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, 150, 50)];
    self.noResultsLabel.text = self.noResultsText;
    self.noResultsLabel.numberOfLines = 0;
    self.noResultsLabel.textAlignment = NSTextAlignmentCenter;
    [self.noResultsLabel setCenter:self.view.center];
    
    self.tableView.hidden = YES;
    [self.view addSubview:self.noResultsLabel];
}

#pragma mark - TableView methods


- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return [_dataArray count];
}


- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return 1;
}




- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:NSStringFromClass([_baseCell class])];
    if (cell == nil) {
        cell = [[[_baseCell class] alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:NSStringFromClass([_baseCell class])];
        NSArray *topLevelObjects = [[NSBundle mainBundle] loadNibNamed:NSStringFromClass([_baseCell class]) owner:self options:nil];
        cell = [topLevelObjects objectAtIndex:0];
    }
    
    [self configureCell:cell atIndexPath:indexPath];
    
    return cell;
}

- (void)configureCell:(UITableViewCell *)cell atIndexPath:(NSIndexPath *)indexPath
{
    
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    return 30;
}



#pragma mark - Keyboard appears/disappear methods


- (void) changeTheTableViewHeight: (CGFloat) heightChange
{
    [UIView animateWithDuration:0.2 animations:^{
        CGRect frame = self.tableView.frame;
        frame.size.height += heightChange;
        self.tableView.frame = frame;
    }];
}


#pragma mark Search bar methods

- (void)searchBarSearchButtonClicked:(UISearchBar *)searchBar;
{
    [self loadObjectsWithSearchString:searchBar.text asEmptyArray:YES];
}

- (void)searchBarTextDidBeginEditing:(UISearchBar *)searchBar
{
    searchBar.showsCancelButton = YES;
    self.tableView.scrollEnabled = NO;
}



- (void)searchBarCancelButtonClicked:(UISearchBar *) searchBar
{
    self.tableView.scrollEnabled = YES;
    if ([searchBar.text isEqualToString:@""]) {
        [self loadObjectsWithSearchString:searchBar.text asEmptyArray:NO];
    }
    [searchBar resignFirstResponder];
    searchBar.showsCancelButton = NO;
}

@end

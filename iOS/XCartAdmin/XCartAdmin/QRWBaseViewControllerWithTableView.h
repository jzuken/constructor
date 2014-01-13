//
//  QRWBaseViewControllerWithTableView.h
//  XCartAdmin
//
//  Created by Ivan Afanasiev on 24/12/13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import "QRWBaseViewController.h"
#import <SVPullToRefresh/SVPullToRefresh.h>


@interface QRWBaseViewControllerWithTableView : QRWBaseViewController<UITableViewDataSource, UITableViewDelegate, UISearchBarDelegate>

@property (nonatomic, strong) IBOutlet UITableView *tableView;

@property (strong, nonatomic) IBOutlet UISearchBar *requestSearchBar;

@property (nonatomic, strong) NSArray *dataArray;

@property (nonatomic, strong) UITableViewCell *baseCell;



- (void)configureProductCell:(UITableViewCell *)cell atIndexPath:(NSIndexPath *)indexPath;

- (void)loadObjectsWithSearchString:(NSString *)searchString asEmptyArray:(BOOL)asEmpty;

- (void)stopAllAnimations;

@end

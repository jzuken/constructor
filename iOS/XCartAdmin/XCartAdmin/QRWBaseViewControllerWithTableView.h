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
{
   NSDictionary *_statusColorsDictionary;
}

@property (nonatomic, weak) IBOutlet UITableView *tableView;

@property (nonatomic, weak) IBOutlet UISearchBar *requestSearchBar;

@property (nonatomic, copy) NSArray *dataArray;

@property (nonatomic, copy) NSString *noResultsText;

@property (nonatomic, strong) UITableViewCell *baseCell;



- (void)configureCell:(UITableViewCell *)cell atIndexPath:(NSIndexPath *)indexPath;

- (void)loadObjectsWithSearchString:(NSString *)searchString asEmptyArray:(BOOL)asEmpty;
- (void)smartAddObjectToDataArrayAsNew:(BOOL)asNew withLoaddedArray:(NSArray *)array;

- (void)stopAllAnimations;

@end

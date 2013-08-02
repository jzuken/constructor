//
//  QRWUsersSortedByTableView.h
//  QRealWeb_iOSApp
//
//  Created by Ivan Afanasiev on 8/2/13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol QRWUsersSortedByTableViewControllerDelegate <NSObject>

- (void) useSortWithName: (NSString *) sortType;

@end




@interface QRWUsersSortedByTableViewController : UIViewController<UITableViewDataSource, UITabBarDelegate>

@property (nonatomic, strong) IBOutlet UITableView *sortedByTableView;
@property (nonatomic, strong) id<QRWUsersSortedByTableViewControllerDelegate> delegate;

- (id)initWithCurrentSortType: (NSString *) sortType;
@end

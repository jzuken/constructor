//
//  QRWUsersViewController.h
//  QRealWeb_iOSApp
//
//  Created by Ivan Afanasiev on 8/2/13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import "QRWBaseViewController.h"
#import "QRWUsersSortedByTableViewController.h"

@interface QRWUsersViewController : QRWBaseViewController <UITableViewDelegate, UITableViewDataSource, UIActionSheetDelegate, QRWUsersSortedByTableViewControllerDelegate, UIScrollViewDelegate>


@property (nonatomic, strong) IBOutlet UITableView *usersTableView;

@property (strong, nonatomic) IBOutlet UIView *pointForPopoverPresent;

@end

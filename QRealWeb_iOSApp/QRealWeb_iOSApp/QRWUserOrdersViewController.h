//
//  QRWUserOrdersViewController.h
//  QRealWeb_iOSApp
//
//  Created by Ivan Afanasiev on 8/14/13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import "QRWBaseViewController.h"

@interface QRWUserOrdersViewController : QRWBaseViewController<UITableViewDelegate, UITableViewDataSource>

@property (nonatomic, strong) IBOutlet UITableView *ordersTableView;
@property (nonatomic, strong) IBOutlet UILabel *userLable;

- (id)initWithUser:(QRWUser *)user;


@end

//
//  QRWOrdersInfoDashboardViewController.h
//  QRealWeb_iOSApp
//
//  Created by Иван Афанасьев on 23.07.13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import "QRWBaseDashboardPageViewController.h"

@interface QRWOrdersInfoDashboardViewController : QRWBaseDashboardPageViewController<UITableViewDelegate, UITableViewDataSource>


@property (nonatomic, strong) IBOutlet UITableView *ordersStatisticTableView;

@end

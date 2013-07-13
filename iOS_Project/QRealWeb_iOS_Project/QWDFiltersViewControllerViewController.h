//
//  QWDFiltersViewControllerViewController.h
//  QRealWebDemoProject
//
//  Created by Ivan Afanasiev on 7/8/13.
//  Copyright (c) 2013 Иван Афанасьев. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "QWDBaseViewController.h"

@interface QWDFiltersViewControllerViewController : QWDBaseViewController<UITableViewDataSource, UITableViewDelegate>

@property (strong, nonatomic) IBOutlet UITableView *filtersTableView;

@end

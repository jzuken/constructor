//
//  QRWStatisticViewController.h
//  QRealWeb_iOSApp
//
//  Created by Ivan Afanasiev on 7/17/13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "QRWBaseViewController.h"

@interface QRWDashboardViewController : QRWBaseViewController

@property (strong, nonatomic) IBOutlet UIScrollView *dashboardPagesScrollView;

@property (strong, nonatomic) IBOutlet UISegmentedControl *timeSegmentedControl;

@end

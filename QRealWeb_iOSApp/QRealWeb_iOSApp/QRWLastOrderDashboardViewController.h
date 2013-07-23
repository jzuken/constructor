//
//  QRWLastOrderDashboardViewController.h
//  QRealWeb_iOSApp
//
//  Created by Иван Афанасьев on 23.07.13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import "QRWBaseDashboardPageViewController.h"
#import "QRWLastOrder.h"

@interface QRWLastOrderDashboardViewController : QRWBaseDashboardPageViewController

@property (nonatomic, strong) QRWLastOrder *lastOrder;

@end

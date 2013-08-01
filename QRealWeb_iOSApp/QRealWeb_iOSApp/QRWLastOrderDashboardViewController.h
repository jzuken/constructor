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

@property (strong, nonatomic) IBOutlet UILabel *IDLabel;
@property (strong, nonatomic) IBOutlet UILabel *dateLabel;
@property (strong, nonatomic) IBOutlet UIButton *productButton;
@property (strong, nonatomic) IBOutlet UILabel *totalPriceLabel;
@property (strong, nonatomic) IBOutlet UILabel *nameLabel;
@property (strong, nonatomic) IBOutlet UILabel *statusLabel;


- (void) setLastOrder:(QRWLastOrder *)lastOrder;


@end

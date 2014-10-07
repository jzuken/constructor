//
//  QRWdashboardViewController.h
//  XCartAdmin
//
//  Created by Ivan Afanasiev on 10/21/13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import "QRWBaseViewControllerWithTableView.h"

@interface QRWDashboardViewController : QRWBaseViewControllerWithTableView

@property (weak, nonatomic) IBOutlet UILabel *numberOfOrdersLable;
@property (weak, nonatomic) IBOutlet UILabel *todaySalesLabel;
@property (weak, nonatomic) IBOutlet UILabel *visitorsToday;
@property (weak, nonatomic) IBOutlet UILabel *productsSoldTodayLabel;
@property (weak, nonatomic) IBOutlet UILabel *reviewsTodayLabel;
@property (weak, nonatomic) IBOutlet UILabel *lowStockProducts;


- (IBAction)todaySalesClicked:(id)sender;
- (IBAction)visitorsTodayClicked:(id)sender;
- (IBAction)productsSoldTodayClicked:(id)sender;
- (IBAction)reviewsTodayClicked:(id)sender;
- (IBAction)lowStockProductsClicked:(id)sender;




@end

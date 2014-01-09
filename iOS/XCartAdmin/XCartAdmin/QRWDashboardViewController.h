//
//  QRWdashboardViewController.h
//  XCartAdmin
//
//  Created by Ivan Afanasiev on 10/21/13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import "QRWBaseViewControllerWithTableView.h"
#import "QRWToolView.h"

@interface QRWDashboardViewController : QRWBaseViewControllerWithTableView


//@property (nonatomic, strong) IBOutlet UITableView *todayOrderTableView;
@property (nonatomic, strong) IBOutlet UILabel *numberOfOrdersLable;


@property (strong, nonatomic) IBOutlet UILabel *todaySalesLabel;
@property (strong, nonatomic) IBOutlet UILabel *visitorsToday;
@property (strong, nonatomic) IBOutlet UILabel *productsSoldTodayLabel;
@property (strong, nonatomic) IBOutlet UILabel *reviewsTodayLabel;
@property (strong, nonatomic) IBOutlet UILabel *lowStockProducts;


- (IBAction)todaySalesClicked:(id)sender;
- (IBAction)visitorsTodayClicked:(id)sender;
- (IBAction)productsSoldTodayClicked:(id)sender;
- (IBAction)reviewsTodayClicked:(id)sender;
- (IBAction)lowStockProductsClicked:(id)sender;




@end

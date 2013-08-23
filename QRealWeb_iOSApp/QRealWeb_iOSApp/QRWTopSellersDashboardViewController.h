//
//  QRWTopSellersDashboardViewController.h
//  QRealWeb_iOSApp
//
//  Created by Иван Афанасьев on 23.07.13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import "QRWBaseDashboardPageViewController.h"

#import "QRWTopProducts.h"
#import "QRWTopCategories.h"

@interface QRWTopSellersDashboardViewController : QRWBaseDashboardPageViewController<UITableViewDataSource, UITableViewDelegate>


@property (nonatomic, strong) IBOutlet UITableView *topSellersTableView;

- (void) setTopProducts: (QRWTopProducts *) topProducts;
- (void) setTopCategories: (QRWTopCategories *) topCategories;

@property (nonatomic, strong) NSString *typeOfData;

@end

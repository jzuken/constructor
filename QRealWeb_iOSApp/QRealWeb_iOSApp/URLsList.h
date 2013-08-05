//
//  URLsList.h
//  QRealWeb_iOSApp
//
//  Created by Ivan Afanasiev on 7/23/13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import <Foundation/Foundation.h>



#pragma mark Dashboard

static NSString *const url_lastOrderURL = @"http://54.213.38.9/xcart/api.php?request=last_order";

static NSString *const url_topProductsURL = @"http://54.213.38.9/xcart/api.php?request=top_products";

static NSString *const url_topCategoriesURL = @"http://54.213.38.9/xcart/api.php?request=top_categories";

static NSString *const url_ordersStatisticURL = @"http://54.213.38.9/xcart/api.php?request=orders_statistic";

#pragma mark Users

static NSString *const url_usersURL = @"http://54.213.38.9/xcart/api.php?request=users&from=%d&size=%d&sort=%@";

#pragma mark Discounts

static NSString *const url_discountsURL = @"http://54.213.38.9/xcart/api.php?request=discounts";
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
static NSString *const url_discountsCreateURL = @"http://54.213.38.9/xcart/api.php?request=create_discount&minprice=%.2f&discount=%.2f&discount_type=%@&provider=1&membership_id=%d";
static NSString *const url_discountsEditURL = @"http://54.213.38.9/xcart/api.php?request=update_discount&id=%d&minprice=%.2f&discount=%.2f&discount_type=%@&provider=1&membership_id=%d";
static NSString *const url_discountsDeleteURL = @"http://54.213.38.9/xcart/api.php?request=delete_discount&id=%d";

#pragma mark Reviews

static NSString *const url_reviewsURL = @"http://54.213.38.9/xcart/api.php?request=reviews&from=%d&size=%d";
static NSString *const url_reviewDeleteURL = @"http://54.213.38.9/xcart/api.php?request=delete_review&id=%d";


#pragma mark Products

static NSString *const url_productsURL = @"http://54.213.38.9/xcart/api.php?request=products&from=%d&size=%d";
static NSString *const url_productsSearchURL = @"http://54.213.38.9/xcart/api.php?request=products&search_word=%@&from=%d&size=%d";
static NSString *const url_productDeleteURL = @"http://54.213.38.9/xcart/api.php?request=delete_product&id=%d";
static NSString *const url_productEditURL = @"http://54.213.38.9/xcart/api.php?request=update_product&id=%d&price=%d";
//
//  URLsList.h
//  QRealWeb_iOSApp
//
//  Created by Ivan Afanasiev on 7/23/13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import <Foundation/Foundation.h>

static NSString *const url_XCarApiURlappend = @"xcart/mobile_admin_api.php?";

static NSString *const url_developmentBaseURL = @"https://vm-constructor.cloudapp.net/";

static NSString *const url_developmentGetShopURLappend = @"/AppServerListener/api/shops/%@/ApiURL?key=%@";

static NSString *const url_configURLappend = @"?request=get_config&key=%@";

static NSString *const url_dashboardURLappend = @"?request=dashboard&key=%@";

static NSString *const url_usersURLappend = @"?request=users&from=%d&size=%d&key=%@&search=%@";
static NSString *const url_userInfoURLappend = @"?request=user_info&id=%d&key=%@";
static NSString *const url_userOrdersURLappend = @"?request=user_orders&user_id=%d&from=%d&size=%d&key=%@";

static NSString *const url_reviewsURLappend = @"?request=reviews&from=%d&size=%d&key=%@";
static NSString *const url_deleteReviewURLappend = @"?request=delete_review&id=%d&sid=%@";

static NSString *const url_productsLowStockURLappend = @"?request=products&from=%d&size=%d&low_stock=1&key=%@&search=%@";
static NSString *const url_productsURLappend = @"?request=products&from=%d&size=%d&key=%@&search=%@";
static NSString *const url_productInfoURLappend = @"?request=product_info&id=%d&key=%@";
static NSString *const url_productChangePriceURLappend = @"?request=update_product_price&id=%d&price=%@f&key=%@";
static NSString *const url_productChangeAvaliabilityURLappend = @"?request=change_available&product_id=%d&available=%d$&key=%@";


static NSString *const url_lastOrdersURLappend = @"?request=last_orders&from=%d&size=%d&status=%@&date=%@&key=%@&search=%@";
static NSString *const url_orderInfoURLappend = @"?request=order_info&id=%d&key=%@";
static NSString *const url_changeTrackingOrdersURLappend = @"?request=change_tracking&order_id=%@&tracking_number=%@&key=%@";
static NSString *const url_changeStatusOrdersURLappend = @"?request=change_status&order_id=%d&status=%@&key=%@";

static NSString *const URL_pushAppend = @"?request=register_apns&key=%@&regid=%@&model=%@&manufacturer=Apple&android_version=%@";





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



static NSString *const url_dashboardURLappend = @"xcart_pph/mobile_admin_api.php?request=dashboard&key=%@";

static NSString *const url_usersURLappend = @"xcart_pph/mobile_admin_api.php?request=users&from=%d&size=%d&key=%@&search=%@";
static NSString *const url_userInfoURLappend = @"xcart_pph/mobile_admin_api.php?request=user_info&id=%d&key=%@";
static NSString *const url_userOrdersURLappend = @"xcart_pph/mobile_admin_api.php?request=user_orders&user_id=%d&from=%d&size=%d&key=%@";

static NSString *const url_reviewsURLappend = @"xcart_pph/mobile_admin_api.php?request=reviews&from=%d&size=%d&key=%@";
static NSString *const url_deleteReviewURLappend = @"xcart_pph/mobile_admin_api.php?request=delete_review&id=%d&sid=%@";

static NSString *const url_productsLowStockURLappend = @"xcart_pph/mobile_admin_api.php?request=products&from=%d&size=%d&low_stock=1&key=%@&search=%@";
static NSString *const url_productsURLappend = @"xcart_pph/mobile_admin_api.php?request=products&from=%d&size=%d&key=%@&search=%@";
static NSString *const url_productInfoURLappend = @"xcart_pph/mobile_admin_api.php?request=product_info&id=%d&key=%@";
static NSString *const url_productChangePriceURLappend = @"xcart_pph/mobile_admin_api.php?request=update_product_price&id=%d&price=%.2f&key=%@";
static NSString *const url_productChangeAvaliabilityURLappend = @"xcart_pph/mobile_admin_api.php?request=change_available&product_id=%d&available=%d$&key=%@";


static NSString *const url_lastOrdersURLappend = @"xcart_pph/mobile_admin_api.php?request=last_orders&from=%d&size=%d&status=%@&date=%@&key=%@&search=%@";
static NSString *const url_orderInfoURLappend = @"xcart_pph/mobile_admin_api.php?request=order_info&id=%d&key=%@";




//
//  FLSDataManager.m
//  FirstLineSoftwareTestProject
//
//  Created by Иван Афанасьев on 06.04.13.
//  Copyright (c) 2013 Иван Афанасьев. All rights reserved.
//

#import "QRWDataManager.h"
#import "JSONKit.h"

#import "constants.h"
#import "URLsList.h"

#import "QRWLoginScrinViewController.h"

#import "QRWToolView.h"
#import "QRWProductsViewController.h"
#import "QRWToolsScrinViewController.h"
#import "QRWMainScrinViewController.h"

#import "QRWDashboardViewController.h"
#import "QRWUsersViewController.h"
#import "QRWDiscountsViewController.h"
#import "QRWReviewsViewController.h"

#import "QRWBaseViewController.h"

@interface QRWDataManager ()
{
    NSString *usersUrl;
    NSString *userOrderUrl;
    
    NSString *editedDiscountUrl;
    NSString *newDiscountUrl;
    NSString *deletedDiscountUrl;
    
    NSString *reviewUrl;
    NSString *deletedReviewUrl;
    
    NSString *productsUrl;
    NSString *deletedProductUrl;
    NSString *editedProductUrl;
    NSString *searchedProductUrl;
}

@end


@implementation QRWDataManager

static QRWDataManager *_instance;



-(id)init
{
    if (_instance != nil) {
        return _instance;
    }
    self = [super init];
    return self;
}

+ (QRWDataManager *)instance
{
    @synchronized(self){
        if (_instance == nil) {
            _instance = [QRWDataManager new];
        }
    }
    return _instance;
}


#pragma mark SEND REQUESTS


- (void) sendRequestUseDownloaderWithURL: (NSString *) urlString
{
    QRWDownloader *lastOrderDownloader = [[QRWDownloader alloc] initWithRequestURL:[NSURL URLWithString:urlString]];
    [lastOrderDownloader startDownloadWithDelegate:self];
}


- (void) sendRequestUseDownloaderWithURL: (NSString *) urlString parametres: (NSDictionary *) parametres
{
    QRWDownloader *lastOrderDownloader = [[QRWDownloader alloc] initWithRequestURL:[NSURL URLWithString:urlString] parametres:parametres];
    [lastOrderDownloader startDownloadWithDelegate:self];
}


#pragma mark Auth



-(void)sendAuthorizationRequestWithLogin:(NSString *)login andPassowrd:(NSString *)password
{

    [_delegate respondsForAuthRequest:[kTestUsername isEqualToString:login] && [kTestPassword isEqualToString:password]];
//    NSDictionary *parametres = [NSDictionary dictionaryWithObjects:@[login, password, [[UIDevice currentDevice].identifierForVendor UUIDString]] forKeys:@[@"name", @"pass", @"udid"]];
//    [self sendRequestUseDownloaderWithURL:url_auth parametres:parametres];
}



-(void)sendToolsRequest
{
    NSMutableArray *tools = [[NSMutableArray alloc] init];
    
    void (^actionBlock)(void) = ^{
        [[(QRWMainScrinViewController *)_delegate navigationController] pushViewController:[[QRWProductsViewController alloc] init] animated:YES];
    };
    
    void (^dashboardActionBlock)(void) = ^{
        [[(QRWMainScrinViewController *)_delegate navigationController] pushViewController:[[QRWDashboardViewController alloc] init] animated:YES];
    };
    
    void (^usersActionBlock)(void) = ^{
        [[(QRWMainScrinViewController *)_delegate navigationController] pushViewController:[[QRWUsersViewController alloc] init] animated:YES];
    };
    
    void (^discountsActionBlock)(void) = ^{
        [[(QRWMainScrinViewController *)_delegate navigationController] pushViewController:[[QRWDiscountsViewController alloc] init] animated:YES];
    };
    
    void (^reviewsActionBlock)(void) = ^{
        [[(QRWMainScrinViewController *)_delegate navigationController] pushViewController:[[QRWReviewsViewController alloc] init] animated:YES];
    };
    
    void (^logoutActionBlock)(void) = ^{
        [[NSUserDefaults standardUserDefaults] setObject:kUserDefaults_isLogOutObject forKey:kUserDefaults_isLogInKey];
        [[(QRWMainScrinViewController *)_delegate navigationController] pushViewController:[[QRWLoginScrinViewController alloc] init] animated:YES];
    };
    
    for (int index = 0; index < 6; index++) {
        QRWToolView *toolView;
        switch (index) {
            case 0:
                toolView = [[QRWToolView alloc] initWithName:@""
                                                       image:[UIImage imageNamed:@"button_orders.png"]
                                               selectedImage:[UIImage imageNamed:@"active_button_orders_blue.png"]
                                                 actionBlock:dashboardActionBlock];
                break;
            
            case 1:
                toolView = [[QRWToolView alloc] initWithName:@""
                                                       image:[UIImage imageNamed:@"usersIcon.jpg"]
                                               selectedImage:[UIImage imageNamed:@"active_button_users.png"]
                                                 actionBlock:usersActionBlock];
                break;
                
            case 2:
                toolView = [[QRWToolView alloc] initWithName:@""
                                                       image:[UIImage imageNamed:@"discountsIcon.jpg"]
                                               selectedImage:[UIImage imageNamed:@"active_button_discounts.png"]
                                                 actionBlock:discountsActionBlock];
                break;
                
            case 3:
                toolView = [[QRWToolView alloc] initWithName:@""
                                                       image:[UIImage imageNamed:@"productsIcon.jpg"]
                                               selectedImage:[UIImage imageNamed:@"active_button_products.png"]
                                                 actionBlock:actionBlock];
                break;
                
            case 4:
                toolView = [[QRWToolView alloc] initWithName:@""
                                                       image:[UIImage imageNamed:@"reviewsIcon.jpg"]
                                               selectedImage:[UIImage imageNamed:@"active_button_reviews.png"]
                                                 actionBlock:reviewsActionBlock];
                break;
                
            case 5:
                toolView = [[QRWToolView alloc] initWithName:@""
                                                       image:[UIImage imageNamed:@"logoutIcon.jpg"]
                                               selectedImage:[UIImage imageNamed:@"active_button_logout.png"]
                                                 actionBlock:logoutActionBlock];
                break;
                
            default:
                break;
        }
        [tools addObject:toolView];
    }
    
    [_delegate respondsForToolsRequest:tools];
    
}

#pragma mark Products

- (void) sendProductsRequestWithStartPoint:(NSInteger) startPoint lenght:(NSInteger) lenght
{
    productsUrl = [NSString stringWithFormat:url_productsURL, startPoint, lenght];
    [self sendRequestUseDownloaderWithURL:productsUrl];
}

- (void) sendProductsRequestWithSearchWord:(NSString *)word startPoint:(NSInteger) startPoint lenght:(NSInteger) lenght
{
    searchedProductUrl = [NSString stringWithFormat:url_productsSearchURL, word, startPoint, lenght];
    [self sendRequestUseDownloaderWithURL:searchedProductUrl];
}

- (void) uploadEditedProductWithProduct:(QRWProduct *) product
{
    editedProductUrl = [NSString stringWithFormat:url_productEditURL,[product.productid intValue], [product.price floatValue]];
    [self sendRequestUseDownloaderWithURL:editedProductUrl];
}

- (void) uploadDeletedProductWithProduct:(QRWProduct *) product
{
    deletedProductUrl = [NSString stringWithFormat:url_productDeleteURL,[product.productid intValue]];
    [self sendRequestUseDownloaderWithURL:deletedProductUrl];
}

#pragma mark Reviews

- (void) sendReviewsRequestWithStartPoint:(NSInteger) startPoint lenght:(NSInteger) lenght
{
    reviewUrl = [NSString stringWithFormat:url_reviewsURL, startPoint, lenght];
    [self sendRequestUseDownloaderWithURL:reviewUrl];
}


- (void) uploadDeletedReviewWithReview:(QRWReview *) review
{
    deletedReviewUrl = [NSString stringWithFormat:url_reviewDeleteURL, [review.review_id intValue]];
    [self sendRequestUseDownloaderWithURL:deletedReviewUrl];
}

#pragma mark Discounts

- (void) sendDiscountsRequest
{
    [self sendRequestUseDownloaderWithURL:url_discountsURL];
}


- (void) uploadNewDiscountWithDiscount:(QRWDiscount *) discount
{
    newDiscountUrl = [NSString stringWithFormat:url_discountsCreateURL, [discount.minprice floatValue], [discount.discount floatValue], discount.discountType, [discount.membershipid intValue]];
    [self sendRequestUseDownloaderWithURL:newDiscountUrl];
}


- (void) uploadEditedDiscountWithDiscount:(QRWDiscount *) discount
{
    editedDiscountUrl = [NSString stringWithFormat:url_discountsEditURL, [discount.discountid intValue], [discount.minprice floatValue], [discount.discount floatValue], discount.discountType, [discount.membershipid intValue]];
    [self sendRequestUseDownloaderWithURL:editedDiscountUrl];
}

- (void) uploadDeletedDiscountWithDiscount:(QRWDiscount *) discount
{
    editedDiscountUrl = [NSString stringWithFormat:url_discountsDeleteURL, [discount.discountid intValue]];
    [self sendRequestUseDownloaderWithURL:editedDiscountUrl];
}

#pragma mark Users


- (void) sendUsersRequestWithSort: (NSString *) sort startPoint: (NSInteger) startPoint lenght: (NSInteger) lenght
{
    usersUrl = [NSString stringWithFormat:url_usersURL, startPoint, lenght, sort];
    [self sendRequestUseDownloaderWithURL:usersUrl];
}


- (void) sendOrdersOfUserRequestWithUser:(QRWUser *) user startPoint:(NSInteger) startPoint lenght:(NSInteger) lenght
{
    userOrderUrl = [NSString stringWithFormat:url_userOrdersURL, [user.userID intValue], startPoint ,lenght];
    [self sendRequestUseDownloaderWithURL:userOrderUrl];
}

#pragma mark Dashboard


- (void) sendTopProductsRequest
{
    [self sendRequestUseDownloaderWithURL:url_topProductsURL];
}

- (void) sendTopCategoriesRequest
{
    [self sendRequestUseDownloaderWithURL:url_topCategoriesURL];
}

- (void)sendLastOrderRequest
{
    [self sendRequestUseDownloaderWithURL:url_lastOrderURL];
}

- (void)sendOrdersStatisticRequest
{
    [self sendRequestUseDownloaderWithURL:url_ordersStatisticURL];
}


//########################################################################################
//########################################################################################
//########################################################################################
#pragma mark SEND OBJECTS TO CONTROLLERS
//########################################################################################
//########################################################################################
//########################################################################################

#pragma mark Auth

- (void) sendResponseForAuthRequest:(NSDictionary *)jsonDictionary
{
    [_delegate respondsForAuthRequest:[[jsonDictionary objectForKey:@"upload_status"] isEqualToString:@"login success"]];
}


#pragma mark Products


- (void) sendResponseForProductsRequest:(NSData *)jsonObject
{
    NSError *error;
    NSArray *jsonObjects = [NSJSONSerialization JSONObjectWithData:jsonObject options:kNilOptions error:&error];
    
    NSMutableArray *products = [[NSMutableArray alloc] init];
    
    NSNumberFormatter * formatter = [[NSNumberFormatter alloc] init];
    [formatter setNumberStyle:NSNumberFormatterDecimalStyle];
    
    
    for (NSDictionary *productInJSON in jsonObjects) {
        
        NSNumberFormatter * formatter = [[NSNumberFormatter alloc] init];
        [formatter setNumberStyle:NSNumberFormatterDecimalStyle];
        
        QRWProduct *product = [[QRWProduct alloc] init];
        
        product.avaliable = [formatter numberFromString: (NSString *)[productInJSON objectForKey:@"avail"]];
        product.price = [NSNumber numberWithDouble: [(NSString *)[productInJSON objectForKey:@"list_price"] doubleValue]];
        product.productid = [formatter numberFromString: (NSString *)[productInJSON objectForKey:@"productid"]];
        product.count = [formatter numberFromString: (NSString *)[productInJSON objectForKey:@"min_amount"]];
        
        product.product = [productInJSON objectForKey:@"product"];
        product.productcode = [productInJSON objectForKey:@"productcode"];
        product.freeShiping = [productInJSON objectForKey:@"free_shipping"];
        product.provider = [productInJSON objectForKey:@"provider"];
        
        DLog(@"Price of product is: %.2f$", [product.price floatValue]);
        DLog(@"Price of product in JSON: %@$", [productInJSON objectForKey:@"list_price"]);
        
        [products addObject:product];
    }
    
    if ([_delegate respondsToSelector:@selector(respondsForProductsRequest:)]) {
        [_delegate respondsForProductsRequest:products];
    }
}

#pragma mark Review


-(void) sendResponseForReviewsRequest:(NSData *)jsonObject
{
    NSError *error;
    NSArray *jsonObjects = [NSJSONSerialization JSONObjectWithData:jsonObject options:kNilOptions error:&error];
    
    NSMutableArray *reviews = [[NSMutableArray alloc] init];
    
    NSNumberFormatter * formatter = [[NSNumberFormatter alloc] init];
    [formatter setNumberStyle:NSNumberFormatterDecimalStyle];
    
    
    for (NSDictionary *discountInJSON in jsonObjects) {
        
        NSNumberFormatter * formatter = [[NSNumberFormatter alloc] init];
        [formatter setNumberStyle:NSNumberFormatterDecimalStyle];
        
        QRWReview *review = [[QRWReview alloc] init];
        
        review.message = [discountInJSON objectForKey:@"message"];
        review.email = [discountInJSON objectForKey:@"email"];
        review.product = [discountInJSON objectForKey:@"product"];
        review.review_id = [formatter numberFromString: (NSString *)[discountInJSON objectForKey:@"review_id"]];
        
        
        [reviews addObject:review];
    }
    
    if ([_delegate respondsToSelector:@selector(respondsForReviewsRequest:)]) {
        [_delegate respondsForReviewsRequest:reviews];
    }
}

#pragma mark Discounts


-(void) sendResponseForDiscountRequest:(NSData *)jsonObject
{
    NSError *error;
    NSArray *jsonObjects = [NSJSONSerialization JSONObjectWithData:jsonObject options:kNilOptions error:&error];
    
    NSMutableArray *discounts = [[NSMutableArray alloc] init];
    
    NSNumberFormatter * formatter = [[NSNumberFormatter alloc] init];
    [formatter setNumberStyle:NSNumberFormatterDecimalStyle];

        
    for (NSDictionary *discountInJSON in jsonObjects) {
            
        NSNumberFormatter * formatter = [[NSNumberFormatter alloc] init];
        [formatter setNumberStyle:NSNumberFormatterDecimalStyle];
        
        QRWDiscount *discount = [[QRWDiscount alloc] init];
        
        discount.discountType = [discountInJSON objectForKey:@"discount_type"];
        discount.discountid = [formatter numberFromString: (NSString *)[discountInJSON objectForKey:@"discountid"]];
        discount.minprice  = [NSNumber numberWithDouble: [(NSString *)[discountInJSON objectForKey:@"minprice"] doubleValue]];
        discount.discount = [NSNumber numberWithDouble: [(NSString *)[discountInJSON objectForKey:@"discount"] doubleValue]];
        discount.provider = [formatter numberFromString: (NSString *)[discountInJSON objectForKey:@"provider"]];
        if (![@"none" isEqualToString:[discountInJSON objectForKey:@"membershipid"] ]) {
            discount.membershipid = [formatter numberFromString: (NSString *)[discountInJSON objectForKey:@"membershipid"]];
        } else {
            discount.membershipid = 0;
            
        }
        
        [discounts addObject:discount];
    }
    
    if ([_delegate respondsToSelector:@selector(respondsForDiscountsRequest:)]) {
        [_delegate respondsForDiscountsRequest:discounts];
    }
}


#pragma mark Users

-(void) sendResponseForUserOrdersRequest:(NSData *)jsonObject
{
    NSError *error;
    NSArray *jsonObjects = [NSJSONSerialization JSONObjectWithData:jsonObject options:kNilOptions error:&error];
    NSMutableArray *orders = [[NSMutableArray alloc] init];
    
    for (NSDictionary *order in jsonObjects) {
        [orders addObject:[self orderInJson:order]];
    }
    
    if ([_delegate respondsToSelector:@selector(respondsForUserOrdersRequest:)]) {
        [_delegate respondsForUserOrdersRequest: orders];
    }
}


-(void) sendResponseForUserRequest:(NSDictionary *)jsonDictionary
{
    QRWUsers *users = [[QRWUsers alloc] init];
    
    NSNumberFormatter * formatter = [[NSNumberFormatter alloc] init];
    [formatter setNumberStyle:NSNumberFormatterDecimalStyle];
    
    users.registered = [[jsonDictionary objectForKey:@"users_count"] objectForKey:@"registered"];
    users.users = [NSArray arrayWithArray:[self arrayUsersInUsersJson:jsonDictionary]];
    
    if ([_delegate respondsToSelector:@selector(respondsForUserRequest:)]) {
        [_delegate respondsForUserRequest:users];
    }
}


- (NSArray *) arrayUsersInUsersJson: (NSDictionary *)jsonDictionary
{
    NSMutableArray *products = [[NSMutableArray alloc] init];
    
    if ([@"none" isEqualToString: [jsonDictionary objectForKey:@"users"]]) {
        return nil;
    } else {
        NSArray *objectsInJson = [NSArray arrayWithArray:[jsonDictionary objectForKey:@"users"]];
        
        for (NSDictionary *product in objectsInJson) {
            
            NSNumberFormatter * formatter = [[NSNumberFormatter alloc] init];
            [formatter setNumberStyle:NSNumberFormatterDecimalStyle];
            
            QRWUser *productInLastOrder = [[QRWUser alloc] init];
            
            productInLastOrder.login = [product objectForKey:@"login"];
            productInLastOrder.username = [product objectForKey:@"username"];
            productInLastOrder.usertype = [product objectForKey:@"usertype"];
            productInLastOrder.invalidLoginAttempts = [product objectForKey:@"invalid_login_attempts"];
            productInLastOrder.title = [product objectForKey:@"title"];
            productInLastOrder.firstname = [product objectForKey:@"firstname"];
            productInLastOrder.lastname = [product objectForKey:@"lastname"];
            productInLastOrder.company = [product objectForKey:@"company"];
            productInLastOrder.email = [product objectForKey:@"email"];
            productInLastOrder.url = [product objectForKey:@"url"];
            productInLastOrder.lastLogin = [product objectForKey:@"last_login"];
            productInLastOrder.firstLogin = [product objectForKey:@"first_login"];
            productInLastOrder.status = [product objectForKey:@"status"];
            productInLastOrder.language = [product objectForKey:@"language"];
            productInLastOrder.activity = [product objectForKey:@"activity"];
            productInLastOrder.trustedProvider = [product objectForKey:@"trusted_provider"];
            productInLastOrder.ordersCount = [formatter numberFromString: (NSString *)[product objectForKey:@"orders_count"]];
            productInLastOrder.userID = [formatter numberFromString: (NSString *)[product objectForKey:@"id"]];
            
            [products addObject:productInLastOrder];
        }      
        return products;
    }
}
#pragma mark Dashboard

//#####################  DASHBOARD - Orders statistics  #####################


- (void)sendResponseForOrdersStatisticsRequest: (NSDictionary *) jsonDictionary
{
    NSMutableArray *keysArray = [[NSMutableArray alloc] init];
    NSMutableDictionary *resultsDictionary = [[NSMutableDictionary alloc] init];
    
    for (NSString *key in [jsonDictionary allKeys]) {
        NSDictionary *staticticInTime = [jsonDictionary objectForKey:key];
        for (NSString *inTimeKey in [staticticInTime allKeys]) {
            NSString *totalKey = [NSString stringWithFormat:@"%@_%@", key, inTimeKey];
            [keysArray addObject:totalKey];
            [resultsDictionary setObject:[staticticInTime objectForKey:inTimeKey] forKey:totalKey];
        }
    }
    if ([_delegate respondsToSelector:@selector(respondsForOrdersStatisticRequest:withArratOfKeys:)]) {
        [_delegate respondsForOrdersStatisticRequest:resultsDictionary withArratOfKeys:keysArray];
    }
}

//#####################  DASHBOARD - Last order  #####################

- (void)sendResponseForLastOrderRequest: (NSDictionary *) jsonDictionary
{
    if ([_delegate respondsToSelector:@selector(respondsForLastOrderRequest:)]) {
        [_delegate respondsForLastOrderRequest:[self orderInJson:jsonDictionary]];
    }
}


- (QRWLastOrder *) orderInJson: (NSDictionary *) jsonDictionary
{
    QRWLastOrder *lastOrder = [[QRWLastOrder alloc] init];
    
    NSNumberFormatter * formatter = [[NSNumberFormatter alloc] init];
    [formatter setNumberStyle:NSNumberFormatterDecimalStyle];
    
    lastOrder.lastname = [jsonDictionary objectForKey:@"lastname"];
    lastOrder.firstname = [jsonDictionary objectForKey:@"firstname"];
    lastOrder.email = [jsonDictionary objectForKey:@"email"];
    lastOrder.status = [jsonDictionary objectForKey:@"status"];
    lastOrder.date = [jsonDictionary objectForKey:@"date"];
    lastOrder.title = [jsonDictionary objectForKey:@"title"];
    
    lastOrder.orderid = [formatter numberFromString: (NSString *)[jsonDictionary objectForKey:@"orderid"]];
    lastOrder.userid = [formatter numberFromString: (NSString *)[jsonDictionary objectForKey:@"userid"]];
    lastOrder.total = [NSNumber numberWithDouble: [(NSString *)[jsonDictionary objectForKey:@"total"] doubleValue]];
    
    lastOrder.products = [NSArray arrayWithArray:[self arrayOfProductsInLastOrderInJson:jsonDictionary]];
    
    return lastOrder;
}


- (NSArray *) arrayOfProductsInLastOrderInJson: (NSDictionary *)jsonDictionary
{
    NSMutableArray *products = [[NSMutableArray alloc] init];
 
    if ([@"none" isEqualToString: [jsonDictionary objectForKey:@"details"]]) {
        return nil;
    } else {
        NSArray *objectsInJson = [NSArray arrayWithArray:[jsonDictionary objectForKey:@"details"]];
        
        for (NSDictionary *product in objectsInJson) {
            
            NSNumberFormatter * formatter = [[NSNumberFormatter alloc] init];
            [formatter setNumberStyle:NSNumberFormatterDecimalStyle];
            
            QRWProductInOrder *productInLastOrder = [[QRWProductInOrder alloc] init];
            
            productInLastOrder.product = [product objectForKey:@"product"];
            productInLastOrder.productcode = [product objectForKey:@"productcode"];
            productInLastOrder.productid = [formatter numberFromString: (NSString *)[product objectForKey:@"productid"]];
            productInLastOrder.count = [formatter numberFromString: (NSString *)[product objectForKey:@"amount"]];
            
            productInLastOrder.price = [NSNumber numberWithDouble: [(NSString *)[product objectForKey:@"price"] doubleValue]];
            productInLastOrder.provider = [product objectForKey:@"provider"];
            productInLastOrder.itemid = [formatter numberFromString: (NSString *)[product objectForKey:@"itemid"]];
            productInLastOrder.productOptions = [product objectForKey:@"product_options"];
            
            [products addObject:productInLastOrder];
        }
        
        return products;
    }
}


//#####################  DASHBOARD - TopSellers - TopProducts  ##################### 

- (void)sendResponseForTopProductsRequest: (NSDictionary *) jsonDictionary
{
    QRWTopProducts *topProducts = [[QRWTopProducts alloc] init];
    
    topProducts.todayTopArray = [NSArray arrayWithArray:[self arrayOfProductsForTag:@"today" inJson:jsonDictionary]];
    topProducts.lastLoginTopArray = [NSArray arrayWithArray:[self arrayOfProductsForTag:@"last_login" inJson:jsonDictionary]];
    topProducts.weekTopArray = [NSArray arrayWithArray:[self arrayOfProductsForTag:@"week" inJson:jsonDictionary]];
    topProducts.monthTopArray = [NSArray arrayWithArray:[self arrayOfProductsForTag:@"month" inJson:jsonDictionary]];
    
    if ([_delegate respondsToSelector:@selector(respondsForTopProductsRequest:)]) {
        [_delegate respondsForTopProductsRequest:topProducts];
    }
}

- (NSArray *) arrayOfProductsForTag: (NSString *) tag inJson: (NSDictionary *)jsonDictionary
{
    NSMutableArray *productsFortTag = [[NSMutableArray alloc] init];
    
    
    if ([@"none" isEqualToString: [jsonDictionary objectForKey:tag]]) {
        return nil;
    } else {
        NSArray *objectsInJson = [NSArray arrayWithArray:[jsonDictionary objectForKey:tag]];

        for (NSDictionary *product in objectsInJson) {
            
            NSNumberFormatter * formatter = [[NSNumberFormatter alloc] init];
            [formatter setNumberStyle:NSNumberFormatterDecimalStyle];
            
            QRWProductInTop *topProductInTop = [[QRWProductInTop alloc] init];
            
            topProductInTop.product = [product objectForKey:@"product"];
            topProductInTop.productcode = [product objectForKey:@"productcode"];
            topProductInTop.productid = [formatter numberFromString: (NSString *)[product objectForKey:@"productid"]];
            topProductInTop.count = [formatter numberFromString: (NSString *)[product objectForKey:@"count"]];
            
            
            [productsFortTag addObject:topProductInTop];
        }
        
        return productsFortTag;
    }
}



//#####################  DASHBOARD - TopSellers - TopCategories  ##################### 

- (void)sendResponseForTopCategoriesRequest: (NSDictionary *) jsonDictionary
{
    QRWTopCategories *topCateroies = [[QRWTopCategories alloc] init];
    
    topCateroies.todayTopArray = [NSArray arrayWithArray:[self arrayOfCategoriesForTag:@"today" inJson:jsonDictionary]];
    topCateroies.lastLoginTopArray = [NSArray arrayWithArray:[self arrayOfCategoriesForTag:@"last_login" inJson:jsonDictionary]];
    topCateroies.weekTopArray = [NSArray arrayWithArray:[self arrayOfCategoriesForTag:@"week" inJson:jsonDictionary]];
    topCateroies.monthTopArray = [NSArray arrayWithArray:[self arrayOfCategoriesForTag:@"month" inJson:jsonDictionary]];
    
    if ([_delegate respondsToSelector:@selector(respondsForTopCategoriesRequest:)]) {
        [_delegate respondsForTopCategoriesRequest:topCateroies];
    }
}

- (NSArray *) arrayOfCategoriesForTag: (NSString *) tag inJson: (NSDictionary *)jsonDictionary
{
    NSMutableArray *productsFortTag = [[NSMutableArray alloc] init];
    
    
    if ([@"none" isEqualToString: [jsonDictionary objectForKey:tag]]) {
        return nil;
    } else {
        NSArray *objectsInJson = [NSArray arrayWithArray:[jsonDictionary objectForKey:tag]];
        
        for (NSDictionary *category in objectsInJson) {
            
            NSNumberFormatter * formatter = [[NSNumberFormatter alloc] init];
            [formatter setNumberStyle:NSNumberFormatterDecimalStyle];
            
            QRWCategoryInTop *topCategoryInTop = [[QRWCategoryInTop alloc] init];
            
            topCategoryInTop.category = [category objectForKey:@"category"];
            topCategoryInTop.categoryid = [formatter numberFromString: (NSString *)[category objectForKey:@"categoryid"]];
            topCategoryInTop.count = [formatter numberFromString: (NSString *)[category objectForKey:@"count"]];
            
            [productsFortTag addObject:topCategoryInTop];
        }
        
        return productsFortTag;
    }
}

#pragma mark UPLOAD

- (void) sendUploadStatus:(NSDictionary *)jsonDictionary
{
    if ([_delegate respondsToSelector:@selector(respondsForUploadingRequest:)]) {
        [_delegate respondsForUploadingRequest:[@"1" isEqualToString:[jsonDictionary objectForKey:@"upload_status"]]];
    }
}


#pragma mark NSURLConnection methods

- (void)downloadWasFinishedWithData:(NSMutableData *)jsonData forRequestURL:(NSURL *)requesrURL
{
    NSError *error;
    NSMutableString *result = [[NSMutableString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
    DLog(@"Receive data: %@", result);
    
    DLog(@"Is valid JSON %@ for URL: %@", ([NSJSONSerialization isValidJSONObject:jsonData] ? @"YES" : @"NO"), requesrURL.absoluteString);
    
    NSDictionary *jsonDictionary = [NSJSONSerialization JSONObjectWithData:jsonData options:kNilOptions error:&error];
    
    
    if ([url_lastOrderURL isEqualToString:requesrURL.absoluteString]) {
        [self sendResponseForLastOrderRequest: jsonDictionary];
    }
    
    if ([url_topProductsURL isEqualToString:requesrURL.absoluteString]) {
        [self sendResponseForTopProductsRequest:jsonDictionary];
    }
    
    if ([url_topCategoriesURL isEqualToString:requesrURL.absoluteString]) {
        [self sendResponseForTopCategoriesRequest:jsonDictionary];
    }
    
    if ([url_ordersStatisticURL isEqualToString:requesrURL.absoluteString]) {
        [self sendResponseForOrdersStatisticsRequest:jsonDictionary];
    }
    
    if ([usersUrl isEqualToString:requesrURL.absoluteString]) {
        [self sendResponseForUserRequest:jsonDictionary];
    }
    
    if ([userOrderUrl isEqualToString:requesrURL.absoluteString]) {
        [self sendResponseForUserOrdersRequest:jsonData];
    }
    
    if ([url_discountsURL isEqualToString:requesrURL.absoluteString]) {
        [self sendResponseForDiscountRequest:jsonData];
    }
    
    if ([reviewUrl isEqualToString:requesrURL.absoluteString]) {
        [self sendResponseForReviewsRequest:jsonData];
    }
    
    if ([url_auth isEqualToString:requesrURL.absoluteString]) {
        [self sendResponseForAuthRequest:jsonDictionary];
    }
    
    if ([productsUrl isEqualToString:requesrURL.absoluteString] || [searchedProductUrl isEqualToString:requesrURL.absoluteString]) {
        [self sendResponseForProductsRequest:jsonData];
    }
    
    if ([newDiscountUrl isEqualToString:requesrURL.absoluteString] ||
        [editedDiscountUrl isEqualToString:requesrURL.absoluteString] ||
        [deletedDiscountUrl isEqualToString:requesrURL.absoluteString] ||
        [deletedReviewUrl isEqualToString:requesrURL.absoluteString] ||
        [deletedProductUrl isEqualToString:requesrURL.absoluteString] ||
        [editedProductUrl isEqualToString:requesrURL.absoluteString]) {
        
        [self sendUploadStatus:jsonDictionary];
    }
    
}


- (void)downloadWasFailedWithError:(NSError *)error forRequestURL:(NSURL *)requesrURL
{
    [(QRWBaseViewController *)_delegate stopLoadingAnimation];
}



@end


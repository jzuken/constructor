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

#import "QRWToolView.h"
#import "QRWProductsViewController.h"
#import "QRWToolsScrinViewController.h"
#import "QRWMainScrinViewController.h"

#import "QRWDashboardViewController.h"

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


#pragma mark Auth

-(void)sendAuthorizationRequestWithLogin:(NSString *)login andPassowrd:(NSString *)password
{
    [_delegate respondsForAuthRequest:([kTestUsername isEqualToString:login] && [kTestPassword isEqualToString:password])];

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
    
    for (int index = 0; index < 6; index++) {
        QRWToolView *toolView;
        switch (index) {
            case 0:
                toolView = [[QRWToolView alloc] initWithName:@"" image:[UIImage imageNamed:@"dashboardIcon.jpg"] actionBlock:dashboardActionBlock];
                break;
            
            case 1:
                toolView = [[QRWToolView alloc] initWithName:@"" image:[UIImage imageNamed:@"usersIcon.jpg"] actionBlock:actionBlock];
                break;
                
            case 2:
                toolView = [[QRWToolView alloc] initWithName:@"" image:[UIImage imageNamed:@"discountsIcon.jpg"] actionBlock:actionBlock];
                break;
                
            case 3:
                toolView = [[QRWToolView alloc] initWithName:@"" image:[UIImage imageNamed:@"productsIcon.jpg"] actionBlock:actionBlock];
                break;
                
            case 4:
                toolView = [[QRWToolView alloc] initWithName:@"" image:[UIImage imageNamed:@"reviewsIcon.jpg"] actionBlock:actionBlock];
                break;
                
            case 5:
                toolView = [[QRWToolView alloc] initWithName:@"" image:[UIImage imageNamed:@"logoutIcon.jpg"] actionBlock:actionBlock];
                break;
                
            default:
                break;
        }
        [tools addObject:toolView];
    }
    
    [_delegate respondsForToolsRequest:tools];
    
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

#pragma mark SEND OBJECTS TO CONTROLLERS

#pragma mark Dashboard

//#####################  DASHBOARD - Last order  #####################





- (void)sendOrdersStatisticsResponse: (NSDictionary *) jsonDictionary
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
    
    [_delegate respondsForOrdersStatisticRequest:resultsDictionary withArratOfKeys:keysArray];
}

//#####################  DASHBOARD - Last order  #####################

- (void)sendLastOrderResponse: (NSDictionary *) jsonDictionary
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
    lastOrder.total = [formatter numberFromString: (NSString *)[jsonDictionary objectForKey:@"total"]];
    
    lastOrder.products = [NSArray arrayWithArray:[self arrayOfProductsInLastOrderInJson:jsonDictionary]];
    
    [_delegate respondsForLastOrderRequest:lastOrder];
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
            
            productInLastOrder.price = [formatter numberFromString: (NSString *)[product objectForKey:@"price"]];
            productInLastOrder.provider = [product objectForKey:@"provider"];
            productInLastOrder.itemid = [formatter numberFromString: (NSString *)[product objectForKey:@"itemid"]];
            productInLastOrder.productOptions = [product objectForKey:@"product_options"];
            
            [products addObject:productInLastOrder];
        }
        
        return products;
    }
}


//#####################  DASHBOARD - TopSellers - TopProducts  ##################### 

- (void)sendTopProductsResponse: (NSDictionary *) jsonDictionary
{
    QRWTopProducts *topProducts = [[QRWTopProducts alloc] init];
    
    topProducts.todayTopArray = [NSArray arrayWithArray:[self arrayOfProductsForTag:@"today" inJson:jsonDictionary]];
    topProducts.lastLoginTopArray = [NSArray arrayWithArray:[self arrayOfProductsForTag:@"last_login" inJson:jsonDictionary]];
    topProducts.weekTopArray = [NSArray arrayWithArray:[self arrayOfProductsForTag:@"week" inJson:jsonDictionary]];
    topProducts.monthTopArray = [NSArray arrayWithArray:[self arrayOfProductsForTag:@"month" inJson:jsonDictionary]];
       
    [_delegate respondsForTopProductsRequest:topProducts];
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

- (void)sendTopCategoriesResponse: (NSDictionary *) jsonDictionary
{
    QRWTopCategories *topCateroies = [[QRWTopCategories alloc] init];
    
    topCateroies.todayTopArray = [NSArray arrayWithArray:[self arrayOfCategoriesForTag:@"today" inJson:jsonDictionary]];
    topCateroies.lastLoginTopArray = [NSArray arrayWithArray:[self arrayOfCategoriesForTag:@"last_login" inJson:jsonDictionary]];
    topCateroies.weekTopArray = [NSArray arrayWithArray:[self arrayOfCategoriesForTag:@"week" inJson:jsonDictionary]];
    topCateroies.monthTopArray = [NSArray arrayWithArray:[self arrayOfCategoriesForTag:@"month" inJson:jsonDictionary]];
    
    [_delegate respondsForTopCategoriesRequest:topCateroies];
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


#pragma mark NSURLConnection methods

- (void)downloadWasFinishedWithData:(NSMutableData *)jsonData forRequestURL:(NSURL *)requesrURL
{
    NSError *error;
    NSMutableString *result = [[NSMutableString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
    DLog(@"Receive data: %@", result);
    
    DLog(@"Is valid JSON %@ for URL: %@", ([NSJSONSerialization isValidJSONObject:jsonData] ? @"YES" : @"NO"), requesrURL.absoluteString);
    
    NSDictionary *jsonDictionary = [NSJSONSerialization JSONObjectWithData:jsonData options:kNilOptions error:&error];
    
    if ([url_lastOrderURL isEqualToString:requesrURL.absoluteString]) {
        [self sendLastOrderResponse: jsonDictionary];
    }
    
    if ([url_topProductsURL isEqualToString:requesrURL.absoluteString]) {
        [self sendTopProductsResponse:jsonDictionary];
    }
    
    if ([url_topCategoriesURL isEqualToString:requesrURL.absoluteString]) {
        [self sendTopCategoriesResponse:jsonDictionary];
    }
    
    if ([url_ordersStatisticURL isEqualToString:requesrURL.absoluteString]) {
        [self sendOrdersStatisticsResponse:jsonDictionary];
    }
}


- (void)downloadWasFailedWithError:(NSError *)error forRequestURL:(NSURL *)requesrURL
{
    
}



@end


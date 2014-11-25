//
//  FLSDataManager.m
//  FirstLineSoftwareTestProject
//
//  Created by Иван Афанасьев on 06.04.13.
//  Copyright (c) 2013 Иван Афанасьев. All rights reserved.
//

#import "QRWDataManager.h"
#import "QRWHTTPClient.h"
#import "QRWSettingsClient.h"
#import "constants.h"
#import "URLsList.h"
#import "QRWBaseViewController.h"


@implementation QRWDataManager


+ (NSURLSessionDataTask *)sendAuthorizationRequestWithLogin:(NSString *)login
                                                andPassowrd:(NSString *)password
                                                      block:(void (^)(BOOL isAuth, NSString *desription, NSError *error))block;
{
    NSString *getShop = [NSString stringWithFormat:url_developmentGetShopURLappend, login, password];

    return [[QRWHTTPClient sharedDevelopmentClient] GET:getShop
                                             parameters:nil
                                                success:^(NSURLSessionDataTask *__unused task, id JSON) {
                                                    DLog(@"JSON from dev server is: %@", JSON);
                                                    DLog(@"dev server request is: %@", task.currentRequest.URL);
                                                     NSError *localError = nil;
                                                     NSDictionary *parsedObject = [NSJSONSerialization JSONObjectWithData:JSON options:0 error:&localError];

                                                     if (block) {
                                                         [@"wrongKey" isEqual:[parsedObject objectForKey:@"api"]] ? block(NO, @"", nil) : block(YES, [parsedObject objectForKey:@"url"], nil);
                                                     }
                                                 }
                                                failure:^(NSURLSessionDataTask *__unused task, NSError *error) {
                                                     DLog(@"Error: %@", error);
                                                     if (block) {
                                                         block(NO, @"", error);
                                                     }
                                                 }];
}


+ (NSURLSessionDataTask *)checkTheSubscriptionStatusWithSuccessBlock:(void (^)(NSString *status))successBlock
{
    NSString *getShop = [NSString stringWithFormat:url_developmentGetChecksubscription, [QRWSettingsClient getLogin]];
    
    return [[QRWHTTPClient sharedDevelopmentClient] GET:getShop
                                             parameters:nil
                                                success:^(NSURLSessionDataTask *__unused task, id JSON) {
                                                    DLog(@"JSON from dev server is: %@", JSON);
                                                    DLog(@"dev server request is: %@", task.currentRequest.URL);
                                                    NSError *localError = nil;
                                                    NSDictionary *parsedObject = [NSJSONSerialization JSONObjectWithData:JSON options:0 error:&localError];
                                                    
                                                    if (successBlock) {
                                                        successBlock([parsedObject objectForKey:@"subscribed"]);
                                                    }
                                                }
                                                failure:^(NSURLSessionDataTask *__unused task, NSError *error) {
                                                    DLog(@"Error: %@", error);
                                                    if (successBlock) {
                                                        successBlock(nil);
                                                    }
                                                }];
}

+ (NSURLSessionDataTask *)sendConfigRequestWithBlock:(void (^)(NSString *XCartVersion, NSError *error))block
{
    return [self sendRequestWithURL:[NSString stringWithFormat:url_configURLappend, [QRWSettingsClient getSecurityKey]]
                            success:^(NSURLSessionDataTask *__unused task, id JSON) {
                                DLog(@"JSON from dev server is: %@", JSON);
                                DLog(@"Registration server request is: %@", task.currentRequest.URL);
                                
                                if (block) {
                                    block([JSON objectForKey:@"General:store_engine"], nil);
                                }
                                
                                NSDictionary *allStatuses = [JSON objectForKey:@"Order:statuses"] ;
                                if ([[JSON objectForKey:@"General:store_engine"] isEqual:@"XCart4"]) {
                                    [QRWSettingsClient setPaymentStatuses:[[allStatuses objectForKey:@"status"] valueForKey:@"name"]
                                     codes:[[allStatuses objectForKey:@"status"] valueForKey:@"code"]];
                                } else {
                                    [QRWSettingsClient setPaymentStatuses:[[allStatuses objectForKey:@"payment_status"] valueForKey:@"name"] codes:[[allStatuses objectForKey:@"payment_status"] valueForKey:@"code"]];
                                    [QRWSettingsClient setShippingStatuses:[[allStatuses objectForKey:@"fulfilment_status"] valueForKey:@"name"] codes:[[allStatuses objectForKey:@"fulfilment_status"] valueForKey:@"code"]];
                                }
                            }
                            failure:^(NSURLSessionDataTask *__unused task, NSError *error) {
                                DLog(@"Error: %@", error);
                                DLog(@"dev server request is: %@", task.currentRequest.URL);
                                if (block) {
                                    block(@"", error);
                                }
                            }];
}


+ (NSURLSessionDataTask *)sendPushTokenAuthorization:(NSString *)token
                                               block:(void (^)(BOOL isAuth, NSError *error))block
{
    NSString *getShop = [NSString stringWithFormat:URL_pushAppend, [QRWSettingsClient getSecurityKey], token, [[UIDevice currentDevice] model], [[UIDevice currentDevice] systemVersion]];
    
    return [self sendRequestWithURL:getShop
                            success:^(NSURLSessionDataTask *__unused task, id JSON) {
                                DLog(@"Answer for registration is: %@", JSON);
                                if (block) {
                                    block(YES, nil);
                                }
                            }
                            failure:^(NSURLSessionDataTask *__unused task, NSError *error) {
                                DLog(@"Error: %@", error);
                                if (block) {
                                    block(NO, error);
                                }
                            }];
}

#pragma mark - Dashboard


+ (NSURLSessionDataTask *)sendDashboardRequestWithBlock:(void (^)(QRWDashboardEntety *dashboardEntety, NSError *error))block
{
    NSString *getURL = [NSString stringWithFormat:url_dashboardURLappend, [QRWSettingsClient getSecurityKey]];

    return [self sendRequestWithURL:getURL
                            success:^(NSURLSessionDataTask *__unused task, id JSON) {
                                QRWDashboardEntety *dashboardEntety = [QRWDashboardEntety new];
                                DLog(@"Json is: %@", JSON);
                                [dashboardEntety buildDataByJson:(NSDictionary *) JSON];

                                if (block) {
                                    block(dashboardEntety, nil);
                                }
                            }
                            failure:^(NSURLSessionDataTask *__unused task, NSError *error) {
                                DLog(@"Error: %@", error);
                                if (block) {
                                    block([QRWDashboardEntety new], error);
                                }
                            }];
}


#pragma mark - Orders

+ (NSURLSessionDataTask *)sendLastOrderRequestWithSearchString: (NSString *)searchString
                                                     fromPoint: (NSInteger)startPoint
                                                       toPoint: (NSInteger)finishPoint
                                                        status: (NSString *)status
                                                          date: (NSString *)date
                                                         block: (void (^)(NSArray *orders, NSError *error))block
{
    NSString *getURL = [NSString stringWithFormat:url_lastOrdersURLappend, (int)startPoint, (int)finishPoint, status, date, [QRWSettingsClient getSecurityKey], searchString];
    
    return [self sendRequestWithURL:getURL
                            success:^(NSURLSessionDataTask *__unused task, id JSON) {
                                DLog(@"Json is: %@", JSON);
                                NSMutableArray *orders = [NSMutableArray new];
                                NSArray *ordersArray = (NSArray *) JSON;
                                for (NSDictionary *data in ordersArray) {
                                    QRWOrder *order = [QRWOrder new];
                                    [order buildDataByJson:data];
                                    [orders addObject:order];
                                }
                                if (block) {
                                    block(orders, nil);
                                }
                            }
                            failure:^(NSURLSessionDataTask *__unused task, NSError *error) {
                                DLog(@"Error: %@", error);
                                if (block) {
                                    block([NSArray array], error);
                                }
                            }];
}

+ (NSURLSessionDataTask *)sendOrderInfoRequestWithID:(NSInteger)orderID
                                               block:(void (^)(QRWOrderInfo *order, NSError *error))block
{
    NSString *getURL = [NSString stringWithFormat:url_orderInfoURLappend, (int)orderID, [QRWSettingsClient getSecurityKey]];
    
    return [self sendRequestWithURL:getURL
                            success:^(NSURLSessionDataTask *__unused task, id JSON) {
                                DLog(@"Json is: %@", JSON);
                                QRWOrderInfo *orderInfo = [QRWOrderInfo new];
                                [orderInfo buildDataByJson:JSON];
                                
                                if (block) {
                                    block(orderInfo, nil);
                                }
                            }
                            failure:^(NSURLSessionDataTask *__unused task, NSError *error) {
                                DLog(@"Error: %@", error);
                                if (block) {
                                    block([QRWOrderInfo new], error);
                                }
                            }];
}


+ (NSURLSessionDataTask *)sendOrderChangeTrackingNumberRequestWithID:(NSString *)orderID
                                                      trackingNumber:(NSString *)trackingNumber
                                                               block:(void (^)(BOOL isSuccess, NSError *error))block
    {
        NSString *getURL = [NSString stringWithFormat:url_changeTrackingOrdersURLappend, orderID, trackingNumber, [QRWSettingsClient getSecurityKey]];
        
        return [self sendRequestWithURL:getURL
                                success:^(NSURLSessionDataTask *__unused task, id JSON) {
                                    DLog(@"Json is: %@", JSON);
                                    if (block) {
                                        block(YES, nil);
                                    }
                                }
                                failure:^(NSURLSessionDataTask *__unused task, NSError *error) {
                                    DLog(@"Error: %@", error);
                                    if (block) {
                                        block(NO, error);
                                    }
                                }];
}


+ (NSURLSessionDataTask *)sendOrderChangeStatusRequestWithID:(NSInteger)orderID
                                                  pphDetails:(NSString *)pphDetails
                                                      status:(NSString *)status
                                               paymentStatus:(NSString *)paymentStatus
                                              shippingStatus:(NSString *)shippingStatus
                                                       block:(void (^)(BOOL isSuccess, NSError *error))block
{
    NSString *getURL = [NSString stringWithFormat:url_changeStatusOrdersURLappend,
                        (int)orderID,
                        pphDetails,
                        [QRWSettingsClient paymentStatusesCodeDictionary][status],
                        [QRWSettingsClient getSecurityKey],
                        [QRWSettingsClient paymentStatusesCodeDictionary][paymentStatus],
                        [QRWSettingsClient shippingStatusesCodeDictionary][shippingStatus]];
    
    return [self sendRequestWithURL:getURL
                            success:^(NSURLSessionDataTask *__unused task, id JSON) {
                                DLog(@"Json is: %@", JSON);
                                if (block) {
                                    block(YES, nil);
                                }
                            }
                            failure:^(NSURLSessionDataTask *__unused task, NSError *error) {
                                DLog(@"Error: %@", error);
                                if (block) {
                                    block(NO, error);
                                }
                            }];
}

#pragma mark - Products


+ (NSURLSessionDataTask *)sendProductChangeAvaliabilityRequestWithID: (NSInteger)productID
                                                         isAvaliable: (BOOL) isAvaliable
                                                               block: (void (^)(BOOL isSuccess, NSError *error))block
{
    NSString *getURL = [NSString stringWithFormat:url_productChangeAvaliabilityURLappend, (int)productID, isAvaliable ? 1: 2, [QRWSettingsClient getSecurityKey]];
    
    return [self sendRequestWithURL:getURL
                            success:^(NSURLSessionDataTask *__unused task, id JSON) {
                                DLog(@"Json is: %@", JSON);
                                if (block) {
                                    block(YES, nil);
                                }
                            }
                            failure:^(NSURLSessionDataTask *__unused task, NSError *error) {
                                DLog(@"Error: %@", error);
                                if (block) {
                                    block(NO, error);
                                }
                            }];
}

+ (NSURLSessionDataTask *)sendProductChangePriceRequestWithID:(NSInteger)productID
                                                    variantID:(NSString *)variantID
                                                     newPrice:(NSString *)newPrice
                                                        block:(void (^)(BOOL, NSError *))block
{
    NSString *getURL = variantID ?
    [NSString stringWithFormat:url_productChangeVariantPriceURLappend, (int)productID, newPrice, [QRWSettingsClient getSecurityKey], variantID]:
    [NSString stringWithFormat:url_productChangePriceURLappend, (int)productID, newPrice, [QRWSettingsClient getSecurityKey]];
    
    return [self sendRequestWithURL:getURL
                            success:^(NSURLSessionDataTask *__unused task, id JSON) {
                                DLog(@"Json is: %@", JSON);
                                if (block) {
                                    block(YES, nil);
                                }
                            }
                            failure:^(NSURLSessionDataTask *__unused task, NSError *error) {
                                DLog(@"Error: %@", error);
                                if (block) {
                                    block(NO, error);
                                }
                            }];
}


+ (NSURLSessionDataTask *)sendProductsRequestWithSearchString:(NSString *)searchString
                                                    fromPoint:(NSInteger)startPoint
                                                      toPoint:(NSInteger)finishPoint
                                                     lowStock:(BOOL)isLowStock
                                                        block:(void (^)(NSArray *products, NSError *error))block;
{
    NSString *getURL = [NSString stringWithFormat:(isLowStock ? url_productsLowStockURLappend : url_productsURLappend),
                        (int)startPoint, (int)finishPoint,
                        [QRWSettingsClient getSecurityKey], searchString];

    return [self sendRequestWithURL:getURL
                            success:^(NSURLSessionDataTask *__unused task, id JSON) {
                                NSMutableArray *products = [NSMutableArray new];
                                DLog(@"Json is: %@", JSON);
                                NSArray *productsArray = (NSArray *) JSON;
                                for (NSDictionary *data in productsArray) {
                                    QRWProduct *product = [QRWProduct new];
                                    [product buildDataByJson:data];
                                    [products addObject:product];
                                }
                                if (block) {
                                    block(products, nil);
                                }
                            }
                            failure:^(NSURLSessionDataTask *__unused task, NSError *error) {
                                DLog(@"Error: %@", error);
                                if (block) {
                                    block([NSArray array], error);
                                }
                            }];
}

+ (NSURLSessionDataTask *)sendProductInfoRequestWithID:(NSInteger)productID
                                               block:(void (^)(QRWProductWithInfo *product, NSError *error))block
{
    NSString *getURL = [NSString stringWithFormat:url_productInfoURLappend, (int)productID, [QRWSettingsClient getSecurityKey]];
    
    return [self sendRequestWithURL:getURL
                            success:^(NSURLSessionDataTask *__unused task, id JSON) {
                                QRWProductWithInfo *product = [QRWProductWithInfo new];
                                DLog(@"Json is: %@", JSON);
                                [product buildDataByJson:JSON];
                                if (block) {
                                    block(product, nil);
                                }
                            }
                            failure:^(NSURLSessionDataTask *__unused task, NSError *error) {
                                DLog(@"Error: %@", error);
                                if (block) {
                                    block([QRWProductWithInfo new], error);
                                }
                            }];
}


#pragma mark - Users


+ (NSURLSessionDataTask *)sendUserRequestWithSearchString:(NSString *)searchString
                                                fromPoint:(NSInteger)startPoint
                                                  toPoint:(NSInteger)finishPoint
                                                    block:(void (^)(NSArray *users, NSError *error))block
{
    NSString *getURL = [NSString stringWithFormat:url_usersURLappend, (int)startPoint, (int)finishPoint, [QRWSettingsClient getSecurityKey], searchString];

    return [self sendRequestWithURL:getURL
                            success:^(NSURLSessionDataTask *__unused task, id JSON) {
                                NSMutableArray *users = [NSMutableArray new];
                                NSArray *usersArray = (NSArray *) JSON;
                                for (NSDictionary *data in usersArray) {
                                    DLog(@"Json is: %@", data);
                                    QRWUser *user = [QRWUser new];
                                    [user buildDataByJson:data];
                                    [users addObject:user];
                                }
                                if (block) {
                                    block(users, nil);
                                }
                            }
                            failure:^(NSURLSessionDataTask *__unused task, NSError *error) {
                                DLog(@"Error: %@", error);
                                if (block) {
                                    block([NSArray array], error);
                                }
                            }];
}


+ (NSURLSessionDataTask *)sendUserInfoRequestWithID:(NSInteger)userID
                                              block: (void (^)(QRWUserInfo *userInfo, NSError *error))block;
{
    NSString *getURL = [NSString stringWithFormat:url_userInfoURLappend, (int)userID, [QRWSettingsClient getSecurityKey]];

    return [self sendRequestWithURL:getURL
                            success:^(NSURLSessionDataTask *__unused task, id JSON) {
                                DLog(@"Json is: %@", JSON);
                                QRWUserInfo *userInfo = [QRWUserInfo new];
                                [userInfo buildDataByJson:JSON];
                                if (block) {
                                    block(userInfo, nil);
                                }
                            }
                            failure:^(NSURLSessionDataTask *__unused task, NSError *error) {
                                DLog(@"Error: %@", error);
                                if (block) {
                                    block([QRWUserInfo new], error);
                                }
                            }];
}



+ (NSURLSessionDataTask *)sendUserOrderRequestWithUserID: (NSInteger )userID
                                               fromPoint: (NSInteger)startPoint
                                                 toPoint: (NSInteger)finishPoint
                                                   block: (void (^)(NSArray *orders, NSError *error))block
{
    NSString *getURL = [NSString stringWithFormat:url_userOrdersURLappend, (int)userID, (int)startPoint, (int)finishPoint, [QRWSettingsClient getSecurityKey]];
    
    return [self sendRequestWithURL:getURL
                            success:^(NSURLSessionDataTask *__unused task, id JSON) {
                                DLog(@"Json is: %@", JSON);
                                NSMutableArray *orders = [NSMutableArray new];
                                NSArray *ordersArray = (NSArray *) JSON;
                                for (NSDictionary *data in ordersArray) {
                                    QRWOrder *order = [QRWOrder new];
                                    [order buildDataByJson:data];
                                    [orders addObject:order];
                                }
                                if (block) {
                                    block(orders, nil);
                                }
                            }
                            failure:^(NSURLSessionDataTask *__unused task, NSError *error) {
                                DLog(@"Error: %@", error);
                                if (block) {
                                    block([NSArray array], error);
                                }
                            }];
}


#pragma mark - Reviews

+ (NSURLSessionDataTask *)sendReviewsRequestFromPoint:(NSInteger)startPoint
                                              toPoint:(NSInteger)finishPoint
                                                block:(void (^)(NSArray *reviews, NSError *error))block
{
    NSString *getURL = [NSString stringWithFormat:url_reviewsURLappend, (int)startPoint, (int)finishPoint, [QRWSettingsClient getSecurityKey]];

    return [self sendRequestWithURL:getURL
                            success:^(NSURLSessionDataTask *__unused task, id JSON) {
                                NSMutableArray *reviews = [NSMutableArray new];
                                NSArray *reviewsArray = (NSArray *) JSON;
                                DLog(@"Json is: %@", JSON);
                                for (NSDictionary *data in reviewsArray) {
                                    QRWReview *review = [QRWReview new];
                                    [review buildDataByJson:data];
                                    [reviews addObject:review];
                                }
                                if (block) {
                                    block(reviews, nil);
                                }
                            }
                            failure:^(NSURLSessionDataTask *__unused task, NSError *error) {
                                DLog(@"Error: %@", error);
                                if (block) {
                                    block([NSArray array], error);
                                }
                            }];
}

+ (NSURLSessionDataTask *)sendDeleteReviewRequestWithID:(NSInteger)reviewID
                                                  block:(void (^)(BOOL isSuccess, NSError *))block
{
    NSString *getURL = [NSString stringWithFormat:url_deleteReviewURLappend, (int)reviewID, [QRWSettingsClient getSecurityKey]];
    
    return [self sendRequestWithURL:getURL
                            success:^(NSURLSessionDataTask *__unused task, id JSON) {
                                DLog(@"Json is: %@", JSON);
                                if (block) {
                                    block(YES, nil);
                                }
                            }
                            failure:^(NSURLSessionDataTask *__unused task, NSError *error) {
                                DLog(@"Error: %@", error);
                                if (block) {
                                    block(NO, error);
                                }
                            }];
}

#pragma mark - private methods


+ (NSURLSessionDataTask *)sendRequestWithURL:(NSString *)requestURL
                                     success:(void (^)(NSURLSessionDataTask *task, id responseObject))success
                                     failure:(void (^)(NSURLSessionDataTask *task, NSError *error))failure
{
    DLog(@"URL is: %@", requestURL);
    NSArray *words = [requestURL componentsSeparatedByCharactersInSet :[NSCharacterSet whitespaceCharacterSet]];
    NSString *nospacesRequestURL = [words componentsJoinedByString:@""];
    return [[QRWHTTPClient sharedClient] GET:nospacesRequestURL
                                  parameters:nil
                                     success:success
                                     failure:failure];
}



@end


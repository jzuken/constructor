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

@interface QRWDataManager ()


@end


@implementation QRWDataManager


+ (NSURLSessionDataTask *)sendAuthorizationRequestWithLogin:(NSString *)login
                                                andPassowrd:(NSString *)password
                                                      block:(void (^)(BOOL isAuth, NSString *desription, NSError *error))block;
{
    NSString *getShop = [NSString stringWithFormat:url_developmentGetShopURLappend, login, password];

    return [self sendRequestWithURL:getShop
                            success:^(NSURLSessionDataTask *__unused task, id JSON) {
                                NSError *localError = nil;
                                NSDictionary *parsedObject = [NSJSONSerialization JSONObjectWithData:JSON options:0 error:&localError];

                                if (block) {
                                    [@"wrongKey" isEqual:[parsedObject objectForKey:@"api"]] ? block(NO, @"", nil) : block(YES, @"", nil);
                                }
                            }
                            failure:^(NSURLSessionDataTask *__unused task, NSError *error) {
                                DLog(@"Error: %@", error);
                                if (block) {
                                    block(NO, @"", error);
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
                                                         block: (void (^)(NSArray *products, NSError *error))block
{
    NSString *getURL = [NSString stringWithFormat:url_lastOrdersURLappend, startPoint, finishPoint, status, date, [QRWSettingsClient getSecurityKey], searchString];
    
    return [self sendRequestWithURL:getURL
                            success:^(NSURLSessionDataTask *__unused task, id JSON) {
                                NSMutableArray *orders = [NSMutableArray new];
                                NSArray *ordersArray = (NSArray *) JSON;
                                for (NSDictionary *data in ordersArray) {
                                    DLog(@"Json is: %@", data);
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
                                              block:(void (^)(NSArray *reviews, NSError *error))block
{
    NSString *getURL = [NSString stringWithFormat:url_orderInfoURLappend, orderID, [QRWSettingsClient getSecurityKey]];
    
    return [self sendRequestWithURL:getURL
                            success:^(NSURLSessionDataTask *__unused task, id JSON) {
                                NSMutableArray *reviews = [NSMutableArray new];
                                NSArray *reviewsArray = (NSArray *) JSON;
                                for (NSDictionary *data in reviewsArray) {
                                    DLog(@"Json is: %@", data);
                                    //                                             QRWUser *user = [QRWUser new];
                                    //                                             [user buildDataByJson:data];
                                    //                                             [users addObject:user];
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


#pragma mark - Products

+ (NSURLSessionDataTask *)sendProductsRequestWithSearchString:(NSString *)searchString
                                                    fromPoint:(NSInteger)startPoint
                                                      toPoint:(NSInteger)finishPoint
                                                     lowStock:(BOOL)isLowStock
                                                        block:(void (^)(NSArray *products, NSError *error))block;
{
    NSString *getURL = [NSString stringWithFormat:(isLowStock ? url_productsLowStockURLappend : url_productsURLappend), startPoint, finishPoint, [QRWSettingsClient getSecurityKey], searchString];

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
    NSString *getURL = [NSString stringWithFormat:url_productInfoURLappend, productID, [QRWSettingsClient getSecurityKey]];
    
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
    NSString *getURL = [NSString stringWithFormat:url_usersURLappend, startPoint, finishPoint, [QRWSettingsClient getSecurityKey], searchString];

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
                                              block:(void (^)(NSArray *reviews, NSError *error))block
{
    NSString *getURL = [NSString stringWithFormat:url_userInfoURLappend, userID, [QRWSettingsClient getSecurityKey]];

    return [self sendRequestWithURL:getURL
                            success:^(NSURLSessionDataTask *__unused task, id JSON) {
                                NSMutableArray *reviews = [NSMutableArray new];
                                NSArray *reviewsArray = (NSArray *) JSON;
                                for (NSDictionary *data in reviewsArray) {
                                    DLog(@"Json is: %@", data);
                                    //                                             QRWUser *user = [QRWUser new];
                                    //                                             [user buildDataByJson:data];
                                    //                                             [users addObject:user];
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


#pragma mark - Reviews

+ (NSURLSessionDataTask *)sendReviewsRequestFromPoint:(NSInteger)startPoint
                                              toPoint:(NSInteger)finishPoint
                                                block:(void (^)(NSArray *reviews, NSError *error))block
{
    NSString *getURL = [NSString stringWithFormat:url_reviewsURLappend, startPoint, finishPoint, [QRWSettingsClient getSecurityKey]];

    return [self sendRequestWithURL:getURL
                            success:^(NSURLSessionDataTask *__unused task, id JSON) {
                                NSMutableArray *reviews = [NSMutableArray new];
                                NSArray *reviewsArray = (NSArray *) JSON;
                                for (NSDictionary *data in reviewsArray) {
                                    DLog(@"Json is: %@", data);
//                                             QRWUser *user = [QRWUser new];
//                                             [user buildDataByJson:data];
//                                             [users addObject:user];
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


+ (NSURLSessionDataTask *)sendRequestWithURL:(NSString *)requestURL
                                     success:(void (^)(NSURLSessionDataTask *task, id responseObject))success
                                     failure:(void (^)(NSURLSessionDataTask *task, NSError *error))failure
{
    return [[QRWHTTPClient sharedClient] GET:requestURL
                                  parameters:nil
                                     success:success
                                     failure:failure];
}



@end

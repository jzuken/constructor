//
//  FLSDataManager.h
//  FirstLineSoftwareTestProject
//
//  Created by Иван Афанасьев on 06.04.13.
//  Copyright (c) 2013 Иван Афанасьев. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "QRWDashboardEntety.h"
#import "QRWUser.h"
#import "QRWProduct.h"
#import "QRWOrder.h"
#import "QRWProductWithInfo.h"



@interface QRWDataManager : NSObject


+ (NSURLSessionDataTask *)sendAuthorizationRequestWithLogin: (NSString *)login
                                                andPassowrd: (NSString *)password
                                                      block: (void (^)(BOOL isAuth, NSString *description, NSError *error))block;


//  ----------------- DASHBOARD --------------------------

+ (NSURLSessionDataTask *)sendDashboardRequestWithBlock: (void (^)(QRWDashboardEntety *dashboardEntety, NSError *error))block;



//  ----------------- REVIEWS --------------------------

+ (NSURLSessionDataTask *)sendReviewsRequestFromPoint: (NSInteger)startPoint
                                              toPoint: (NSInteger)finishPoint
                                                block: (void (^)(NSArray *reviews, NSError *error))block;


//  ----------------- USERS --------------------------

+ (NSURLSessionDataTask *)sendUserRequestWithSearchString: (NSString *)searchString
                                                fromPoint: (NSInteger)startPoint
                                                  toPoint: (NSInteger)finishPoint
                                                    block: (void (^)(NSArray *users, NSError *error))block;


+ (NSURLSessionDataTask *)sendUserInfoRequestWithID: (NSInteger)userID
                                              block: (void (^)(NSArray *reviews, NSError *error))block;


//  ----------------- PRODUCTS --------------------------


+ (NSURLSessionDataTask *)sendProductChangePriceRequestWithID: (NSInteger)productID
                                                     newPrice: (CGFloat) newPrice
                                                        block: (void (^)(BOOL isSuccess, NSError *error))block;


+ (NSURLSessionDataTask *)sendProductChangeAvaliabilityRequestWithID: (NSInteger)productID
                                                               block: (void (^)(BOOL isSuccess, NSError *error))block;


+ (NSURLSessionDataTask *)sendProductInfoRequestWithID: (NSInteger)productID
                                              block: (void (^)(QRWProductWithInfo *product, NSError *error))block;


+ (NSURLSessionDataTask *)sendProductsRequestWithSearchString: (NSString *)searchString
                                                    fromPoint: (NSInteger)startPoint
                                                      toPoint: (NSInteger)finishPoint
                                                     lowStock: (BOOL) isLowStock
                                                        block: (void (^)(NSArray *products, NSError *error))block;



//  ----------------- ORDERS --------------------------

+ (NSURLSessionDataTask *)sendLastOrderRequestWithSearchString: (NSString *)searchString
                                                     fromPoint: (NSInteger)startPoint
                                                       toPoint: (NSInteger)finishPoint
                                                        status: (NSString *)status
                                                          date: (NSString *)date
                                                         block: (void (^)(NSArray *orders, NSError *error))block;






@end

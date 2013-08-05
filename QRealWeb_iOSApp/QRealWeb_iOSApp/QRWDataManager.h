//
//  FLSDataManager.h
//  FirstLineSoftwareTestProject
//
//  Created by Иван Афанасьев on 06.04.13.
//  Copyright (c) 2013 Иван Афанасьев. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "QRWDownloader.h"

#import "QRWLastOrder.h"
#import "QRWTopProducts.h"
#import "QRWTopCategories.h"

#import "QRWUsers.h"

#import "QRWDiscount.h"

#import "QRWReview.h"



@protocol QRWDataManagerDelegate <NSObject>

@optional

-(void) respondsForAuthRequest:(BOOL)isAccepted;

-(void) respondsForToolsRequest:(NSArray *)tools;

-(void) respondsForLastOrderRequest:(QRWLastOrder *)lastOrder;

-(void) respondsForTopProductsRequest:(QRWTopProducts *)topProducts;

-(void) respondsForTopCategoriesRequest:(QRWTopCategories *)topCategories;

-(void) respondsForOrdersStatisticRequest:(NSDictionary *)statistic withArratOfKeys: (NSArray *) keys;

-(void) respondsForUserRequest:(QRWUsers *)usersObject;

-(void) respondsForDiscountsRequest:(NSArray *)discounts;

-(void) respondsForReviewsRequest:(NSArray *)reviews;

@end



@interface QRWDataManager : NSObject<QRWDownloaderDelegate>

@property (nonatomic, strong) id<QRWDataManagerDelegate> delegate;

+ (QRWDataManager *)instance;

/*
 Auth
 */

- (void) sendAuthorizationRequestWithLogin:(NSString *)login andPassowrd:(NSString *)password;

/*
 Settings for admin
 */

- (void) sendToolsRequest;


/*
 Dashboard
 */

- (void) sendLastOrderRequest;

- (void) sendTopProductsRequest;

- (void) sendTopCategoriesRequest;

- (void) sendOrdersStatisticRequest;

/*
 Users
 */

- (void) sendUsersRequestWithSort: (NSString *) sort startPoint: (NSInteger) startPoint lenght: (NSInteger) lenght;


/*
 Discounts
 */
- (void) sendDiscountsRequest;




/*
 Discounts
 */
- (void) sendReviewsRequest;





@end

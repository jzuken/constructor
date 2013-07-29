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



@protocol QRWDataManagerDelegate <NSObject>

@optional

-(void) respondsForAuthRequest:(BOOL)isAccepted;

-(void) respondsForToolsRequest:(NSArray *)tools;

-(void) respondsForLastOrderRequest:(QRWLastOrder *)lastOrder;

-(void) respondsForTopProductsRequest:(QRWTopProducts *)topProducts;

-(void) respondsForTopCategoriesRequest:(QRWLastOrder *)lastOrder;

@end



@interface QRWDataManager : NSObject<QRWDownloaderDelegate>

@property (nonatomic, strong) id<QRWDataManagerDelegate> delegate;
@property (nonatomic, strong) NSDictionary *documentsDictionary;

+ (QRWDataManager *)instance;

- (void) sendAuthorizationRequestWithLogin:(NSString *)login andPassowrd:(NSString *)password;

- (void) sendToolsRequest;

- (void) sendLastOrderRequest;

- (void) sendTopProductsRequest;

- (void) sendTopCategoriesRequest;

@end

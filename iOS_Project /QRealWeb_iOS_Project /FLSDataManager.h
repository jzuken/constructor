//
//  FLSDataManager.h
//  FirstLineSoftwareTestProject
//
//  Created by Иван Афанасьев on 06.04.13.
//  Copyright (c) 2013 Иван Афанасьев. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "FLSDocument.h"



@protocol FLSDataManagerDelegate <NSObject>

@optional

-(void)respondsForAuthRequest:(BOOL)isAccepted;
-(void)respondsForToolsRequest:(NSArray *)tools;

@end



@interface FLSDataManager : NSObject

@property (nonatomic, strong) id<FLSDataManagerDelegate> delegate;
@property (nonatomic, strong) NSDictionary *documentsDictionary;

+(FLSDataManager *)instance;

-(void)sendAuthorizationRequestWithLogin:(NSString *)login andPassowrd:(NSString *)password;
-(void)sendToolsRequest;

@end

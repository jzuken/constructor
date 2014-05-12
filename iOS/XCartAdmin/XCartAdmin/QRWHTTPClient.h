//
//  PBHTTPClient
//
//  Created by Sergey Rakov on 8/6/13.
//


#import <Foundation/Foundation.h>
#import <AFNetworking/AFNetworking.h>

@interface QRWHTTPClient : AFHTTPSessionManager

+ (instancetype)sharedClient;

+ (instancetype)sharedDevelopmentClient;

@end

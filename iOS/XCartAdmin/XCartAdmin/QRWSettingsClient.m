//
//  QRWSettingsClient.m
//  XCartAdmin
//
//  Created by Ivan Afanasiev on 24/12/13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import "QRWSettingsClient.h"
#import "URLsList.h"


@implementation QRWSettingsClient


+ (void)saveSecurityKey: (NSString *)securityKey
{
    
}

+ (NSString *)getSecurityKey
{
    return @"FQMTED8L";
}

+ (void)saveBaseUrl: (NSURL *) baseUrl
{
    
}

+ (NSURL *)getBaseUrl
{
    return [NSURL URLWithString:@"http://mobileadmin.x-cart.com/"];
}


+ (NSURL *)getDevelopmentServerUrl
{
    return [NSURL URLWithString:url_developmentBaseURL];
}


@end

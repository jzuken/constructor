//
//  PBHTTPClient
//
//  Created by Sergey Rakov on 8/6/13.
//


#import "QRWHTTPClient.h"
#import "QRWSettingsClient.h"


@implementation QRWHTTPClient


+ (instancetype)sharedClient
{
    QRWHTTPClient *_sharedClient = [[QRWHTTPClient alloc] initWithBaseURL:[QRWSettingsClient getBaseUrl]];
    AFSecurityPolicy *securityPolice = [AFSecurityPolicy policyWithPinningMode:AFSSLPinningModeNone];
    securityPolice.allowInvalidCertificates = YES;
    [_sharedClient setSecurityPolicy:securityPolice];
    return _sharedClient;
}


+ (instancetype)sharedDevelopmentClient
{
    static QRWHTTPClient *_sharedClient = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        _sharedClient = [[QRWHTTPClient alloc] initWithBaseURL:[QRWSettingsClient getDevelopmentServerUrl]];
        _sharedClient.responseSerializer = [AFHTTPResponseSerializer serializer];
        AFSecurityPolicy *securityPolice = [AFSecurityPolicy policyWithPinningMode:AFSSLPinningModeNone];
        securityPolice.allowInvalidCertificates = YES;
        [_sharedClient setSecurityPolicy:securityPolice];
    });
    
    return _sharedClient;
}


@end

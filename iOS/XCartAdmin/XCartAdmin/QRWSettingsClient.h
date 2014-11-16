//
//  QRWSettingsClient.h
//  XCartAdmin
//
//  Created by Ivan Afanasiev on 24/12/13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import <Foundation/Foundation.h>

typedef enum QRWSubscriptionStatus : NSUInteger {
    QRWSubscriptionStatusSuccess = 1,
    QRWSubscriptionStatusExpired = 2,
    QRWSubscriptionStatusTrial = 3
} QRWSubscriptionStatus;


@interface QRWSettingsClient : NSObject

+ (NSArray *)paymentStatuses;
+ (NSArray *)shippingStatuses;

+ (void)setPaymentStatuses:(NSArray *)paymentStatuses;
+ (void)setShippingStatuses:(NSArray *)shippingStatuses;


+ (BOOL)checkSubscriptionStatusesWithSuccessBlock:(void(^)(void))successBlock;
+ (void)saveSubscriptionStatus:(QRWSubscriptionStatus)subscriptionStatus;
+ (QRWSubscriptionStatus)getSubscriptionStatus;


+ (void)saveSecurityKey:(NSString *)securityKey;
+ (NSString *)getSecurityKey;


+ (void)saveBaseUrl:(NSString *)baseUrl;
+ (NSURL *)getBaseUrl;
+ (NSString *)getURLLogin;


+ (NSURL *)getDevelopmentServerUrl;

+ (void)saveXCartVersion:(NSString *)xCartVersion;
+ (NSString *)getXCartVersion;

+ (void)saveUnlockKey:(NSString *)unlockKey;
+ (NSString *)getUnlockKey;

+ (void)showConnectionErrorAlert;
+ (void)showAuthErrorAlert;

+ (NSString *)getUserTypeByKey:(NSString *)key;


@end

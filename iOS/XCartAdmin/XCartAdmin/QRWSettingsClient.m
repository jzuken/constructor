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

#pragma mark - order statuses

+ (NSArray *)paymentStatuses
{
    return [[NSUserDefaults standardUserDefaults] arrayForKey:@"QRW_paymentStatuses"];
}

+ (NSArray *)shippingStatuses
{
    return [[NSUserDefaults standardUserDefaults] arrayForKey:@"QRW_shippingStatuses"];
}

+ (void)setPaymentStatuses:(NSArray *)paymentStatuses
{
    [[NSUserDefaults standardUserDefaults] setObject:paymentStatuses forKey:@"QRW_paymentStatuses"];
}

+ (void)setShippingStatuses:(NSArray *)shippingStatuses
{
    [[NSUserDefaults standardUserDefaults] setObject:shippingStatuses forKey:@"QRW_shippingStatuses"];
}

#pragma mark - 

+ (BOOL)checkSubscriptionStatusesWithSuccessBlock:(void(^)(void))successBlock
{
    if ([QRWSettingsClient getSubscriptionStatus] == QRWSubscriptionStatusExpired) {
        [[[UIAlertView alloc] initWithTitle:NSLocalizedString(@"SUBSCRIPTION_EXPIRED", nil)
                                    message:NSLocalizedString(@"SUBSCRIPTION_EXPIRED_MESSAGE", nil)
                                   delegate:nil cancelButtonTitle:NSLocalizedString(@"OK", nil)
                          otherButtonTitles:nil, nil] show];
    } else if ([QRWSettingsClient getSubscriptionStatus] == QRWSubscriptionStatusTrial){
        [[[UIAlertView alloc] initWithTitle:NSLocalizedString(@"TRIAL_VERSION", nil)
                                    message:NSLocalizedString(@"TRIAL_VERSION_MESSAGE", nil)
                                   delegate:nil cancelButtonTitle:NSLocalizedString(@"OK", nil)
                          otherButtonTitles:nil, nil] show];
    } else {
        if (successBlock) {
            successBlock();
            return YES;
        }
    }
    return NO;
}


+ (void)saveSubscriptionStatus:(QRWSubscriptionStatus)subscriptionStatus
{
    [[NSUserDefaults standardUserDefaults] setObject:[NSNumber numberWithInteger:subscriptionStatus]
                                              forKey:@"QRW_subscriptionStatus"];
    
}

+ (QRWSubscriptionStatus)getSubscriptionStatus
{
    return [(NSNumber *)[[NSUserDefaults standardUserDefaults] objectForKey:@"QRW_subscriptionStatus"] integerValue];
}


+ (void)saveUnlockKey:(NSString *)unlockKey
{
    [[NSUserDefaults standardUserDefaults] setObject:unlockKey
                                              forKey:@"QRW_unlockKey"];
}

+ (NSString *)getUnlockKey
{
    return [[NSUserDefaults standardUserDefaults] objectForKey:@"QRW_unlockKey"];
}

+ (void)saveSecurityKey:(NSString *)securityKey
{
    [[NSUserDefaults standardUserDefaults] setObject:securityKey
                                              forKey:@"QRW_securityKey"];
}

+ (NSString *)getSecurityKey
{
//    return @"8WXE1NGH";//@"D7PMJ9SY";//@"FQMTED8L";
    return [[NSUserDefaults standardUserDefaults] objectForKey:@"QRW_securityKey"];
}

+ (void)saveBaseUrl:(NSString *)baseUrl
{
    [[NSUserDefaults standardUserDefaults] setObject:baseUrl
                                              forKey:@"QRW_baseUrl"];
}

+ (NSURL *)getBaseUrl
{
    return [NSURL URLWithString:[[NSUserDefaults standardUserDefaults] objectForKey:@"QRW_baseUrl"]];
}

+ (NSString *)getURLLogin
{
    return [[NSUserDefaults standardUserDefaults] objectForKey:@"QRW_baseUrl"];
}


+ (NSURL *)getDevelopmentServerUrl
{
    return [NSURL URLWithString:url_developmentBaseURL];
}


+ (void)saveXCartVersion:(NSString *)xCartVersion
{
    [[NSUserDefaults standardUserDefaults] setObject:xCartVersion
                                              forKey:@"QRW_xCartVersion"];
}

+ (NSString *)getXCartVersion
{
    return [[NSUserDefaults standardUserDefaults] objectForKey:@"QRW_xCartVersion"];
}

+ (NSString *)getUserTypeByKey:(NSString *)key
{
    NSDictionary *types = @{@"C": @"Customer", @"P": @"Administrator"};
    return types[key];
}

#pragma mark - alerts

+ (void)showConnectionErrorAlert
{
    [[[UIAlertView alloc] initWithTitle:NSLocalizedString(@"RELOGIN_ALERT_TITLE", nil)
                                message:NSLocalizedString(@"RELOGIN_ALERT_MESSAGE", nil)
                               delegate:nil cancelButtonTitle:NSLocalizedString(@"OK", nil)
                      otherButtonTitles:nil, nil] show];
}


+ (void)showAuthErrorAlert
{
    [[[UIAlertView alloc] initWithTitle:NSLocalizedString(@"AUTH_ERROR_ALERT_TITLE", nil)
                                message:NSLocalizedString(@"AUTH_ERROR_ALERT_MESSAGE", nil)
                               delegate:nil cancelButtonTitle:NSLocalizedString(@"OK", nil)
                      otherButtonTitles:nil, nil] show];
}
@end

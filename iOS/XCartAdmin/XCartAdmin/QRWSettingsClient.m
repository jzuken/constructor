//
//  QRWSettingsClient.m
//  XCartAdmin
//
//  Created by Ivan Afanasiev on 24/12/13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import "QRWSettingsClient.h"
#import "URLsList.h"
#import "constants.h"


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

+ (NSDictionary *)paymentStatusesCodeDictionary
{
    return [[NSUserDefaults standardUserDefaults] dictionaryForKey:@"QRW_paymentStatusesCode"];
}

+ (NSDictionary *)shippingStatusesCodeDictionary
{
    return [[NSUserDefaults standardUserDefaults] dictionaryForKey:@"QRW_shippingStatusesCode"];
}

+ (void)setPaymentStatuses:(NSArray *)paymentStatuses codes:(NSArray *)codes
{
    [[NSUserDefaults standardUserDefaults] setObject:paymentStatuses forKey:@"QRW_paymentStatuses"];
    NSDictionary *statusesDictionary = [NSDictionary dictionaryWithObjects:codes forKeys:paymentStatuses];
    [[NSUserDefaults standardUserDefaults] setObject:statusesDictionary forKey:@"QRW_paymentStatusesCode"];
}

+ (void)setShippingStatuses:(NSArray *)shippingStatuses codes:(NSArray *)codes
{
    [[NSUserDefaults standardUserDefaults] setObject:shippingStatuses forKey:@"QRW_shippingStatuses"];
    NSDictionary *shippingStatusesDictionary = [NSDictionary dictionaryWithObjects:codes forKeys:shippingStatuses];
    [[NSUserDefaults standardUserDefaults] setObject:shippingStatusesDictionary forKey:@"QRW_shippingStatusesCode"];
}

+ (NSDictionary *)statusesColors
{
    return @{@"I": [UIColor redColor],
             @"D": [UIColor redColor],
             @"F": [UIColor redColor],
             @"Q": [UIColor blueColor],
             @"B": [UIColor blueColor],
             @"P": kTextBlueColor,
             @"C": [UIColor greenColor],
             @"A": [UIColor blueColor],
             @"PP": kTextBlueColor,
             @"R": kTextBlueColor,
             };
}

#pragma mark - Subscription

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

#pragma mark - login and connection key

+ (void)saveCurrency:(NSString *)currency
{
    [[NSUserDefaults standardUserDefaults] setObject:currency forKey:@"QRW_currentCurrency"];
}

+ (NSString *)getCurrency
{
    return [[NSUserDefaults standardUserDefaults] stringForKey:@"QRW_currentCurrency"] ? : @"";
}

#pragma mark - login and connection key

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

+ (void)saveLogin:(NSString *)login
{
    [[NSUserDefaults standardUserDefaults] setObject:login
                                              forKey:@"QRW_login"];
}

+ (NSURL *)getLogin
{
    return [NSURL URLWithString:[[NSUserDefaults standardUserDefaults] objectForKey:@"QRW_login"]];
}

+ (NSString *)getURLLogin
{
    return [[NSUserDefaults standardUserDefaults] objectForKey:@"QRW_baseUrl"];
}


+ (NSURL *)getDevelopmentServerUrl
{
    return [NSURL URLWithString:url_developmentBaseURL];
}

#pragma mark - XCart version

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

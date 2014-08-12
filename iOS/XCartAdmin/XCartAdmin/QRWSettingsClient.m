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
    [[NSUserDefaults standardUserDefaults] setObject:[NSString stringWithFormat:@"https://%@/", baseUrl]
                                              forKey:@"QRW_baseUrl"];
}

+ (NSURL *)getBaseUrl
{
    return [NSURL URLWithString:[[NSUserDefaults standardUserDefaults] objectForKey:@"QRW_baseUrl"]];
//    return [NSURL URLWithString:@"https://mobileadmin.x-cart.com/"];
}


+ (NSURL *)getDevelopmentServerUrl
{
    return [NSURL URLWithString:url_developmentBaseURL];
}



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

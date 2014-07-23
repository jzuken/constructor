//
//  QRWSettingsClient.h
//  XCartAdmin
//
//  Created by Ivan Afanasiev on 24/12/13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface QRWSettingsClient : NSObject


+ (void)saveSecurityKey: (NSString *)securityKey;
+ (NSString *)getSecurityKey;


+ (void)saveBaseUrl: (NSURL *) baseUrl;
+ (NSURL *)getBaseUrl;


+ (NSURL *)getDevelopmentServerUrl;


+ (void)saveUnlockKey:(NSString *)unlockKey;
+ (NSString *)getUnlockKey;

+ (void)showConnectionErrorAlert;
+ (void)showAuthErrorAlert;


@end

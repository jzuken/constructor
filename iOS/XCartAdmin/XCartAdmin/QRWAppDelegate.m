//
//  QRWAppDelegate.m
//  XCartAdmin
//
//  Created by Ivan Afanasiev on 10/21/13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import "QRWAppDelegate.h"
#import "QRWDashboardViewController.h"
#import "QRWUnlockViewController.h"
#import "QRWDataManager.h"
#import "QRWOrderInfoViewController.h"
#import "QRWSettingsClient.h"
#import <SAMCategories/NSURL+SAMAdditions.h>
#import <SAMCategories/NSDictionary+SAMAdditions.h>

@implementation QRWAppDelegate


- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
    self.window = [[UIWindow alloc] initWithFrame:[[UIScreen mainScreen] bounds]];
    // Override point for customization after application launch.
    self.window.backgroundColor = [UIColor whiteColor];
    
    _mainViewController = [[QRWDashboardViewController alloc] init];
    _appNavigationController = [[UINavigationController alloc] initWithRootViewController:_mainViewController];
    
    [self.window setRootViewController:_appNavigationController];
    _appNavigationController.navigationBarHidden = YES;
    
    [_window makeKeyAndVisible];
    
    return YES;
}


- (void)applicationWillEnterForeground:(UIApplication *)application
{
    [[QRWUnlockViewController sharedInstance] showUnlockViewOnViewController:[QRWAppDelegate topMostController]];
}


+ (UIViewController*)topMostController
{
    UIViewController *topController = [UIApplication sharedApplication].keyWindow.rootViewController;
    while (topController.presentedViewController) {
        topController = topController.presentedViewController;
    }
    return topController;
}

- (BOOL)application:(UIApplication *)application
            openURL:(NSURL *)url
  sourceApplication:(NSString *)sourceApplication
         annotation:(id)annotation
{
    DLog(@"URL:%@", url);
    
    [QRWDataManager sendOrderChangeStatusRequestWithID:[[[url sam_queryDictionary] objectForKey:@"oid"] integerValue]
                                            pphDetails:[[url sam_queryDictionary] sam_stringWithFormEncodedComponents]
                                                status:[[QRWSettingsClient getXCartVersion] isEqual:@"XCart4"] ? [[url sam_queryDictionary] objectForKey:@"Type"] : @""
                                         paymentStatus:![[QRWSettingsClient getXCartVersion] isEqual:@"XCart4"] ? [[url sam_queryDictionary] objectForKey:@"Type"] : @""
                                        shippingStatus:@""
                                                 block:nil];
    
    return YES;
}

#pragma mark - Push Notifications

+ (void)registerOnPushNotifications
{
#if __IPHONE_OS_VERSION_MAX_ALLOWED >= __IPHONE_8_0
    if ([[UIApplication sharedApplication] respondsToSelector:@selector(registerUserNotificationSettings:)]) {
        UIUserNotificationSettings* notificationSettings = [UIUserNotificationSettings settingsForTypes:UIUserNotificationTypeAlert | UIUserNotificationTypeBadge | UIUserNotificationTypeSound categories:nil];
        [[UIApplication sharedApplication] registerUserNotificationSettings:notificationSettings];
        [[UIApplication sharedApplication] registerForRemoteNotifications];
    } else {
        [[UIApplication sharedApplication] registerForRemoteNotificationTypes: (UIRemoteNotificationTypeBadge | UIRemoteNotificationTypeSound | UIRemoteNotificationTypeAlert)];
    }
#else
    [[UIApplication sharedApplication] registerForRemoteNotificationTypes: (UIRemoteNotificationTypeBadge | UIRemoteNotificationTypeSound | UIRemoteNotificationTypeAlert)];
#endif
}


+ (void)unregisterForPushNotifications
{
    [[UIApplication sharedApplication] unregisterForRemoteNotifications];
#ifdef __IPHONE_8_0
    UIUserNotificationSettings *settings = [UIUserNotificationSettings settingsForTypes:UIUserNotificationTypeNone categories:nil];
    [[UIApplication sharedApplication] registerUserNotificationSettings:settings];
#endif
}

- (void)application:(UIApplication*)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData*)deviceToken
{
    DLog(@"My token is: %@", deviceToken);
    
    if (![[NSUserDefaults standardUserDefaults] objectForKey:@"isTokenWasSent"]) {
        NSString *stringDeviceToken = [[[[deviceToken description]
                                         stringByReplacingOccurrencesOfString: @"<" withString: @""]
                                        stringByReplacingOccurrencesOfString: @">" withString: @""]
                                       stringByReplacingOccurrencesOfString: @" " withString: @""];
        
        [QRWDataManager sendPushTokenAuthorization:stringDeviceToken
                                             block:^(BOOL isAuth, NSError *error) {
                                                 if (isAuth) {
                                                     [[NSUserDefaults standardUserDefaults] setObject:[NSNumber numberWithBool:YES] forKey:@"isTokenWasSent"];
                                                 }
                                             }];
    }
}


- (void)application:(UIApplication*)application didFailToRegisterForRemoteNotificationsWithError:(NSError*)error
{
    DLog(@"Failed to get token, error: %@", error);
}


- (void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo
{
    DLog(@"%@", userInfo);
    NSDictionary *pushInfo = (NSDictionary *)[userInfo objectForKey:@"data"];
    
    NSArray *viewControllers = [(UINavigationController *)[self.window rootViewController] viewControllers];
    
    UIViewController *topViewController = [viewControllers lastObject];
    
    if ([@"New order received" isEqual:[pushInfo objectForKey:@"message"]]) {
        
    }
    [QRWDataManager sendOrderInfoRequestWithID:(int)[pushInfo objectForKey:@"data"] block:^(QRWOrderInfo *order, NSError *error) {
        QRWOrderInfoViewController *orderInfoViewController = [[UIStoryboard storyboardWithName:@"MainStoryboard" bundle:nil] instantiateViewControllerWithIdentifier:@"QRWOrderInfoViewController"];
        [topViewController.navigationController pushViewController:orderInfoViewController animated:YES];
        [orderInfoViewController setOrderInfo:order];
    }];
}


@end

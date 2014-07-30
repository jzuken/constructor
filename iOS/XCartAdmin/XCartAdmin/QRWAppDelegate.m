//
//  QRWAppDelegate.m
//  XCartAdmin
//
//  Created by Ivan Afanasiev on 10/21/13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import "QRWAppDelegate.h"
#import "QRWLoginScrinViewController.h"
#import "QRWUnlockViewController.h"
#import "QRWDataManager.h"
#import "QRWOrderInfoViewController.h"
#import "QRWSettingsClient.h"

@implementation QRWAppDelegate




- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
    self.window = [[UIWindow alloc] initWithFrame:[[UIScreen mainScreen] bounds]];
    // Override point for customization after application launch.
    self.window.backgroundColor = [UIColor whiteColor];
    
    [QRWSettingsClient saveUnlockKey:@"0000"];
    
//    [[UIApplication sharedApplication] registerForRemoteNotificationTypes:
//     (UIRemoteNotificationTypeBadge | UIRemoteNotificationTypeSound | UIRemoteNotificationTypeAlert)];

    
    _firstEnterViewController = [[QRWLoginScrinViewController alloc] init];
    _appNavigationController = [[UINavigationController alloc] initWithRootViewController:_firstEnterViewController];
    
    [self.window setRootViewController:_appNavigationController];
    _appNavigationController.navigationBarHidden = YES;
    
    [_window makeKeyAndVisible];
    
    return YES;
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

- (void)applicationWillEnterForeground:(UIApplication *)application
{
    [QRWUnlockViewController showUnlockViewOnViewController:[QRWAppDelegate topMostController]];
}


+ (UIViewController*)topMostController
{
    UIViewController *topController = [UIApplication sharedApplication].keyWindow.rootViewController;
    while (topController.presentedViewController) {
        topController = topController.presentedViewController;
    }
    return topController;
}


@end

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

+ (void)registerOnPushNotifications
{
#ifdef __IPHONE_8_0
    UIUserNotificationSettings *settings = [UIUserNotificationSettings settingsForTypes:(UIRemoteNotificationTypeBadge
                                                                                         |UIRemoteNotificationTypeSound
                                                                                         |UIRemoteNotificationTypeAlert) categories:nil];
    [[UIApplication sharedApplication] registerUserNotificationSettings:settings];
#else
    UIRemoteNotificationType myTypes = UIRemoteNotificationTypeBadge | UIRemoteNotificationTypeAlert | UIRemoteNotificationTypeSound;
    [[UIApplication sharedApplication] registerForRemoteNotificationTypes:myTypes];
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



#ifdef __IPHONE_8_0
- (void)application:(UIApplication *)application didRegisterUserNotificationSettings:(UIUserNotificationSettings *)notificationSettings
{
    //register to receive notifications
    [application registerForRemoteNotifications];
}

- (void)application:(UIApplication *)application
handleActionWithIdentifier:(NSString *)identifier
forRemoteNotification:(NSDictionary *)userInfo
  completionHandler:(void(^)())completionHandler
{
    //handle the actions
    if ([identifier isEqualToString:@"declineAction"]){
    }
    else if ([identifier isEqualToString:@"answerAction"]){
    }
}
#endif

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
    NSString *orderID = [[NSUserDefaults standardUserDefaults] objectForKey:@"ChangePPHStatusID"];
    
    [QRWDataManager sendOrderChangeStatusRequestWithID:[orderID intValue] status:@"C" block:nil];
    
    return YES;
}


@end

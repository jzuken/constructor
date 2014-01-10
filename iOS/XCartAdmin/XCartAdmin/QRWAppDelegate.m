//
//  QRWAppDelegate.m
//  XCartAdmin
//
//  Created by Ivan Afanasiev on 10/21/13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import "QRWAppDelegate.h"
#import "QRWLoginScrinViewController.h"

@implementation QRWAppDelegate


-(void)setCustomAppearance
{
    UINavigationBar *navbarAppearance = [UINavigationBar appearance];
    UIImage *navbarBackground = [[UIImage imageNamed:@"topBarBackground.png"] resizableImageWithCapInsets:UIEdgeInsetsZero];
    UIColor *tintColor = [UIColor colorWithRed:63.0/255.0 green:63.0/255.0 blue:63.0/255.0 alpha:1.0];
    //    [navbarAppearance setTintColor:tintColor];
    [navbarAppearance setBackgroundImage:navbarBackground forBarMetrics:UIBarMetricsDefault];
    
    
    UIBarButtonItem *barButtonItemAppearance = [UIBarButtonItem appearance];
    [barButtonItemAppearance setTintColor:tintColor];
    UISearchBar *searchBarAppearance = [UISearchBar appearance];
    [searchBarAppearance setTintColor:tintColor];
    
    //    UITableView *tableViewAppereance = [UITableView appearance];
    //    [tableViewAppereance setBackgroundColor:tintColor];
}


- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
    self.window = [[UIWindow alloc] initWithFrame:[[UIScreen mainScreen] bounds]];
    // Override point for customization after application launch.
    self.window.backgroundColor = [UIColor whiteColor];
    
    _firstEnterViewController = [[QRWLoginScrinViewController alloc] init];
    _appNavigationController = [[UINavigationController alloc] initWithRootViewController:_firstEnterViewController];
    
    
    [self setCustomAppearance];
    
    [self.window setRootViewController:_appNavigationController];
    _appNavigationController.navigationBarHidden = YES;
    
    [_window makeKeyAndVisible];
    
    return YES;
}

- (void)applicationWillResignActive:(UIApplication *)application
{
    // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
    // Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
}

- (void)applicationDidEnterBackground:(UIApplication *)application
{
    // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later. 
    // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
}

- (void)applicationWillEnterForeground:(UIApplication *)application
{
    // Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
}

- (void)applicationDidBecomeActive:(UIApplication *)application
{
    // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
}

- (void)applicationWillTerminate:(UIApplication *)application
{
    // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
}

@end
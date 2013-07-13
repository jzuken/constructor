//
//  AppDelegate.m
//  FirstLineSoftwareTestProject
//
//  Created by Иван Афанасьев on 05.04.13.
//  Copyright (c) 2013 Иван Афанасьев. All rights reserved.
//

#import "AppDelegate.h"
#import "UIDevice+Resolutions.h"
#import "QWDLoginScrinViewController.h"
#import "QWDFastAuthViewController.h"

@implementation AppDelegate

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
    _window = [[UIWindow alloc] initWithFrame:[[UIScreen mainScreen] bounds]];
    
    if ([[UIDevice currentDevice] resolution] == UIDeviceResolution_iPhoneRetina5) {
        _firstEnterViewController = [[QWDLoginScrinViewController alloc] initWithNibName:@"QWDLoginScrinViewController4" bundle:nil];
    } else {
        _firstEnterViewController = [[QWDLoginScrinViewController alloc] initWithNibName:@"QWDLoginScrinViewController3.5" bundle:nil];
    }
    
    _fastAuthViewController = [[QWDFastAuthViewController alloc] init];
    
    _appNavigationController = [[UINavigationController alloc] initWithRootViewController:_firstEnterViewController];
    [self.window setRootViewController:_appNavigationController];
    _appNavigationController.navigationBarHidden = YES;
    
    [_window makeKeyAndVisible];
    
//    [[UIApplication sharedApplication] setStatusBarHidden:YES];
    
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
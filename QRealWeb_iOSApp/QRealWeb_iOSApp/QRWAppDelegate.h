//
//  QRWAppDelegate.h
//  QRealWeb_iOSApp
//
//  Created by Ivan Afanasiev on 7/16/13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import <UIKit/UIKit.h>



@class QRWLoginScrinViewController;
@class QRWDeployScrinViewController;
@class QRWMainScrinViewController;



@interface QRWAppDelegate : UIResponder <UIApplicationDelegate>

@property (strong, nonatomic) UIWindow *window;

@property (strong, nonatomic) QRWLoginScrinViewController *firstEnterViewController;
@property (strong, nonatomic) QRWDeployScrinViewController *fastAuthViewController;
@property (strong, nonatomic) QRWMainScrinViewController *mainScrinViewController;

@property (strong, nonatomic) UINavigationController *appNavigationController;

@property (readonly, strong, nonatomic) NSManagedObjectContext *managedObjectContext;
@property (readonly, strong, nonatomic) NSManagedObjectModel *managedObjectModel;
@property (readonly, strong, nonatomic) NSPersistentStoreCoordinator *persistentStoreCoordinator;

- (void)saveContext;
- (NSURL *)applicationDocumentsDirectory;

@end




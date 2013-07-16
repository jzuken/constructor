//
//  QRWAppDelegate.h
//  QRealWeb_iOSApp
//
//  Created by Ivan Afanasiev on 7/16/13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import <UIKit/UIKit.h>



@class QWDLoginScrinViewController;
@class QWDFastAuthViewController;



@interface QRWAppDelegate : UIResponder <UIApplicationDelegate>

@property (strong, nonatomic) UIWindow *window;

@property (strong, nonatomic) QWDLoginScrinViewController *firstEnterViewController;
@property (strong, nonatomic) QWDFastAuthViewController *fastAuthViewController;

@property (strong, nonatomic) UINavigationController *appNavigationController;

@property (readonly, strong, nonatomic) NSManagedObjectContext *managedObjectContext;
@property (readonly, strong, nonatomic) NSManagedObjectModel *managedObjectModel;
@property (readonly, strong, nonatomic) NSPersistentStoreCoordinator *persistentStoreCoordinator;

- (void)saveContext;
- (NSURL *)applicationDocumentsDirectory;

@end




//
//  QWDFastAuthViewController.h
//  QRealWebDemoProject
//
//  Created by Ivan Afanasiev on 7/8/13.
//  Copyright (c) 2013 Иван Афанасьев. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "iCarousel.h"

@interface QRWDeployScrinViewController : UIViewController<iCarouselDataSource, iCarouselDelegate>

@property (strong, nonatomic) IBOutlet iCarousel *passcodeCarousel;
@property (strong, nonatomic) IBOutlet UILabel *firstLoginLabel;

@property BOOL isPresented;

- (IBAction)enterToShopAction:(id)sender;

- (void) showFirstRunMessage;

@end

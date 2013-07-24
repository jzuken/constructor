//
//  QWDFastAuthViewController.h
//  QRealWebDemoProject
//
//  Created by Ivan Afanasiev on 7/8/13.
//  Copyright (c) 2013 Иван Афанасьев. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "iCarousel.h"
#import "QRWBaseViewController.h"

@interface QRWDeployScrinViewController : QRWBaseViewController<iCarouselDataSource, iCarouselDelegate>

@property (strong, nonatomic) IBOutlet iCarousel *passcodeCarousel;
@property (strong, nonatomic) IBOutlet UILabel *firstLoginLabel;

- (IBAction)enterToShopAction:(id)sender;

- (void) showFirstRunMessage;

@end

//
//  FLSBaseViewController.h
//  FirstLineSoftwareTestProject
//
//  Created by Иван Афанасьев on 06.04.13.
//  Copyright (c) 2013 Иван Афанасьев. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <QuartzCore/QuartzCore.h>

#import "QRWDataManager.h"

#import "QRWBaseEntety.h"

#import "constants.h"
#import "UIDevice+Resolutions.h"




@interface QRWBaseViewController : UIViewController



- (void) startLoadingAnimation;
- (void) stopLoadingAnimation;
- (void) showSuccesView;
- (void) showErrorView;

- (id)initWithNibName:(NSString *)nibNameOrNil oldNibName:(NSString *)oldNibNameOrNil;

- (void) setNavigationBarColor:(UIColor *)color title: (NSString *)title;

- (void) changeTheTableViewHeight: (CGFloat) heightChange;

@end

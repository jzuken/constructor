//
//  FLSBaseViewController.h
//  FirstLineSoftwareTestProject
//
//  Created by Иван Афанасьев on 06.04.13.
//  Copyright (c) 2013 Иван Афанасьев. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "QRWDataManager.h"

#import "UIDevice+Resolutions.h"
#import "constants.h"
#import "UIDevice+Resolutions.h"

#import "SVPullToRefresh.h"

#import "TLAlertView.h"


@interface QRWBaseViewController : UIViewController<QRWDataManagerDelegate>
{
    BOOL isFirstDataLoading;
    QRWDataManager *dataManager;
}


@property (nonatomic, strong) UIActivityIndicatorView *loadingActivityIndicator;
@property (nonatomic, strong) UIView *backgroundLoadingView;

- (void) startLoadingAnimation;
- (void) stopLoadingAnimation;

@end

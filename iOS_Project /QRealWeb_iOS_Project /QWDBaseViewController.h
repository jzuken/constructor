//
//  FLSBaseViewController.h
//  FirstLineSoftwareTestProject
//
//  Created by Иван Афанасьев on 06.04.13.
//  Copyright (c) 2013 Иван Афанасьев. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "UIDevice+Resolutions.h"
#import "FLSDataManager.h"
#import "constants.h"
#import "UIDevice+Resolutions.h"



@interface QWDBaseViewController : UIViewController<FLSDataManagerDelegate>
{
    FLSDataManager *dataManager;
}

@end

//
//  FLSBaseViewController.h
//  FirstLineSoftwareTestProject
//
//  Created by Иван Афанасьев on 06.04.13.
//  Copyright (c) 2013 Иван Афанасьев. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "QRWDataManager.h"

#import "QRWBaseEntety.h"

#import "constants.h"
#import "SVPullToRefresh.h"
#import "UIDevice+Resolutions.h"



@protocol QRWBaseViewControllerProtocol

- (void) responseFromTheServer:(QRWBaseEntety *) entety;

@end


@interface QRWBaseViewController : UIViewController<QRWBaseViewControllerProtocol>


- (void) startLoadingAnimation;
- (void) stopLoadingAnimation;

//- (void) showAfterDeletedAlertWithSuccessStatus: (BOOL) status;
//- (void) showSureToDeleteItemAlertWithHandleCancel:(TLCompletionBlock)cancelBlock handleConfirm:(TLCompletionBlock)confirmBlock;


- (id)initWithNibName:(NSString *)nibNameOrNil oldNibName:(NSString *)oldNibNameOrNil;




- (void) QRWpushViewController: (QRWBaseViewController *) viewController;

@end

//
//  QRWUnlockViewController.h
//  XCartAdmin
//
//  Created by Ivan Afanasiev on 10/01/14.
//  Copyright (c) 2014 Ivan Afanasiev. All rights reserved.
//

#import "QRWBaseViewController.h"

@interface QRWUnlockViewController : QRWBaseViewController<UIPickerViewDataSource, UIPickerViewDelegate>


+ (void)showUnlockViewOnViewController:(UIViewController *)viewController;


@property (strong, nonatomic) IBOutlet UIPickerView *unlockPicker;

- (IBAction)enterButton:(id)sender;


@end

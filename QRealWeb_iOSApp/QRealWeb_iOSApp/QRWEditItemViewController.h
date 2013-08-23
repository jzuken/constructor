//
//  QRWEditItemViewController.h
//  QRealWeb_iOSApp
//
//  Created by Иван Афанасьев on 14.08.13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import "QRWBaseViewController.h"

@interface QRWEditItemViewController : QRWBaseViewController<UIGestureRecognizerDelegate, UITextFieldDelegate>


@property (nonatomic, strong) IBOutlet UIButton *exitButton;
@property (nonatomic, strong) IBOutlet UIButton *uploadButton;


- (IBAction)exitButtonClicked:(id)sender;
- (IBAction)uploadButtonClicked:(id)sender;

@end

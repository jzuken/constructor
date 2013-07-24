//
//  ViewController.h
//  FirstLineSoftwareTestProject
//
//  Created by Иван Афанасьев on 05.04.13.
//  Copyright (c) 2013 Иван Афанасьев. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "QRWBaseViewController.h"

@interface QRWLoginScrinViewController : QRWBaseViewController<UITextFieldDelegate, UIGestureRecognizerDelegate>

@property (strong, nonatomic) IBOutlet UITextField *loginTextField;
@property (strong, nonatomic) IBOutlet UITextField *passwordTextField;
@property (strong, nonatomic) IBOutlet UIButton *signInButton;

@property (strong, nonatomic) IBOutlet UIView *signInBoxView;

- (IBAction)signInClick:(id)sender;

@end

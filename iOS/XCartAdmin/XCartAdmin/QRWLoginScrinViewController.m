//
//  ViewController.m
//  FirstLineSoftwareTestProject
//
//  Created by Иван Афанасьев on 05.04.13.
//  Copyright (c) 2013 Иван Афанасьев. All rights reserved.
//

#import "QRWLoginScrinViewController.h"
#import "QRWDashboardViewController.h"

#import <QuartzCore/QuartzCore.h>

@interface QRWLoginScrinViewController ()

@end

@implementation QRWLoginScrinViewController


- (id)init
{
    return [self initWithNibName:@"QRWLoginScrinViewController" oldNibName:@"QRWLoginScrinViewControllerOld"];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    UITapGestureRecognizer *tapRecog = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(userTapOnScreen:)];
    [self.signInBoxView addGestureRecognizer:tapRecog];
    tapRecog.delegate = self;
    
    [_passwordTextField addTarget:self action:@selector(textDidChangeText:) forControlEvents:UIControlEventEditingChanged];
    [_loginTextField addTarget:self action:@selector(textDidChangeText:) forControlEvents:UIControlEventEditingChanged];
    
    [_loginTextField setText:kTestUsername];
    [_passwordTextField setText:kTestPassword];
    
    [self textDidChangeText:nil];
}


- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    self.navigationController.navigationBarHidden = YES;
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}

- (IBAction)signInClick:(id)sender
{
//    [self startLoadingAnimation];
//    [QRWDataManager sendAuthorizationRequestWithLogin:_loginTextField.text andPassowrd:_passwordTextField.text block:^(BOOL isAuth, NSString *description, NSError *error) {
//        [self respondsForAuthRequest:isAuth];
//    }];
    
    QRWDashboardViewController *dashboardViewController = [[QRWDashboardViewController alloc] init];
    [self.navigationController pushViewController:dashboardViewController animated:YES];
    
}

-(void)userTapOnScreen:(UIGestureRecognizer *)sender
{
    [_loginTextField resignFirstResponder];
    [_passwordTextField resignFirstResponder];
    
    [UIView animateWithDuration:0.5 animations:^{
        CGRect frame = self.view.frame;
        
        frame.origin.y = (([[UIDevice currentDevice] resolution] == UIDeviceResolution_iPhoneRetina5)) ? kBottomYCoordinateLoginBoxFor4: kBottomYCoordinateLoginBoxFor3_5;
        
        self.view.frame = frame;
    }];
}

#pragma mark - dataManager delegate

- (void)respondsForAuthRequest:(BOOL)isAccepted
{
    [self stopLoadingAnimation];
    if (isAccepted) {
        [_loginTextField resignFirstResponder];
        [_passwordTextField resignFirstResponder];
        
        [[NSUserDefaults standardUserDefaults] setObject:kUserDefaults_isLogInObject forKey:kUserDefaults_isLogInKey];
        
        QRWDashboardViewController *dashboardViewController = [[QRWDashboardViewController alloc] init];
        [self.navigationController pushViewController:dashboardViewController animated:YES];
    } else {
        [_passwordTextField setText:@""];
        [_loginTextField setText:@""];
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:NSLocalizedString(@"RELOGIN_ALERT_TITLE", nil)
                                                        message:NSLocalizedString(@"RELOGIN_ALERT_MESSAGE", nil)
                                                       delegate:nil cancelButtonTitle:NSLocalizedString(@"OK", nil)
                                              otherButtonTitles:nil, nil];
        [alert show];
    }

}

#pragma mark - Text Field

- (void)textFieldDidBeginEditing:(UITextField *)textField
{
    [UIView animateWithDuration:0.5 animations:^{
        CGRect frame = self.view.frame;
        
        frame.origin.y = (([[UIDevice currentDevice] resolution] == UIDeviceResolution_iPhoneRetina5)) ? kTopYCoordinateLoginBoxFor4: kTopYCoordinateLoginBoxFor3_5;
        
        self.view.frame = frame;
    }];
}

-(void)textDidChangeText:(id)sender
{
    if ([@"" isEqualToString: _loginTextField.text] || [@"" isEqualToString: _passwordTextField.text] ) {
        [_signInButton setEnabled:NO];
    } else {
        [_signInButton setEnabled:YES];
    }
}


-(BOOL)textFieldShouldReturn:(UITextField *)textField
{
    [UIView animateWithDuration:0.5 animations:^{
        CGRect frame = self.view.frame;

        frame.origin.y = (([[UIDevice currentDevice] resolution] == UIDeviceResolution_iPhoneRetina5)) ? kBottomYCoordinateLoginBoxFor4: kBottomYCoordinateLoginBoxFor3_5;
        
        self.view.frame = frame;
    }];
    [textField resignFirstResponder];
    return YES;
}

#pragma mark - GestureRecognizer delegate

-(BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer shouldReceiveTouch:(UITouch *)touch
{
    if (touch.view == _signInButton) {
        return NO;
    }
    return YES;
}

@end

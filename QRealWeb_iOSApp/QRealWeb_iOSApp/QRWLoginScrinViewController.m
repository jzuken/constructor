//
//  ViewController.m
//  FirstLineSoftwareTestProject
//
//  Created by Иван Афанасьев on 05.04.13.
//  Copyright (c) 2013 Иван Афанасьев. All rights reserved.
//

#import "QRWLoginScrinViewController.h"

#import "QRWMainScrinViewController.h"

#import <QuartzCore/QuartzCore.h>

@interface QRWLoginScrinViewController ()

@end

@implementation QRWLoginScrinViewController


- (id)init
{
    return [self initWithNibName:@"QRWLoginScrinViewController" bundle:nil];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    
    self.signInBoxView.layer.cornerRadius = 10;
    self.signInBoxView.backgroundColor = [UIColor orangeColor];
    self.signInBoxView.layer.shadowOffset = CGSizeMake(5, 5);
    self.signInBoxView.layer.shadowRadius = 5;
    self.signInBoxView.layer.shadowOpacity = 1;
    
    UITapGestureRecognizer *tapRecog = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(userTapOnScreen:)];
    [self.signInBoxView addGestureRecognizer:tapRecog];
    tapRecog.delegate = self;
    
    [_passwordTextField addTarget:self action:@selector(textDidChangeText:) forControlEvents:UIControlEventEditingChanged];
    [_loginTextField addTarget:self action:@selector(textDidChangeText:) forControlEvents:UIControlEventEditingChanged];
    
    [_signInButton setEnabled:NO];
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
    [dataManager sendAuthorizationRequestWithLogin:_loginTextField.text andPassowrd:_passwordTextField.text];
}

-(void)userTapOnScreen:(UIGestureRecognizer *)sender
{
    [_loginTextField resignFirstResponder];
    [_passwordTextField resignFirstResponder];
    
    [UIView animateWithDuration:0.5 animations:^{
        CGRect frame = self.signInBoxView.frame;
        
        frame.origin.y = (([[UIDevice currentDevice] resolution] == UIDeviceResolution_iPhoneRetina5)) ? kBottomYCoordinateLoginBoxFor4: kBottomYCoordinateLoginBoxFor3_5;
        
        self.signInBoxView.frame = frame;
    }];
}

#pragma mark - dataManager delegate

- (void)respondsForAuthRequest:(BOOL)isAccepted
{
    if (isAccepted) {
        [_loginTextField resignFirstResponder];
        [_passwordTextField resignFirstResponder];
        
        [[NSUserDefaults standardUserDefaults] setObject:kUserDefaults_isLogInObject forKey:kUserDefaults_isLogInKey];
        
        QRWMainScrinViewController *mainScrinViewController = [[QRWMainScrinViewController alloc] init];
        [self.navigationController pushViewController:mainScrinViewController animated:YES];
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
        CGRect frame = self.signInBoxView.frame;
        
        frame.origin.y = (([[UIDevice currentDevice] resolution] == UIDeviceResolution_iPhoneRetina5)) ? kTopYCoordinateLoginBoxFor4: kTopYCoordinateLoginBoxFor3_5;
        
        self.signInBoxView.frame = frame;
    }];
}

-(void)textDidChangeText:(id)sender
{
    if ([_passwordTextField.text isEqualToString:@""] || [_loginTextField.text isEqualToString:@""]) {
        [_signInButton setEnabled:NO];
    } else {
        [_signInButton setEnabled:YES];
    }
}


-(BOOL)textFieldShouldReturn:(UITextField *)textField
{
    [UIView animateWithDuration:0.5 animations:^{
        CGRect frame = self.signInBoxView.frame;

        frame.origin.y = (([[UIDevice currentDevice] resolution] == UIDeviceResolution_iPhoneRetina5)) ? kBottomYCoordinateLoginBoxFor4: kBottomYCoordinateLoginBoxFor3_5;
        
        self.signInBoxView.frame = frame;
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

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
{
    BOOL _isKeyboardBeHide;
}

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
    [self.view addGestureRecognizer:tapRecog];
    tapRecog.delegate = self;
    
    _isKeyboardBeHide = NO;
    
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
    [self startLoadingAnimation];
    [QRWDataManager sendAuthorizationRequestWithLogin:_loginTextField.text andPassowrd:_passwordTextField.text block:^(BOOL isAuth, NSString *description, NSError *error) {
        [self respondsForAuthRequest:isAuth];
    }];
}


- (IBAction)scanQRInClick:(id)sender
{
    DLog(@"Scanning..");
//    _loginTextField.text = @"Scanning..";
    
    ZBarReaderViewController *codeReader = [ZBarReaderViewController new];
    codeReader.readerDelegate=self;
    codeReader.supportedOrientationsMask = ZBarOrientationMaskAll;
    
    ZBarImageScanner *scanner = codeReader.scanner;
    [scanner setSymbology: ZBAR_I25 config: ZBAR_CFG_ENABLE to: 0];
    
    [self presentViewController:codeReader animated:YES completion:nil];
}

- (void) imagePickerController: (UIImagePickerController*) reader didFinishPickingMediaWithInfo: (NSDictionary*) info
{
    //  get the decode results
    id<NSFastEnumeration> results = [info objectForKey: ZBarReaderControllerResults];
    
    ZBarSymbol *symbol = nil;
    for(symbol in results)
        // just grab the first barcode
        break;
    
    // showing the result on textview
//    _loginTextField.text = symbol.data;
    
//    resultImageView.image = [info objectForKey: UIImagePickerControllerOriginalImage];
    
    // dismiss the controller
    [reader dismissViewControllerAnimated:YES completion:nil];
}


-(void)userTapOnScreen:(UIGestureRecognizer *)sender
{
    if (_isKeyboardBeHide) {
        [_loginTextField resignFirstResponder];
        [_passwordTextField resignFirstResponder];

        [UIView animateWithDuration:0.5 animations:^{
            CGRect frame = self.signInBoxView.frame;
            
            frame.origin.y += kLoginViewUpHeight;
            
            self.signInBoxView.frame = frame;
        }];
        
        _isKeyboardBeHide = NO;
    }
}

#pragma mark - dataManager delegate

- (void)respondsForAuthRequest:(BOOL)isAccepted
{
    [self stopLoadingAnimation];
//    if (isAccepted) {
//        [_loginTextField resignFirstResponder];
//        [_passwordTextField resignFirstResponder];
//        
//        QRWDashboardViewController *dashboardViewController = [[QRWDashboardViewController alloc] init];
//        [self.navigationController pushViewController:dashboardViewController animated:YES];
//    } else {
//        [_passwordTextField setText:@""];
//        [_loginTextField setText:@""];
//        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:NSLocalizedString(@"RELOGIN_ALERT_TITLE", nil)
//                                                        message:NSLocalizedString(@"RELOGIN_ALERT_MESSAGE", nil)
//                                                       delegate:nil cancelButtonTitle:NSLocalizedString(@"OK", nil)
//                                              otherButtonTitles:nil, nil];
//        [alert show];
//    }
    QRWDashboardViewController *dashboardViewController = [[QRWDashboardViewController alloc] init];
    [self.navigationController pushViewController:dashboardViewController animated:YES];
}

#pragma mark - Text Field

- (void)textFieldDidBeginEditing:(UITextField *)textField
{
    [UIView animateWithDuration:0.5 animations:^{
        CGRect frame = self.signInBoxView.frame;
        
        frame.origin.y -= kLoginViewUpHeight;
        
        self.signInBoxView.frame = frame;
    }];
    
    _isKeyboardBeHide = YES;
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
        CGRect frame = self.signInBoxView.frame;

        frame.origin.y += kLoginViewUpHeight;
        
        self.signInBoxView.frame = frame;
    }];
    [textField resignFirstResponder];
    _isKeyboardBeHide = NO;
    
    return YES;
}

#pragma mark - GestureRecognizer delegate

-(BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer shouldReceiveTouch:(UITouch *)touch
{
    if (touch.view == _loginTextField  || touch.view == _passwordTextField || touch.view == _signInButton) {
        return NO;
    }
    return YES;
}

@end

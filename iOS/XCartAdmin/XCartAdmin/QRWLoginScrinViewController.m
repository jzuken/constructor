//
//  ViewController.m
//  FirstLineSoftwareTestProject
//
//  Created by Иван Афанасьев on 05.04.13.
//  Copyright (c) 2013 Иван Афанасьев. All rights reserved.
//

#import "QRWLoginScrinViewController.h"
#import "QRWDashboardViewController.h"
#import "QRWSettingsClient.h"
#import "QRWAppDelegate.h"

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
    [QRWDataManager sendAuthorizationRequestWithLogin:_loginTextField.text
                                          andPassowrd:_passwordTextField.text
                                                block:^(BOOL isAuth, NSString *description, NSError *error) {
                                                    [self respondsForAuthRequest:isAuth shopURL:description error:error];
    }];
//    [self respondsForAuthRequest:YES error:nil];
}


- (IBAction)scanQRInClick:(id)sender
{
    DLog(@"Scanning..");
    
    if (_isKeyboardBeHide) {
        [self animateloginBoxUp:NO];
    }
    
    
    ZBarReaderViewController *codeReader = [ZBarReaderViewController new];
    codeReader.readerDelegate = self;
    codeReader.supportedOrientationsMask = ZBarOrientationMaskAll;
    
    ZBarImageScanner *scanner = codeReader.scanner;
    [scanner setSymbology: ZBAR_I25 config: ZBAR_CFG_ENABLE to: 0];
    
    [self presentViewController:codeReader animated:YES completion:nil];
}

- (void) imagePickerController: (UIImagePickerController*) reader didFinishPickingMediaWithInfo: (NSDictionary*) info
{
    id<NSFastEnumeration> results = [info objectForKey: ZBarReaderControllerResults];
    
    ZBarSymbol *symbol = nil;
    for(symbol in results)
        break;
    
    [reader dismissViewControllerAnimated:YES completion:nil];
    _passwordTextField.text = [symbol.data componentsSeparatedByString:@"?key="][1];
    [self respondsForAuthRequest:YES shopURL:[symbol.data componentsSeparatedByString:@"?key="][0] error:nil];
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

- (void)respondsForAuthRequest:(BOOL)isAccepted shopURL:(NSString *)url error:(NSError *)error
{
    [self stopLoadingAnimation];
    if (isAccepted) {
        [_loginTextField resignFirstResponder];
        [_passwordTextField resignFirstResponder];
        
        [QRWDataManager sendConfigRequestWithBlock:^(NSString *XCartVersion, NSError *error) {
            if (error) {
                [QRWSettingsClient saveXCartVersion:@"XCart4"];
            } else {
                [QRWSettingsClient saveXCartVersion:XCartVersion];
            }
            
            [QRWSettingsClient saveBaseUrl:url];
            [QRWSettingsClient saveSecurityKey:_passwordTextField.text];
            [QRWAppDelegate registerOnPushNotifications];
            
            [[NSUserDefaults standardUserDefaults] setObject:[NSNumber numberWithBool:YES] forKey:@"QRW_isLogIn"];
            [self dismissViewControllerAnimated:YES completion:nil];
        }];
        
    } else {
        [_passwordTextField setText:@""];
        [_loginTextField setText:@""];
        if (error) {
            [QRWSettingsClient showConnectionErrorAlert];
        } else {
            [QRWSettingsClient showAuthErrorAlert];
        }
    }
}

#pragma mark - Text Field

- (void)textFieldDidBeginEditing:(UITextField *)textField
{
    [self animateloginBoxUp:YES];
    
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
    [self animateloginBoxUp:NO];
    [textField resignFirstResponder];
    _isKeyboardBeHide = NO;
    
    return YES;
}

- (void)animateloginBoxUp:(BOOL)isUp
{
    if (isUp && _isKeyboardBeHide) {
        return;
    }
    [UIView animateWithDuration:0.5 animations:^{
        CGRect frame = self.signInBoxView.frame;
        
        frame.origin.y += isUp? -kLoginViewUpHeight:kLoginViewUpHeight;
        
        self.signInBoxView.frame = frame;
    }];
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

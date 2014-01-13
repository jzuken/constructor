//
//  FLSBaseViewController.m
//  FirstLineSoftwareTestProject
//
//  Created by Иван Афанасьев on 06.04.13.
//  Copyright (c) 2013 Иван Афанасьев. All rights reserved.
//

#import "QRWBaseViewController.h"
#import <ProgressHUD/ProgressHUD.h>


@interface QRWBaseViewController ()

@property (nonatomic, strong) UIActivityIndicatorView *loadingActivityIndicator;
@property (nonatomic, strong) UIView *backgroundLoadingView;

@end

@implementation QRWBaseViewController


- (id)initWithNibName:(NSString *)nibNameOrNil oldNibName:(NSString *)oldNibNameOrNil
{
    return [self initWithNibName:(([[UIDevice currentDevice] resolution] == UIDeviceResolution_iPhoneRetina5)) ? nibNameOrNil: oldNibNameOrNil bundle:nil];
}


- (id)init
{
    return [self initWithNibName:NSStringFromClass([self class]) oldNibName:[NSString stringWithFormat:@"%@Old", NSStringFromClass([self class])]];
}


- (void)viewDidLoad
{
    [super viewDidLoad];
    
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(keyboardDidAppear:)
                                                 name:UIKeyboardWillShowNotification
                                               object:self.view.window];
    
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(keyboardDidDisappear:)
                                                 name:UIKeyboardWillHideNotification
                                               object:self.view.window];
}



#pragma mark Loading view

- (void)startLoadingAnimation
{
    [ProgressHUD show:QRWLoc(@"LOADING")];
}

- (void)showSuccesView
{
    [ProgressHUD showSuccess:QRWLoc(@"SUCCESS")];
}

- (void)showErrorView
{
    [ProgressHUD showSuccess:QRWLoc(@"ERROR")];
}


- (void)stopLoadingAnimation
{
    [ProgressHUD dismiss];
}


- (void) setNavigationBarColor:(UIColor *)color title: (NSString *)title;
{
    NSArray *ver = [[UIDevice currentDevice].systemVersion componentsSeparatedByString:@"."];
    if ([[ver objectAtIndex:0] intValue] >= 7) {
        self.navigationController.navigationBar.barTintColor = color;
        self.navigationController.navigationBar.tintColor = [UIColor whiteColor];
        self.navigationController.navigationBar.translucent = NO;
        if ([kRedColor isEqual:color] || [kYellowColor isEqual:color]) {
            self.navigationController.navigationBar.tintColor = [UIColor blackColor];
            self.navigationController.navigationBar.titleTextAttributes = @{NSForegroundColorAttributeName : [UIColor blackColor]};
        } else {
            self.navigationController.navigationBar.tintColor = [UIColor whiteColor];
            self.navigationController.navigationBar.titleTextAttributes = @{NSForegroundColorAttributeName : [UIColor whiteColor]};
        }
    } else {
        self.navigationController.navigationBar.barTintColor = color;
    }
    
    self.navigationItem.title = title;
}
#pragma mark - Keyboard appears/disappear methods

- (void) keyboardDidDisappear:(NSNotification *)notification
{
    [self changeTheTableViewHeight:[[[notification userInfo] objectForKey:UIKeyboardFrameBeginUserInfoKey] CGRectValue].size.height];
}

- (void) keyboardDidAppear:(NSNotification *)notification
{
    [self changeTheTableViewHeight:-[[[notification userInfo] objectForKey:UIKeyboardFrameBeginUserInfoKey] CGRectValue].size.height];
}


- (void) changeTheTableViewHeight: (CGFloat) heightChange
{
    
}


@end

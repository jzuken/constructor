//
//  FLSBaseViewController.m
//  FirstLineSoftwareTestProject
//
//  Created by Иван Афанасьев on 06.04.13.
//  Copyright (c) 2013 Иван Афанасьев. All rights reserved.
//

#import "QRWBaseViewController.h"

#import "TLAlertView.h"


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
}



#pragma mark Loading view

- (void)startLoadingAnimation
{
    _backgroundLoadingView = [[UIView alloc] initWithFrame:self.view.frame];
    _backgroundLoadingView.backgroundColor = [UIColor blackColor];
    _backgroundLoadingView.alpha = 0;
    
    _loadingActivityIndicator = [[UIActivityIndicatorView alloc] initWithFrame:
                                                         CGRectMake((self.view.frame.size.width - 30)/2, (self.view.frame.size.height - 30)/2, 30, 30)];
    
    [_backgroundLoadingView addSubview:_loadingActivityIndicator];
    
    [self.view addSubview:_backgroundLoadingView];
 
    [UIView animateWithDuration:0.6 animations:^{
        _backgroundLoadingView.alpha = 0.5;
    } completion:^(BOOL finished) {
        [_loadingActivityIndicator startAnimating];
    }];
}


- (void)stopLoadingAnimation
{
    [UIView animateWithDuration:0.6 animations:^{
        _backgroundLoadingView.alpha = 0;
    } completion:^(BOOL finished) {
        [_loadingActivityIndicator stopAnimating];
        [_backgroundLoadingView removeFromSuperview];
    }];
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



@end

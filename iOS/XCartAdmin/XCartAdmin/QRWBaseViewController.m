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



//#pragma mark Alerts
//
//- (void) showSureToDeleteItemAlertWithHandleCancel:(TLCompletionBlock)cancelBlock handleConfirm:(TLCompletionBlock)confirmBlock
//{
//    TLAlertView *alert = [[TLAlertView alloc] initWithTitle:NSLocalizedString(@"ATTENTION", nil) message:NSLocalizedString(@"SURE_TO_DELETE", nil) inView:self.view cancelButtonTitle:NSLocalizedString(@"CANCEL", nil) confirmButton:NSLocalizedString(@"OK", nil)];
//    [alert handleCancel:cancelBlock handleConfirm:confirmBlock];
//    [alert show];
//}
//
//
//- (void) showAfterDeletedAlertWithSuccessStatus: (BOOL) status
//{
//    NSString *titleString;
//    NSString *messageString;
//    
//    if (status) {
//        titleString = NSLocalizedString(@"SUCCESS_TITLE", nil);
//        messageString = NSLocalizedString(@"SUCCESS_DELETE_MESSAGE", nil);
//        
//    } else {
//        titleString = NSLocalizedString(@"FAIL_TITLE", nil);
//        messageString = NSLocalizedString(@"FAIL_DELETE_MESSAGE", nil);
//    }
//    
//    TLAlertView *alert = [[TLAlertView alloc] initWithTitle:titleString message:messageString inView:self.view cancelButtonTitle:NSLocalizedString(@"CANCEL", nil) confirmButton:nil];
//    [alert show];
//    [self stopLoadingAnimation];
//}

@end

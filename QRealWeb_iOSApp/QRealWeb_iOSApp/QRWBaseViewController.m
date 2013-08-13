//
//  FLSBaseViewController.m
//  FirstLineSoftwareTestProject
//
//  Created by Иван Афанасьев on 06.04.13.
//  Copyright (c) 2013 Иван Афанасьев. All rights reserved.
//

#import "QRWBaseViewController.h"


@interface QRWBaseViewController ()

@end

@implementation QRWBaseViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        dataManager = [QRWDataManager instance];
    }
    return self;
}


- (void)viewDidLoad
{
    [super viewDidLoad];
    isFirstDataLoading = YES;
    [self regestraiteDataManagerDelegate];
}

-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [self regestraiteDataManagerDelegate];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}


- (void)regestraiteDataManagerDelegate
{
    dataManager.delegate = self;
}




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

@end

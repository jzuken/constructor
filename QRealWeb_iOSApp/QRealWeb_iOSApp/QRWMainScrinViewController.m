//
//  QRWMainScrinViewController.m
//  QRealWeb_iOSApp
//
//  Created by Ivan Afanasiev on 7/16/13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import "QRWMainScrinViewController.h"
#import "QRWMainStatsViewController.h"
#import "QRWToolsScrinViewController.h"

@interface QRWMainScrinViewController ()

@property (nonatomic, strong) QRWMainStatsViewController *mainStatsViewController;
@property (nonatomic, strong) QRWToolsScrinViewController *toolsViewController;

@end

@implementation QRWMainScrinViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (id)init
{
    return [self initWithNibName:@"QRWMainScrinViewController" bundle:nil];
}


- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    self.navigationController.navigationBarHidden = YES;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    _mainScrinPagesScrollView.pagingEnabled = YES;
    
    _mainScrinPagesScrollView.contentSize = CGSizeMake(_mainScrinPagesScrollView.frame.size.width * 2, _mainScrinPagesScrollView.frame.size.height);
    
    _mainScrinPagesScrollView.showsHorizontalScrollIndicator = NO;
    _mainScrinPagesScrollView.showsVerticalScrollIndicator = NO;
    _mainScrinPagesScrollView.scrollsToTop = NO;
    
    _mainStatsViewController = [[QRWMainStatsViewController alloc] init];
    _toolsViewController = [[QRWToolsScrinViewController alloc] init];
    
    
    CGRect frame = _mainStatsViewController.view.frame;
    frame.origin.x = frame.size.width;
    _mainStatsViewController.view.frame = frame;
    
    [_mainScrinPagesScrollView addSubview:_mainStatsViewController.view];
    [_mainScrinPagesScrollView addSubview:_toolsViewController.view];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}



@end

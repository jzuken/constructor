//
//  QRWBaseDashboardPageViewController.m
//  QRealWeb_iOSApp
//
//  Created by Иван Афанасьев on 22.07.13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import "QRWBaseDashboardPageViewController.h"

@interface QRWBaseDashboardPageViewController ()
{
    NSString *pageImageName;
}

@end

@implementation QRWBaseDashboardPageViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}


- (id)initWithNameOfPageImage: (NSString *) nameOfPageImage nibName: (NSString *) nibName oldNibName: (NSString *) oldNibName viewControllerForPresent: (UIViewController *) forPresentViewController;
{
    self = [self initWithNibName:nibName oldNibName:oldNibName];
    pageImageName = nameOfPageImage;
    _forPresentViewController = forPresentViewController;
    return self;
}


- (void)viewDidLoad
{
    [super viewDidLoad];
    _segmentImageNamesArray = [NSArray arrayWithObjects: [UIImage imageNamed:@"button_since_last_login.jpg"],
                               [UIImage imageNamed:@"button_today.jpg"],
                               [UIImage imageNamed:@"button_this_month.jpg"],
                               [UIImage imageNamed:@"button_this_week.jpg"], nil];
    
    _segmentSelectedImageNamesArray = [NSArray arrayWithObjects: [UIImage imageNamed:@"active_button_since_last_login.png"],
                                       [UIImage imageNamed:@"active_button_today.png"],
                                       [UIImage imageNamed:@"active_button_this_month.png"],
                                       [UIImage imageNamed:@"active_button_this_week.png"], nil];
    
    [self.nameOfPageImageView setImage:[UIImage imageNamed:pageImageName]];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}



- (UIImage *) imageForSegmentedControlWithName: (NSString *) name
{
    return [[UIImage imageNamed:name] resizableImageWithCapInsets:UIEdgeInsetsMake(0, 10, 0, 100)];
}

@end

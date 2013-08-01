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


- (id)initWithNameOfPageImage: (NSString *) nameOfPageImage nibName: (NSString *) nibName
{
    self = [self initWithNibName:nibName bundle:nil];
    pageImageName = nameOfPageImage;
    return self;
}


- (void)viewDidLoad
{
    [super viewDidLoad];
    
    NSArray *segmentImageNamesArray = [NSArray arrayWithObjects: @"button_since_last_login.jpg", @"button_this_month.jpg", @"button_this_week.jpg", @"button_today.jpg", nil];
    _timeAndTypeSegmentedControl = [[UISegmentedControl alloc] initWithFrame:_timeAndTypeSegmentedControlArea.frame];
    [_timeAndTypeSegmentedControl setDividerImage:[UIImage imageNamed:@"segmentedControl_separator.png"]
         forLeftSegmentState:UIControlStateNormal
           rightSegmentState:UIControlStateNormal
                  barMetrics:UIBarMetricsDefault];
    
    for (NSString *imgName in segmentImageNamesArray) {
        UIImage *segmentImage = [UIImage imageWithCGImage:[[UIImage imageNamed:imgName] CGImage] scale:1.8 orientation:UIImageOrientationUp];
        [_timeAndTypeSegmentedControl insertSegmentWithImage:segmentImage atIndex:[segmentImageNamesArray indexOfObject:imgName] animated:NO];
    }
    [self.view addSubview:_timeAndTypeSegmentedControl];

    [self.nameOfPageImageView setImage:[UIImage imageNamed:pageImageName]];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}



@end

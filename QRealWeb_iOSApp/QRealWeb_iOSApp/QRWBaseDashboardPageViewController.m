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
    
//    CGRect frameOfSegmentedControl = _timeAndTypeSegmentedControlArea.frame;
////    frameOfSegmentedControl.origin.y -= frameOfSegmentedControl.size.height;
//    
//    self.timeAndTypeSegmentedControl = [[UISegmentedControl alloc] initWithFrame:frameOfSegmentedControl];
//    [self.timeAndTypeSegmentedControl setSelectedSegmentIndex:0];
    
    _segmentImageNamesArray =
    [NSArray arrayWithObjects: [UIImage imageNamed:@"button_since_last_login.jpg"],
                               [UIImage imageNamed:@"button_today.jpg"],
                               [UIImage imageNamed:@"button_this_month.jpg"],
                               [UIImage imageNamed:@"button_this_week.jpg"], nil];
//    @[[self imageForSegmentedControlWithName:@"button_since_last_login.jpg"], [self imageForSegmentedControlWithName:@"button_today.jpg"],[self imageForSegmentedControlWithName:@"button_this_month.jpg"],[self imageForSegmentedControlWithName:@"button_this_week.jpg"],];
    
    _segmentSelectedImageNamesArray = [NSArray arrayWithObjects: [UIImage imageNamed:@"active_button_since_last_login.png"],
                                       [UIImage imageNamed:@"active_button_today.png"],
                                       [UIImage imageNamed:@"active_button_this_month.png"],
                                       [UIImage imageNamed:@"active_button_this_week.png"], nil];
//    [self.timeAndTypeSegmentedControl setDividerImage:[UIImage imageNamed:@"segmentedControl_separator.png"]
//                                  forLeftSegmentState:UIControlStateNormal
//                                    rightSegmentState:UIControlStateNormal
//                                           barMetrics:UIBarMetricsDefault];
//    for (NSString *imgName in _segmentImageNamesArray) {
//        UIImage *segmentImage = [UIImage imageWithCGImage:[[UIImage imageNamed:imgName] CGImage] scale:1.8 orientation:UIImageOrientationUp];
//        if ([_segmentImageNamesArray indexOfObject:imgName] == self.timeAndTypeSegmentedControl.selectedSegmentIndex) {
//            [self.timeAndTypeSegmentedControl insertSegmentWithImage:segmentImage atIndex:[_segmentSelectedImageNamesArray indexOfObject:imgName] animated:NO];
//        } else {
//            [self.timeAndTypeSegmentedControl insertSegmentWithImage:segmentImage atIndex:[_segmentImageNamesArray indexOfObject:imgName] animated:NO];
//        }
//    }
//    
//    
//    [self.view addSubview:self.timeAndTypeSegmentedControl];
    
    [self.nameOfPageImageView setImage:[UIImage imageNamed:pageImageName]];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}



- (void) presentSegmentedControl
{
//    [UIView animateWithDuration:0.2 animations:^{
//        CGRect frameOfSegmentedControl = self.timeAndTypeSegmentedControlArea.frame;
//        self.timeAndTypeSegmentedControl.frame = frameOfSegmentedControl;
//    }];
    
}


- (void) dismissSegmentedControl
{
//    [UIView animateWithDuration:0.2 animations:^{
//        CGRect frameOfSegmentedControl = _timeAndTypeSegmentedControlArea.frame;
//        frameOfSegmentedControl.origin.x -= frameOfSegmentedControl.size.height;
//        self.timeAndTypeSegmentedControl.frame = frameOfSegmentedControl;
//    }];
}


- (UIImage *) imageForSegmentedControlWithName: (NSString *) name
{
    return [[UIImage imageNamed:name] resizableImageWithCapInsets:UIEdgeInsetsMake(0, 10, 0, 100)];
}

@end

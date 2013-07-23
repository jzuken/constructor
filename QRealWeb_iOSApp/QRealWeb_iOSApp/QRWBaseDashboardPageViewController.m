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
    NSArray *segmentImageNamesArray;
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
    
//    UISegmentedControl *filterSegmentedControl = [[UISegmentedControl alloc] initWithFrame:CGRectMake(0, 30, 320, 60)];
//    NSArray *segmentImageNamesArray = [NSArray arrayWithObjects: [UIImage imageNamed:@"button_since_last_login.png"], [UIImage imageNamed:@"button_this_month.png"], [UIImage imageNamed:@"button_this_week.png"], [UIImage imageNamed:@"button_today.png"], nil];

    //segmentImageNamesArray = [NSArray arrayWithObjects: @"button_since_last_login.png", @"button_this_month.png", @"button_this_week.png", @"button_today.png", nil];
////    _timeAndTypeSegmentedControl = [[UISegmentedControl alloc] initWithItems:segmentImageNamesArray];
//     UISegmentedControl *filterSegmentedControl = [[UISegmentedControl alloc] initWithFrame:CGRectMake(0, 30, 320, 60)];
//    filterSegmentedControl.segmentedControlStyle = UISegmentedControlStyleBar;
//    for (NSString *imageName in segmentImageNamesArray) {
//        [filterSegmentedControl insertSegmentWithTitle:@"" atIndex:[segmentImageNamesArray indexOfObject:imageName] animated:NO];
//        [filterSegmentedControl setBackgroundImage: [UIImage imageNamed:@"button_since_last_login.png"] forState:UIControlStateNormal barMetrics:UIBarMetricsDefault];//:[UIImage imageNamed:imageName] forSegmentAtIndex:[segmentImageNamesArray indexOfObject:imageName]];
//    }
//    [self.view addSubview:filterSegmentedControl];
    
    
//    QRWCustomSegmentedControl *control = [[QRWCustomSegmentedControl alloc] initWithSegmentCount:segmentImageNamesArray.count segmentsize:CGSizeMake(80, 60) dividerImage:[[UIImage alloc] init] tag:12 delegate:self];
//    control.frame = self.timeAndTypeCustomSegmentedControl.frame;
//    control.backgroundColor = [UIColor yellowColor];
//    [self.view addSubview:control];

    [self.nameOfPageImageView setImage:[UIImage imageNamed:pageImageName]];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

//- (UIButton *) buttonFor:(QRWCustomSegmentedControl*)segmentedControl atIndex:(NSUInteger)segmentIndex
//{
//    UIButton *segmentButton = [UIButton buttonWithType:UIButtonTypeCustom];
//    segmentButton.frame = CGRectMake(0, 0, 80, 60);
//    segmentButton.backgroundColor = [UIColor colorWithRed:28 green:103 blue:152 alpha:1];
//    segmentButton.titleLabel.lineBreakMode = NSLineBreakByWordWrapping;
//    segmentButton.titleLabel.text = [segmentImageNamesArray objectAtIndex:segmentIndex];
//    segmentButton.titleLabel.textColor = [UIColor whiteColor];
//    segmentButton.titleLabel.textAlignment = NSTextAlignmentCenter;
////    [segmentButton.imageView setImage:[UIImage imageNamed:@"button_since_last_login.png"]];
////    [segmentButton setBackgroundImage:[UIImage imageNamed:@"button_since_last_login.png"] forState:UIControlStateNormal];
//    return segmentButton;
//}


@end

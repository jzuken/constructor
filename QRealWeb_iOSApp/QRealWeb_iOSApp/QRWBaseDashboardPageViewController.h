//
//  QRWBaseDashboardPageViewController.h
//  QRealWeb_iOSApp
//
//  Created by Иван Афанасьев on 22.07.13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import "QRWBaseViewController.h"
#import "HMSegmentedControl.h"

@interface QRWBaseDashboardPageViewController : QRWBaseViewController


@property (strong, nonatomic) IBOutlet UIView *timeAndTypeSegmentedControlArea;
@property (strong, nonatomic) IBOutlet UIImageView *nameOfPageImageView;
@property (strong, nonatomic) NSString *currentSegment;

@property (strong, nonatomic) NSArray *segmentImageNamesArray;
@property (strong, nonatomic) NSArray *segmentSelectedImageNamesArray;



@property (strong, nonatomic) UIViewController *forPresentViewController;

- (id)initWithNameOfPageImage: (NSString *) nameOfPageImage nibName: (NSString *) nibName oldNibName: (NSString *) oldNibName viewControllerForPresent: (UIViewController *) forPresentViewController;


@end

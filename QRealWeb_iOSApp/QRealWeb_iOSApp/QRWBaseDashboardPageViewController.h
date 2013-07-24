//
//  QRWBaseDashboardPageViewController.h
//  QRealWeb_iOSApp
//
//  Created by Иван Афанасьев on 22.07.13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import "QRWBaseViewController.h"

@interface QRWBaseDashboardPageViewController : QRWBaseViewController


@property (strong, nonatomic) IBOutlet UIView *timeAndTypeSegmentedControlArea;
@property (strong, nonatomic) IBOutlet UIImageView *nameOfPageImageView;



- (id)initWithNameOfPageImage: (NSString *) nameOfPageImage nibName: (NSString *) nibName;

@end

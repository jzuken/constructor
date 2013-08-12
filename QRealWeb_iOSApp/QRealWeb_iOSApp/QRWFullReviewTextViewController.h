//
//  QRWFullReviewTextViewController.h
//  QRealWeb_iOSApp
//
//  Created by Ivan Afanasiev on 8/7/13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import "QRWBaseViewController.h"
#import "QRWReview.h"

@interface QRWFullReviewTextViewController : QRWBaseViewController

@property (strong, nonatomic) IBOutlet UILabel *productLable;
@property (strong, nonatomic) IBOutlet UILabel *userLable;

@property (strong, nonatomic) IBOutlet UIScrollView *messageLableScrollView;

- (IBAction)exitButtonClicked:(id)sender;

- (id)initWithReview: (QRWReview *) review;

@end

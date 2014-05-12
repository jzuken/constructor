//
//  QRWReviewInfoViewController.h
//  XCartAdmin
//
//  Created by Иван Афанасьев on 12.05.14.
//  Copyright (c) 2014 Ivan Afanasiev. All rights reserved.
//

#import "QRWBaseViewController.h"

@interface QRWReviewInfoViewController : QRWBaseViewController


@property(nonatomic, strong) IBOutlet UIButton *productButton;
@property(nonatomic, strong) IBOutlet UIButton *deleteButton;

@property(nonatomic, strong) IBOutlet UIWebView *reviewWebView;


-(id)initWithReview:(QRWReview *)review;

@end

//
//  QRWReviewInfoViewController.m
//  XCartAdmin
//
//  Created by Иван Афанасьев on 12.05.14.
//  Copyright (c) 2014 Ivan Afanasiev. All rights reserved.
//

#import "QRWReviewInfoViewController.h"
#import "QRWProductInfoViewController.h"

@interface QRWReviewInfoViewController ()

@property(nonatomic, strong) QRWReview *review;

@end


@implementation QRWReviewInfoViewController


-(id)initWithReview:(QRWReview *)review
{
    self = [self init];
    self.review = review;
    return self;
}


- (void)viewDidLoad
{
    [super viewDidLoad];
    
    [self.reviewWebView loadHTMLString:self.review.message baseURL:nil];
    
    [self.deleteButton addTarget:self action:@selector(deleteReview) forControlEvents:UIControlEventTouchUpInside];
    [self.productButton addTarget:self action:@selector(openProduct) forControlEvents:UIControlEventTouchUpInside];
    
    [[self.deleteButton layer] setBorderWidth:1.0f];
    [self.deleteButton.layer setCornerRadius:4.0];
    [self.deleteButton.layer setBorderColor:[[UIColor redColor] CGColor]];
    
    [self.productButton setTitle:_review.product forState:UIControlStateNormal];
    [[self.productButton layer] setBorderWidth:1.0f];
    [self.productButton.layer setCornerRadius:4.0];
    [self.productButton.layer setBorderColor:[kTextBlueColor CGColor]];
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [self.navigationController setNavigationBarHidden:NO animated:YES];
    [self setNavigationBarColor:kYellowColor title: self.review.email];
}


-(void)deleteReview
{
    [self startLoadingAnimation];
    [QRWDataManager sendDeleteReviewRequestWithID:[self.review.reviewID intValue]
                                            block:^(BOOL isSuccess, NSError *error) {
                                                [self stopLoadingAnimation];
                                                if (isSuccess) {
                                                    [self showSuccesView];
                                                    [self.navigationController popViewControllerAnimated:YES];
                                                } else {
                                                    [self showErrorView];
                                                }
                                            }];
}

-(void)openProduct
{
    [self startLoadingAnimation];
    [QRWDataManager sendProductInfoRequestWithID:[self.review.productID intValue]
                                           block:^(QRWProductWithInfo *product, NSError *error) {
                                               [self stopLoadingAnimation];
                                               QRWProductInfoViewController *productInfoViewController = [[QRWProductInfoViewController alloc] initWithProduct:product];
                                               [self.navigationController pushViewController:productInfoViewController animated:YES];
                                           }];
}


@end

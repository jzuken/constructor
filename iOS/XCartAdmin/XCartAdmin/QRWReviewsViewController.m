//
//  QRWReviewsViewController.m
//  XCartAdmin
//
//  Created by Ivan Afanasiev on 08/01/14.
//  Copyright (c) 2014 Ivan Afanasiev. All rights reserved.
//

#import "QRWReviewsViewController.h"

@interface QRWReviewsViewController ()

@end

@implementation QRWReviewsViewController


- (void)viewDidLoad
{
    [super viewDidLoad];
    
    [QRWDataManager sendReviewsRequestFromPoint:0
                                        toPoint:10
                                          block:^(NSArray *reviews, NSError *error) {
                                              self.dataArray = [NSArray arrayWithArray:reviews];
                                          }];
}


- (void)viewWillAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    [self.navigationController setNavigationBarHidden:NO animated:YES];
}



- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 110;
}




@end

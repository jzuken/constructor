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

    [QRWDataManager sendReviewsRequestFromPoint:self.dataArray.count
                                        toPoint:kNumberOfLoadedItems
                                          block:^(NSArray *reviews, NSError *error) {
                                              self.dataArray = [NSArray arrayWithArray:reviews];
                                              [self.tableView reloadData];
                                          }];
}


- (void)viewWillAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    [self.navigationController setNavigationBarHidden:NO animated:YES];
    [self setNavigationBarColor:kYellowColor title: QRWLoc(@"REVIEWS")];
}



- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 110;
}




@end

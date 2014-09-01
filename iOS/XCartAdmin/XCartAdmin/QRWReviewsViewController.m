//
//  QRWReviewsViewController.m
//  XCartAdmin
//
//  Created by Ivan Afanasiev on 08/01/14.
//  Copyright (c) 2014 Ivan Afanasiev. All rights reserved.
//

#import "QRWReviewsViewController.h"
#import "QRWReviewCell.h"
#import "QRWReviewInfoViewController.h"

@interface QRWReviewsViewController ()


@end

@implementation QRWReviewsViewController


- (void)viewDidLoad
{
    [super viewDidLoad];
    self.baseCell = [QRWReviewCell new];
    self.noResultsText = QRWLoc(@"NORES_REVIEWS");
    
    self.requestSearchBar.backgroundColor = kYellowColor;
}


- (void)loadObjectsWithSearchString:(NSString *)searchString asEmptyArray:(BOOL)asEmpty
{
    [self startLoadingAnimation];
    [QRWDataManager sendReviewsRequestFromPoint:asEmpty? 0 : self.dataArray.count
                                        toPoint:kNumberOfLoadedItems
                                          block:^(NSArray *reviews, NSError *error) {
                                              [self smartAddObjectToDataArrayAsNew:asEmpty withLoaddedArray:reviews];
                                          }];
}


- (void)viewWillAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    
    [self.navigationController setNavigationBarHidden:NO animated:YES];
    [self setNavigationBarColor:kYellowColor title: QRWLoc(@"REVIEWS")];
    [self loadObjectsWithSearchString:@"" asEmptyArray:0 == self.dataArray.count];
}

- (void)viewDidDisappear:(BOOL)animated
{
    [super viewDidDisappear:animated];
    self.dataArray = [NSArray array];
}



-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    QRWReviewInfoViewController *reviewInfoViewController = [[QRWReviewInfoViewController alloc] initWithReview:(QRWReview *)self.dataArray[indexPath.section]];
    [self.navigationController pushViewController:reviewInfoViewController animated:YES];
    
}


- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 100;
}

- (void)configureCell:(UITableViewCell *)cell atIndexPath:(NSIndexPath *)indexPath
{
    [cell setAccessoryType:UITableViewCellAccessoryNone];
    
    QRWReview *review = [self.dataArray objectAtIndex:indexPath.section];
    [(QRWReviewCell *)cell emailLabel].text = review.email;
    [(QRWReviewCell *)cell messageLabel].text = review.message;
}


- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    UIView *headerView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 320, 30)];
    headerView.backgroundColor = kGreyColor;
    
    UILabel *nameLabel = [[UILabel alloc] initWithFrame:CGRectMake(0, 1, 320, 29)];
    nameLabel.font = [UIFont systemFontOfSize:15];
    QRWReview *review = [self.dataArray objectAtIndex:section];
    nameLabel.text = review.product;
    nameLabel.textColor = kTextBlueColor;
    nameLabel.adjustsFontSizeToFitWidth=YES;
    nameLabel.minimumScaleFactor = 0.5;
    nameLabel.backgroundColor = [UIColor whiteColor];
    
    [headerView addSubview:nameLabel];
    
    return headerView;
}



@end

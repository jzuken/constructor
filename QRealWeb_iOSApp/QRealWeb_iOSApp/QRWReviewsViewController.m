//
//  QRWReviewsViewController.m
//  QRealWeb_iOSApp
//
//  Created by Ivan Afanasiev on 8/5/13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import "QRWReviewsViewController.h"
#import "QRWReviewCell.h"

#import "QRWReview.h"

@interface QRWReviewsViewController ()


@property(nonatomic, strong) NSMutableArray *reviews;

@end

@implementation QRWReviewsViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}


- (id)init
{
    return [self initWithNibName:@"QRWReviewsViewController" bundle:nil];
}


- (void)viewDidLoad
{
    [super viewDidLoad];
    [dataManager sendReviewsRequest];
    
    self.navigationController.navigationBarHidden = NO;
    self.navigationItem.title = @"Discounts";
//    [self.navigationItem setRightBarButtonItem:[[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemAdd target:self action:@selector(openAddDiscountView)]];
    
    __weak QRWReviewsViewController *weakSelf = self;
    
    [_reviewsTableView addPullToRefreshWithActionHandler:^{
        [weakSelf reloadsTableView];
    }];
    
    //    [_discountsTableView addInfiniteScrollingWithActionHandler:^{
    ////        [weakSelf addsDataAndReloadsTableView];
    //    }];
    
    
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}

- (void) openAddDiscountView
{
    
}

- (void) reloadsTableView
{
    [dataManager sendReviewsRequest];
    [_reviews removeAllObjects];
    [_reviewsTableView reloadData];
}


- (void)respondsForReviewsRequest:(NSArray *)reviews
{
    if (_reviews.count == 0) {
        _reviews = [NSMutableArray arrayWithArray:reviews];
    } else {
        [_reviews addObjectsFromArray:reviews];
    }
    [_reviewsTableView reloadData];
    [_reviewsTableView.pullToRefreshView stopAnimating];
}

#pragma mark Action sheet methods

- (void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex
{
    switch (buttonIndex) {
        case 0:{
            
        }
            break;
    }
}

#pragma mark Table view methods


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    UIActionSheet *userActionSheet = [[UIActionSheet alloc] initWithTitle:nil
                                                                 delegate:self
                                                        cancelButtonTitle:NSLocalizedString(@"CANCEL", nil)
                                                   destructiveButtonTitle:nil
                                                        otherButtonTitles:NSLocalizedString(@"EDIT", nil), NSLocalizedString(@"DELETE", nil), nil];
    [userActionSheet showInView:self.view];
}


- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}


- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return _reviews.count;
}


- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 80;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSArray *topLevelObjects = [[NSBundle mainBundle] loadNibNamed:@"QRWReviewCell" owner:self options:nil];
    QRWReviewCell *cell = [topLevelObjects objectAtIndex:0];
    
    [self configureProductCell:cell atIndexPath:indexPath];
    
    return cell;
}

- (void)configureProductCell:(QRWReviewCell *)cell atIndexPath:(NSIndexPath *)indexPath
{
    cell.productLable.text = [(QRWReview *)_reviews[indexPath.row] product];
    cell.userLable.text = [NSString stringWithFormat:@"User: %@", [(QRWReview *)_reviews[indexPath.row] email]];
    cell.messageLable.text = [(QRWReview *)_reviews[indexPath.row] message];
}
@end

//
//  QRWReviewsViewController.m
//  QRealWeb_iOSApp
//
//  Created by Ivan Afanasiev on 8/5/13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import "QRWReviewsViewController.h"
#import "QRWReviewCell.h"
#import "QRWFullReviewTextViewController.h"

#import "QRWReview.h"

@interface QRWReviewsViewController ()
{
    int lastSelectedRow;
}

@property(nonatomic, strong) QRWFullReviewTextViewController *fullReviewTextViewController;
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
    [self addDataAndReloadsTableView];
    
    self.navigationController.navigationBarHidden = NO;
    self.navigationItem.title = NSLocalizedString(@"REVIEWS", nil);
//    [self.navigationItem setRightBarButtonItem:[[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemAdd target:self action:@selector(openAddDiscountView)]];
    
    __weak QRWReviewsViewController *weakSelf = self;
    
    [_reviewsTableView addPullToRefreshWithActionHandler:^{
        [weakSelf reloadsTableView];
    }];
    
    [_reviewsTableView addInfiniteScrollingWithActionHandler:^{
        [weakSelf addDataAndReloadsTableView];
    }];
    
    
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}


- (void) reloadsTableView
{
    [self addDataAndReloadsTableView];
    [_reviews removeAllObjects];
    [_reviewsTableView reloadData];
}

- (void) addDataAndReloadsTableView
{
    [dataManager sendReviewsRequestWithStartPoint:[_reviewsTableView numberOfRowsInSection:0] lenght:10];
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
    [_reviewsTableView.infiniteScrollingView stopAnimating];
}


- (void)respondsForUploadingRequest:(BOOL)status
{
    NSString *titleString;
    NSString *messageString;
    TLCompletionBlock cencelBlock;
    
    if (status) {
        titleString = NSLocalizedString(@"SUCCESS_TITLE", nil);
        messageString = NSLocalizedString(@"SUCCESS_DELETE_MESSAGE", nil);
        cencelBlock = ^{
            [self reloadsTableView];
        };
        
    } else {
        titleString = NSLocalizedString(@"FAIL_TITLE", nil);
        messageString = NSLocalizedString(@"FAIL_DELETE_MESSAGE", nil);
    }
    
    TLAlertView *alert = [[TLAlertView alloc] initWithTitle:titleString message:messageString inView:self.view cancelButtonTitle:NSLocalizedString(@"CANCEL", nil) confirmButton:nil];
    [alert handleCancel:cencelBlock handleConfirm:nil];
    [alert show];
}

#pragma mark Action sheet methods

- (void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex
{
    switch (buttonIndex) {
        case 0:{
            _fullReviewTextViewController = [[QRWFullReviewTextViewController alloc] initWithReview:[_reviews objectAtIndex:lastSelectedRow]];
            [self presentViewController:_fullReviewTextViewController animated:YES completion:nil];
        }
            break;
        case 1:{
            [dataManager uploadDeletedReviewWithReview:[_reviews objectAtIndex:lastSelectedRow]];
        }
        break;
    }
}


#pragma mark Table view methods


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    lastSelectedRow = indexPath.row;
    
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    UIActionSheet *userActionSheet = [[UIActionSheet alloc] initWithTitle:nil
                                                                 delegate:self
                                                        cancelButtonTitle:NSLocalizedString(@"CANCEL", nil)
                                                   destructiveButtonTitle:nil
                                                        otherButtonTitles:  NSLocalizedString(@"FULL_TEXT", nil), NSLocalizedString(@"DELETE", nil), nil];
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

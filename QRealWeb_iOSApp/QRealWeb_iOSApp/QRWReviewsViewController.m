//
//  QRWReviewsViewController.m
//  QRealWeb_iOSApp
//
//  Created by Ivan Afanasiev on 8/5/13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import "QRWReviewsViewController.h"
#import "QRWReviewCell.h"

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
    return [self initWithNibName:@"QRWDiscountsViewController" bundle:nil];
}


- (void)viewDidLoad
{
    [super viewDidLoad];
    [dataManager sendDiscountsRequest];
    
    self.navigationController.navigationBarHidden = NO;
    self.navigationItem.title = @"Discounts";
    [self.navigationItem setRightBarButtonItem:[[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemAdd target:self action:@selector(openAddDiscountView)]];
    
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
    [dataManager sendDiscountsRequest];
    [_reviews removeAllObjects];
    [_reviewsTableView reloadData];
}


- (void)respondsForDiscountsRequest:(NSArray *)discounts
{
    if (_reviews.count == 0) {
        _reviews = [NSMutableArray arrayWithArray:discounts];
    } else {
        [_reviews addObjectsFromArray:discounts];
    }
    [_reviewsTableView reloadData];
    [_reviewsTableView.pullToRefreshView stopAnimating];
}

#pragma mark Table view methods


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    UIActionSheet *userActionSheet = [[UIActionSheet alloc] initWithTitle:nil
                                                                 delegate:self
                                                        cancelButtonTitle:@"Cancel"
                                                   destructiveButtonTitle:nil
                                                        otherButtonTitles:@"Edit", @"Delete", nil];
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
//    cell.minPriceLable.text = [NSString stringWithFormat:@"Min price: %.2f$", [[(QRWDiscount *)_discounts[indexPath.row] minprice] floatValue]];
//    cell.discountLable.text = [NSString stringWithFormat:@"Discount: %.2f$", [[(QRWDiscount *)_discounts[indexPath.row] discount] floatValue]];
//    cell.discountTypeLable.text = [NSString stringWithFormat:@"Type: %@", [(QRWDiscount *)_discounts[indexPath.row] discountType] ];
//    
//    NSString *membershipWord;
//    switch ([[(QRWDiscount *)_discounts[indexPath.row] membershipid] intValue]) {
//        case 0:
//            membershipWord = @"All";
//            break;
//            
//        case 1:
//            membershipWord = @"Premium";
//            break;
//            
//        case 2:
//            membershipWord = @"Wholesaler";
//            break;
//            
//        default:
//            break;
//    }
//    cell.membershipLabel.text = [NSString stringWithFormat:@"Memebership: %@", membershipWord];
}
@end

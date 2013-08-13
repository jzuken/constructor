//
//  DLSDetailsOfDocumentViewController.m
//  FirstLineSoftwareTestProject
//
//  Created by Иван Афанасьев on 06.04.13.
//  Copyright (c) 2013 Иван Афанасьев. All rights reserved.
//

#import "QRWProductsViewController.h"
#import "QRWProductCell.h"


@interface QRWProductsViewController ()
{
    int lastSelectedRow;
}

@property (nonatomic, strong) NSMutableArray *productsArray;

@end

@implementation QRWProductsViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
    }
    return self;
}

- (id)init
{
    return [self initWithNibName:@"QRWProductsViewController" bundle:nil];
}


- (void)viewDidLoad
{
    [super viewDidLoad];
    self.navigationController.navigationBarHidden = NO;
    self.navigationItem.title = NSLocalizedString(@"PRODUCTS", nil);
    
    __weak QRWProductsViewController *weakSelf = self;
    
    [_productsTableView addPullToRefreshWithActionHandler:^{
        [weakSelf reloadsTableView];
    }];
    
    [_productsTableView addInfiniteScrollingWithActionHandler:^{
        [weakSelf addDataAndReloadsTableView];
    }];
    
    [self addDataAndReloadsTableView];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}


- (void) addDataAndReloadsTableView
{
    [dataManager sendProductsRequestWithStartPoint:[_productsTableView numberOfRowsInSection:0] lenght:10];
    if (isFirstDataLoading) {
        isFirstDataLoading = NO;
        [self startLoadingAnimation];
    }
}


- (void) reloadsTableView
{
    [self addDataAndReloadsTableView];
    [_productsArray removeAllObjects];
    [_productsTableView reloadData];
}

- (void)respondsForProductsRequest:(NSArray *)products
{
    [self stopLoadingAnimation];
    if (products.count == 0) {
        _productsArray = [NSMutableArray arrayWithArray:products];
    } else {
        [_productsArray addObjectsFromArray:products];
    }
    [_productsTableView reloadData];
    [_productsTableView.pullToRefreshView stopAnimating];
    [_productsTableView.infiniteScrollingView stopAnimating];
}


#pragma mark Action sheet methods

- (void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex
{
//    switch (buttonIndex) {
//        case 0:{
//            _fullReviewTextViewController = [[QRWFullReviewTextViewController alloc] initWithReview:[_reviews objectAtIndex:lastSelectedRow]];
//            [self.navigationController pushViewController:_fullReviewTextViewController animated:YES];
//        }
//            break;
//        case 1:{
//            [dataManager uploadDeletedReviewWithReview:[_reviews objectAtIndex:lastSelectedRow]];
//        }
//            break;
//    }
}


#pragma mark Table view methods


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    lastSelectedRow = indexPath.row;
    
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    UIActionSheet *userActionSheet = [[UIActionSheet alloc] initWithTitle:nil
                                                                 delegate:self
                                                        cancelButtonTitle: NSLocalizedString(@"CANCEL", nil)
                                                   destructiveButtonTitle:nil
                                                        otherButtonTitles: NSLocalizedString(@"EDIT", nil), NSLocalizedString(@"REMOVE_FROM_STORE", nil), nil];
    [userActionSheet showInView:self.view];
}


- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}


- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return self.productsArray.count;
}


- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 80;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSArray *topLevelObjects = [[NSBundle mainBundle] loadNibNamed:@"QRWProductCell" owner:self options:nil];
    QRWProductCell *cell = [topLevelObjects objectAtIndex:0];
    
    [self configureProductCell:cell atIndexPath:indexPath];
    
    return cell;
}



- (void)configureProductCell:(QRWProductCell *)cell atIndexPath:(NSIndexPath *)indexPath
{
    cell.productLable.text = [(QRWProduct *)_productsArray[indexPath.row] product];
    cell.priceLable.text = [NSString stringWithFormat:@"%.2f$", [[(QRWProduct *)_productsArray[indexPath.row] price] floatValue]];
    cell.freeShipingLable.text = [NSString stringWithFormat:NSLocalizedString(@"FREE_SHIPPING", nil), [[(QRWProduct *)_productsArray[indexPath.row] freeShiping] isEqualToString:@"N" ] ? @"NO" : @"YES"];
    cell.avaliableLable.text = [NSString stringWithFormat:NSLocalizedString(@"AVALIABLE", nil), [[(QRWProduct *)_productsArray[indexPath.row] avaliable] intValue]];
    cell.minAmountLable.text = [NSString stringWithFormat:NSLocalizedString(@"MIN_AMOUNT", nil), [[(QRWProduct *)_productsArray[indexPath.row] count] intValue]];
    cell.minAmountLable.text = [NSString stringWithFormat:NSLocalizedString(@"CODE", nil), [(QRWProduct *)_productsArray[indexPath.row] productcode]];
}
@end

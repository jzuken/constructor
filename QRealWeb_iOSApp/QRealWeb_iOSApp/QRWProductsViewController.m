//
//  DLSDetailsOfDocumentViewController.m
//  FirstLineSoftwareTestProject
//
//  Created by Иван Афанасьев on 06.04.13.
//  Copyright (c) 2013 Иван Афанасьев. All rights reserved.
//

#import "QRWProductsViewController.h"
#import "QRWProductCell.h"
#import "QRWEditProductViewController.h"



@interface QRWProductsViewController ()
{
    int lastSelectedRow;
    BOOL isSearchMode;
    BOOL keyboardIsShown;
}

@property (nonatomic, strong) NSMutableArray *productsArray;
@property (nonatomic, strong) QRWEditProductViewController *editProductViewController;


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
    
    isSearchMode = NO;
    keyboardIsShown = NO;
    
    [self addDataAndReloadsTableView];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}

- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    [self reloadsTableView];
}


- (void) addDataAndReloadsTableView
{
    if (isSearchMode) {
        [dataManager sendProductsRequestWithSearchWord:_productsSearchBar.text startPoint:[_productsTableView numberOfRowsInSection:0] lenght:10];
    } else {
        [dataManager sendProductsRequestWithStartPoint:[_productsTableView numberOfRowsInSection:0] lenght:10];
        if (isFirstDataLoading) {
            isFirstDataLoading = NO;
            [self startLoadingAnimation];
        }
    }
}

- (void) reloadsTableView
{
    [_productsArray removeAllObjects];
    [_productsTableView reloadData];
    [self addDataAndReloadsTableView];
    [_productsTableView setHidden:YES];
}

- (void)respondsForProductsRequest:(NSArray *)products
{
    [self stopLoadingAnimation];
    if (_productsArray.count == 0) {
        _productsArray = [NSMutableArray arrayWithArray:products];
    } else {
        [_productsArray addObjectsFromArray:products];
    }
    [_productsTableView reloadData];
    [_productsTableView.pullToRefreshView stopAnimating];
    [_productsTableView.infiniteScrollingView stopAnimating];
    [_productsTableView setHidden:NO];
}

- (void)respondsForUploadingRequest:(BOOL)status
{
    [self showAfterDeletedAlertWithSuccessStatus:status];
    if (status) {
        [_productsArray removeObjectAtIndex:lastSelectedRow];
        [_productsTableView reloadData];
    }
}

#pragma mark Action sheet methods

- (void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if (buttonIndex == actionSheet.destructiveButtonIndex) {
        [self showSureToDeleteItemAlertWithHandleCancel:nil handleConfirm:^{
            [dataManager uploadDeletedProductWithProduct:[_productsArray objectAtIndex:lastSelectedRow]];
            [self startLoadingAnimation];
        }];
    } else if (buttonIndex != actionSheet.cancelButtonIndex){
        _editProductViewController = [[QRWEditProductViewController alloc] initWithProduct:[_productsArray objectAtIndex:lastSelectedRow]];
        [self presentViewController:_editProductViewController animated:YES completion:nil];
    }
}

#pragma mark Search bar methods

- (void)searchBarSearchButtonClicked:(UISearchBar *)searchBar; 
{
    [self reloadsTableView];
}

- (void)searchBarTextDidBeginEditing:(UISearchBar *)searchBar
{
    isSearchMode = YES;
    keyboardIsShown = YES;
    _productsSearchBar.showsCancelButton = YES;
    _productsTableView.scrollEnabled = NO;
}



- (void)searchBarCancelButtonClicked:(UISearchBar *) searchBar
{
    if ([searchBar.text isEqualToString:@""]) {
        isSearchMode = NO;
        [self reloadsTableView];
    }
    [self dismissSearchFronView];
}


- (void) dismissSearchFronView
{
    [_productsSearchBar resignFirstResponder];
    
    _productsSearchBar.showsCancelButton = NO;
    _productsTableView.scrollEnabled = YES;
    _productsTableView.userInteractionEnabled = YES;
    
    keyboardIsShown = NO;
}

#pragma mark Table view methods


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    
    if (!keyboardIsShown) {
        lastSelectedRow = indexPath.row;
        
        UIActionSheet *productsActionSheet = [[UIActionSheet alloc] initWithTitle:nil
                                                                         delegate:self
                                                                cancelButtonTitle: NSLocalizedString(@"CANCEL", nil)
                                                           destructiveButtonTitle: NSLocalizedString(@"DELETE", nil)
                                                                otherButtonTitles: NSLocalizedString(@"EDIT_PRICE", nil), nil];
        [productsActionSheet showInView:self.view];
    }
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
    cell.codeLable.text = [NSString stringWithFormat:NSLocalizedString(@"CODE", nil), [(QRWProduct *)_productsArray[indexPath.row] productcode]];
}
@end

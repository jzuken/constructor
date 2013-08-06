//
//  QRWDiscountsViewController.m
//  QRealWeb_iOSApp
//
//  Created by Ivan Afanasiev on 8/5/13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import "QRWDiscountsViewController.h"
#import "QRWDiscount.h"
#import "QRWDiscountCell.h"
#import "QRWDiscountEditFormViewController.h"

@interface QRWDiscountsViewController ()
{
    int lastSelectedRow;
}


@property (nonatomic, strong) NSMutableArray *discounts;
@property (nonatomic, strong) QRWDiscountEditFormViewController *discountEditFormViewController;


@end



@implementation QRWDiscountsViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {

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
    
    self.navigationController.navigationBarHidden = NO;
    self.navigationItem.title = NSLocalizedString(@"DISCOUNTS", nil);
    [self.navigationItem setRightBarButtonItem:[[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemAdd target:self action:@selector(openAddDiscountView)]];
    __weak QRWDiscountsViewController *weakSelf = self;
    
    [_discountsTableView addPullToRefreshWithActionHandler:^{
        [weakSelf reloadsTableView];
    }];
    
//    [_discountsTableView addInfiniteScrollingWithActionHandler:^{
////        [weakSelf addsDataAndReloadsTableView];
//    }];
    

}


- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    [self reloadsTableView];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}

- (void) openAddDiscountView
{
    _discountEditFormViewController = [[QRWDiscountEditFormViewController alloc] init];
    [self presentViewController:_discountEditFormViewController animated:YES completion:nil];
}

- (void) reloadsTableView
{
    [dataManager sendDiscountsRequest];
    [_discounts removeAllObjects];
    [_discountsTableView reloadData];
}


- (void)respondsForDiscountsRequest:(NSArray *)discounts
{
    if (_discounts.count == 0) {
        _discounts = [NSMutableArray arrayWithArray:discounts];
    } else {
        [_discounts addObjectsFromArray:discounts];
    }
    [_discountsTableView reloadData];
    [_discountsTableView.pullToRefreshView stopAnimating];
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
            _discountEditFormViewController = [[QRWDiscountEditFormViewController alloc] initWithDiscount:[_discounts objectAtIndex:lastSelectedRow]];
            [self presentViewController:_discountEditFormViewController animated:YES completion:nil];
        }
            break;
            
        case 1:
            [dataManager uploadDeletedDiscountWithDiscount:[_discounts objectAtIndex:lastSelectedRow]];
            break;
    }
}


#pragma mark Table view methods


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    lastSelectedRow = indexPath.row;
    
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
    return _discounts.count;
}


- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 80;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSArray *topLevelObjects = [[NSBundle mainBundle] loadNibNamed:@"QRWDiscountCell" owner:self options:nil];
    QRWDiscountCell *cell = [topLevelObjects objectAtIndex:0];
    
    [self configureProductCell:cell atIndexPath:indexPath];
    
    return cell;
}

- (void)configureProductCell:(QRWDiscountCell *)cell atIndexPath:(NSIndexPath *)indexPath
{
    cell.minPriceLable.text = [NSString stringWithFormat:NSLocalizedString(@"MIN_PRICE", nil), [[(QRWDiscount *)_discounts[indexPath.row] minprice] floatValue]];
    cell.discountLable.text = [NSString stringWithFormat:NSLocalizedString(@"DISCOUNT", nil), [[(QRWDiscount *)_discounts[indexPath.row] discount] floatValue]];
    cell.discountTypeLable.text = [NSString stringWithFormat:NSLocalizedString(@"TYPE", nil), [(QRWDiscount *)_discounts[indexPath.row] discountType] ];
    
    NSString *membershipWord;
    switch ([[(QRWDiscount *)_discounts[indexPath.row] membershipid] intValue]) {
        case 0:
            membershipWord = NSLocalizedString(@"ALL", nil);
            break;
            
        case 1:
            membershipWord = NSLocalizedString(@"PREMIUM", nil);
            break;
            
        case 2:
            membershipWord = NSLocalizedString(@"WHOLESALER", nil);
            break;
            
        default:
            break;
    }
    cell.membershipLabel.text = [NSString stringWithFormat:NSLocalizedString(@"MEMBERSHIP", nil), membershipWord];
}

@end

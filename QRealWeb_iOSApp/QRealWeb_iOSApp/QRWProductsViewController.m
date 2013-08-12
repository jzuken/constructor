//
//  DLSDetailsOfDocumentViewController.m
//  FirstLineSoftwareTestProject
//
//  Created by Иван Афанасьев on 06.04.13.
//  Copyright (c) 2013 Иван Афанасьев. All rights reserved.
//

#import "QRWProductsViewController.h"


@interface QRWProductsViewController ()
{
    int lastSelectedRow;
}

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
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
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


//- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
//{
//    NSArray *topLevelObjects = [[NSBundle mainBundle] loadNibNamed:@"QRWReviewCell" owner:self options:nil];
//    QRWReviewCell *cell = [topLevelObjects objectAtIndex:0];
//    
//    [self configureProductCell:cell atIndexPath:indexPath];
//    
//    return cell;
//}

//- (void)configureProductCell:(QRWReviewCell *)cell atIndexPath:(NSIndexPath *)indexPath
//{
//    cell.productLable.text = [(QRWReview *)_reviews[indexPath.row] product];
//    cell.userLable.text = [NSString stringWithFormat:@"User: %@", [(QRWReview *)_reviews[indexPath.row] email]];
//    cell.messageLable.text = [(QRWReview *)_reviews[indexPath.row] message];
//    
//}
@end

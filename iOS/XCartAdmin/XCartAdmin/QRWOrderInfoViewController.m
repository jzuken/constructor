//
//  QRWOrderInfoViewController.m
//  XCartAdmin
//
//  Created by Ivan Afanasiev on 14/01/14.
//  Copyright (c) 2014 Ivan Afanasiev. All rights reserved.
//

#import "QRWOrderInfoViewController.h"
#import "QRWOrderInfoTableViewCell.h"
#import "QRWPayPalViewController.h"

@interface QRWOrderInfoViewController ()<QRWPayPalViewControllerDelegate>


@end

@implementation QRWOrderInfoViewController


- (void)viewDidLoad
{
    [super viewDidLoad];
    self.tableView.showsInfiniteScrolling = NO;
    self.tableView.showsPullToRefresh = NO;
}

- (void)setOrderInfo:(QRWOrderInfo *)orderInfo
{
    _orderInfo = orderInfo;
    _orderInfo.paymentMethod = @"PayPalHere";
    _orderInfo.status = @"Q";
    [self.tableView reloadData];
}


#pragma mark - TableView



- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    QRWOrderInfoTableViewCell *cell;
    
    if (indexPath.section == 1) {
        cell = [tableView dequeueReusableCellWithIdentifier:@"QRWOrderInfoTableViewCellItem"];
        [cell configurateAsItemCell:_orderInfo.items[indexPath.row]];
        [cell setAccessoryType:UITableViewCellAccessoryNone];
    }
    if (indexPath.section == 0) {
        switch (indexPath.row) {
            case 0:{
                cell = [tableView dequeueReusableCellWithIdentifier:@"QRWOrderInfoTableViewCellFixed"];
                [cell configurateAsCellWithKey:@"Status" value:QRWLoc(_orderInfo.status)];
                [cell setAccessoryType:UITableViewCellAccessoryDisclosureIndicator];
            }
                break;
                
            case 1:{
                cell = [tableView dequeueReusableCellWithIdentifier:@"QRWOrderInfoTableViewCellFixed"];
                [cell configurateAsCellWithKey:@"Tracking number" value:_orderInfo.tracking];
                [cell setAccessoryType:UITableViewCellAccessoryDisclosureIndicator];
            }
                break;
                
            case 2:{
                cell = [tableView dequeueReusableCellWithIdentifier:@"QRWOrderInfoTableViewCellFixed"];
                [cell configurateAsCellWithKey:@"Payment method" value:_orderInfo.paymentMethod];
                if ([_orderInfo.paymentMethod isEqual: @"PayPalHere"] && [_orderInfo.status isEqual:@"Q"]) {
                    [cell setAccessoryType:UITableViewCellAccessoryDisclosureIndicator];
                } else {
                    [cell setAccessoryType:UITableViewCellAccessoryNone];
                }
            }
                break;
                
            case 3:{
                cell = [tableView dequeueReusableCellWithIdentifier:@"QRWOrderInfoTableViewCellFixed"];
                [cell configurateAsCellWithKey:@"Delivery method" value:_orderInfo.shipping];
                [cell setAccessoryType:UITableViewCellAccessoryNone];
            }
                break;
                
            case 4:{
                cell = [tableView dequeueReusableCellWithIdentifier:@"QRWOrderInfoTableViewCellFixed"];
                [cell configurateAsCellWithKey:@"Customer" value:_orderInfo.customer];
                [cell setAccessoryType:UITableViewCellAccessoryDisclosureIndicator];
            }
                break;
                
            case 5:{
                cell = [tableView dequeueReusableCellWithIdentifier:@"QRWOrderInfoTableViewCellInfo"];
                [cell configurateAsInfoCellWithKey:@"Billing info" value:_orderInfo.billingInfo phone:_orderInfo.bPhone];
                [cell setAccessoryType:UITableViewCellAccessoryNone];
            }
                break;
                
            case 6:{
                cell = [tableView dequeueReusableCellWithIdentifier:@"QRWOrderInfoTableViewCellInfo"];
                [cell configurateAsInfoCellWithKey:@"Shipping info" value:_orderInfo.shippingingInfo phone:_orderInfo.sPhone];
                [cell setAccessoryType:UITableViewCellAccessoryNone];
            }
                break;
        }
    }
    if (indexPath.section == 2) {
        switch (indexPath.row) {
            case 0:{
                cell = [tableView dequeueReusableCellWithIdentifier:@"QRWOrderInfoTableViewCellFixed"];
                [cell configurateAsCellWithKey:@"Subtotal" value:NSMoneyString(@"$",_orderInfo.subtotal)];
                [cell setAccessoryType:UITableViewCellAccessoryNone];
            }
                break;
                
            case 1:{
                cell = [tableView dequeueReusableCellWithIdentifier:@"QRWOrderInfoTableViewCellFixed"];
                [cell configurateAsCellWithKey:@"Discount" value:NSMoneyString(@"$",_orderInfo.discount)];
                [cell setAccessoryType:UITableViewCellAccessoryNone];
            }
                break;
                
            case 2:{
                cell = [tableView dequeueReusableCellWithIdentifier:@"QRWOrderInfoTableViewCellFixed"];
                [cell configurateAsCellWithKey:@"Coupon saving" value:NSMoneyString(@"$",_orderInfo.couponDiscount)];
                [cell setAccessoryType:UITableViewCellAccessoryNone];
            }
                break;
                
            case 3:{
                cell = [tableView dequeueReusableCellWithIdentifier:@"QRWOrderInfoTableViewCellFixed"];
                [cell configurateAsCellWithKey:@"Payment metod surcharge" value:NSMoneyString(@"$",_orderInfo.paymentSurcharge)];
                [cell setAccessoryType:UITableViewCellAccessoryNone];
            }
                break;
                
            case 4:{
                cell = [tableView dequeueReusableCellWithIdentifier:@"QRWOrderInfoTableViewCellFixed"];
                [cell configurateAsCellWithKey:@"Shipping cost" value:NSMoneyString(@"$",_orderInfo.shippingCost)];
                [cell setAccessoryType:UITableViewCellAccessoryNone];
            }
                break;
                
            case 5:{
                cell = [tableView dequeueReusableCellWithIdentifier:@"QRWOrderInfoTableViewCellTotal"];
                [cell configurateAsTotalCellWithValue:NSMoneyString(@"$",_orderInfo.total)];
                [cell setAccessoryType:UITableViewCellAccessoryNone];
            }
                break;
        }
    }
    
    return cell;
}


- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
 
    if (indexPath.section == 1) {
        return 60;
    }
    if (indexPath.section == 0) {
        switch (indexPath.row) {
            case 5:
                return 90;
            case 6:
                return 90;
            default:
                return 44;
        }
    }
    if (indexPath.section == 2) {
        return 44;
    } else {
        return 0;
    }
}


-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    switch (section) {
        case 0:
            return 7;
            break;
            
        case 1:
            return _orderInfo.items.count;
            break;
            
        case 2:
            return 6;
            break;
            
        default:
            return 0;
            break;
    }
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    if (indexPath.section == 0) {
        switch (indexPath.row) {

            case 0:{
                
            }
                break;
                
            case 1:{
                
            }
                break;
                
            case 2:{

                if ([_orderInfo.paymentMethod isEqual: @"PayPalHere"] && [_orderInfo.status isEqual:@"Q"]) {
                    QRWPayPalViewController *payPalViewController = [[UIStoryboard storyboardWithName:@"MainStoryboard" bundle:nil] instantiateViewControllerWithIdentifier:@"QRWPayPalViewController"];
                    payPalViewController.delegate = self;
                    payPalViewController.priceField.text = NSMoneyString(@"$",_orderInfo.total);
                    payPalViewController.taxField.text = NSMoneyString(@"$",_orderInfo.shippingCost);
                    payPalViewController.nameField.text = _orderInfo.customer;
                    [self presentViewController:payPalViewController animated:YES completion:nil];
                }
            }
                break;
                
            case 3:{
                
            }
                break;
                
            case 4:{
                
            }
                break;
        }
    }
}


- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    UIView *headerView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 320, 30)];
    headerView.backgroundColor = kGreyColor;
    
    UILabel *nameLabel = [[UILabel alloc] initWithFrame:CGRectMake(0, 1, 320, 29)];
    nameLabel.font = [UIFont systemFontOfSize:15];
    nameLabel.textColor = kTextBlueColor;
    nameLabel.adjustsFontSizeToFitWidth=YES;
    nameLabel.minimumScaleFactor = 0.5;
    nameLabel.backgroundColor = [UIColor whiteColor];
    
    switch (section) {
        case 0:
            nameLabel.text = @" Order info";
            break;
            
        case 1:
            nameLabel.text = @" Items";
            break;
            
        case 2:
            nameLabel.text = @" Order cost";
            break;
            
        default:
            break;
    }
    
    [headerView addSubview:nameLabel];
    
    return headerView;
}

-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 3;
}

-(void)onLaunchPaymentWasTapped:(BOOL)success
{
    [self dismissViewControllerAnimated:YES completion:^{
        if (!success) {
            _orderInfo.status = @"D";
        }
        [self.tableView reloadData];
    }];
}

@end

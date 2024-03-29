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
#import "QRWEditPriceView.h"
#import "QRWUserInfoViewController.h"
#import "QRWProductInfoViewController.h"
#import "QRWChoseSomethingViewController.h"
#import "QRWSettingsClient.h"
#import "NSDictionary+QRWSwap.h"

@interface QRWOrderInfoViewController ()<QRWPayPalViewControllerDelegate, QRWEditPriceViewDelegate, UIAlertViewDelegate>

@property (nonatomic, strong) QRWEditPriceView *editPriceView;

@end



@implementation QRWOrderInfoViewController


- (void)viewDidLoad
{
    [super viewDidLoad];
    [self addEditPriceView];
    self.tableView.showsInfiniteScrolling = NO;
    self.tableView.showsPullToRefresh = NO;
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    [self.navigationController setNavigationBarHidden:NO animated:YES];
    
    if (self.orderInfo) {
        [self reloadOrderInfo];
    }
}

- (void)setOrderInfo:(QRWOrderInfo *)orderInfo
{
    _orderInfo = orderInfo;
    [self setNavigationBarColor:kRedColor
                          title:[NSString stringWithFormat:@"%@ #%@", QRWLoc(@"ORDER"), self.orderInfo.orderid]];
    self.dataArray = self.orderInfo.items;
    [self.tableView reloadData];
}

- (void) addEditPriceView
{
    _editPriceView = [[QRWEditPriceView alloc] initWithFrame:CGRectMake(0, -kheightOfEditPriceView, self.view.frame.size.width, kheightOfEditPriceView)];
    _editPriceView.delegate = self;
    _editPriceView.priceTextField.keyboardType = UIKeyboardTypeDefault;
    [self.view addSubview:_editPriceView];
}

- (void)reloadOrderInfo
{
    [QRWDataManager sendOrderInfoRequestWithID:[self.orderInfo.orderid intValue] block:^(QRWOrderInfo *order, NSError *error) {
        [self setOrderInfo:order];
    }];
}

#pragma mark - TableView



- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    QRWOrderInfoTableViewCell *cell;
    
    if (indexPath.section == 1) {
        cell = [tableView dequeueReusableCellWithIdentifier:@"QRWOrderInfoTableViewCellItem"];
        [cell configurateAsItemCell:self.dataArray[indexPath.row]];
        [cell setAccessoryType:UITableViewCellAccessoryDisclosureIndicator];
    }
    if (indexPath.section == 0) {
        switch (indexPath.row) {
                
            case 0:{
                cell = [tableView dequeueReusableCellWithIdentifier:@"QRWOrderInfoTableViewCellFixed"];
                [cell configurateAsCellWithKey:@"Tracking number" value:_orderInfo.tracking];
                [cell setAccessoryType:UITableViewCellAccessoryDisclosureIndicator];
            }
                break;
                
            case 1:{
                cell = [tableView dequeueReusableCellWithIdentifier:@"QRWOrderInfoTableViewCellFixed"];
                [cell configurateAsCellWithKey:@"Payment method" value:_orderInfo.paymentMethod];
                if (_orderInfo.pphURLString) {
                    [cell setAccessoryType:UITableViewCellAccessoryDisclosureIndicator];
                } else {
                    [cell setAccessoryType:UITableViewCellAccessoryNone];
                }
            }
                break;
                
            case 2:{
                cell = [tableView dequeueReusableCellWithIdentifier:@"QRWOrderInfoTableViewCellStatic"];
                [cell configurateAsCellWithKey:@"Delivery method" value:_orderInfo.shipping];
                [cell setAccessoryType:UITableViewCellAccessoryNone];
            }
                break;
                
            case 3:{
                cell = [tableView dequeueReusableCellWithIdentifier:@"QRWOrderInfoTableViewCellFixed"];
                if ([[QRWSettingsClient getXCartVersion] isEqual:@"XCart4"]) {
                    [cell configurateAsCellWithKey:@"Status"
                                             value:[[QRWSettingsClient paymentStatusesCodeDictionary] qrw_swapKeyValue][self.orderInfo.status]];
                } else {
                    [cell configurateAsCellWithKey:@"Payment status"
                                             value:[[QRWSettingsClient paymentStatusesCodeDictionary] qrw_swapKeyValue][self.orderInfo.status]];
                }
                [cell setAccessoryType:UITableViewCellAccessoryDisclosureIndicator];
            }
                break;
                
            case 4:{
                cell = [tableView dequeueReusableCellWithIdentifier:@"QRWOrderInfoTableViewCellFixed"];
                if ([[QRWSettingsClient getXCartVersion] isEqual:@"XCart4"]) {
                    [cell configurateAsCellWithKey:@"Shipping status"
                                             value:[[QRWSettingsClient shippingStatusesCodeDictionary] qrw_swapKeyValue][self.orderInfo.shippingStatus]];
                    [cell setAccessoryType:UITableViewCellAccessoryDisclosureIndicator];
                }
            }
                break;
                
            case 5:{
                cell = [tableView dequeueReusableCellWithIdentifier:@"QRWOrderInfoTableViewCellStatic"];
                NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
                [formatter setDateFormat:@"yyyy-MM-dd HH:mm:ss zzz"];
                
                [cell configurateAsCellWithKey:@"Date"
                                         value:[formatter stringFromDate:[NSDate dateWithTimeIntervalSince1970:[self.orderInfo.date doubleValue]]]];
                [cell setAccessoryType:UITableViewCellAccessoryNone];
            }
                break;
                
            case 6:{
                cell = [tableView dequeueReusableCellWithIdentifier:@"QRWOrderInfoTableViewCellFixed"];
                [cell configurateAsCellWithKey:@"Customer" value:_orderInfo.customer];
                [cell setAccessoryType:UITableViewCellAccessoryDisclosureIndicator];
            }
                break;
                
            case 7:{
                cell = [tableView dequeueReusableCellWithIdentifier:@"QRWOrderInfoTableViewCellInfo"];
                [cell configurateAsInfoCellWithKey:@"Billing info" value:_orderInfo.billingInfo phone:_orderInfo.bPhone];
                [cell setAccessoryType:UITableViewCellAccessoryNone];
            }
                break;
                
            case 8:{
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
                cell = [tableView dequeueReusableCellWithIdentifier:@"QRWOrderInfoTableViewCellStatic"];
                [cell configurateAsCellWithKey:@"Subtotal" value:NSMoneyString([QRWSettingsClient getCurrency],_orderInfo.subtotal)];
                [cell setAccessoryType:UITableViewCellAccessoryNone];
            }
                break;
                
            case 1:{
                cell = [tableView dequeueReusableCellWithIdentifier:@"QRWOrderInfoTableViewCellStatic"];
                [cell configurateAsCellWithKey:@"Discount" value:NSMoneyString([QRWSettingsClient getCurrency],_orderInfo.discount)];
                [cell setAccessoryType:UITableViewCellAccessoryNone];
            }
                break;
                
            case 2:{
                cell = [tableView dequeueReusableCellWithIdentifier:@"QRWOrderInfoTableViewCellStatic"];
                [cell configurateAsCellWithKey:@"Coupon saving" value:NSMoneyString([QRWSettingsClient getCurrency],_orderInfo.couponDiscount)];
                [cell setAccessoryType:UITableViewCellAccessoryNone];
            }
                break;
                
            case 3:{
                cell = [tableView dequeueReusableCellWithIdentifier:@"QRWOrderInfoTableViewCellStatic"];
                [cell configurateAsCellWithKey:@"Payment metod surcharge" value:NSMoneyString([QRWSettingsClient getCurrency],_orderInfo.paymentSurcharge)];
                [cell setAccessoryType:UITableViewCellAccessoryNone];
            }
                break;
                
            case 4:{
                cell = [tableView dequeueReusableCellWithIdentifier:@"QRWOrderInfoTableViewCellStatic"];
                [cell configurateAsCellWithKey:@"Shipping cost" value:NSMoneyString([QRWSettingsClient getCurrency],_orderInfo.shippingCost)];
                [cell setAccessoryType:UITableViewCellAccessoryNone];
            }
                break;
                
            case 5:{
                cell = [tableView dequeueReusableCellWithIdentifier:@"QRWOrderInfoTableViewCellTotal"];
                [cell configurateAsTotalCellWithValue:NSMoneyString([QRWSettingsClient getCurrency],_orderInfo.total)];
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
        return 85;
    }
    if (indexPath.section == 0) {
        switch (indexPath.row) {
            case 5:
                return 90;
            case 6:
                return 90;
            case 4:{
                if ([[QRWSettingsClient getXCartVersion] isEqual:@"XCart4"]) {
                    return 0;
                } else {
                    return 44;
                }
            }
                
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
            return 9;
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
    __weak QRWOrderInfoViewController *weakSelf = self;
    
    if (indexPath.section == 0) {
        switch (indexPath.row) {
                
            case 0:{
                [QRWSettingsClient checkSubscriptionStatusesWithSuccessBlock:^{
                    if (![_editPriceView.priceTextField isFirstResponder]){
                        [self changeTracking];
                    }
                }];
            }
                break;
                
            case 1:{
                [QRWSettingsClient checkSubscriptionStatusesWithSuccessBlock:^{
                    if (_orderInfo.pphURLString) {
                        [[[UIAlertView alloc] initWithTitle:QRWLoc(@"PAYPALHERE")
                                                    message:QRWLoc(@"PAYPALHEREPROCESS")
                                                   delegate:self
                                          cancelButtonTitle:QRWLoc(@"CANCEL")
                                          otherButtonTitles:QRWLoc(@"PROCESS"), nil] show];
                    }
                }];
            }
                break;
                
            case 4:{
                [self startLoadingAnimation];
                [QRWDataManager sendUserInfoRequestWithID:[_orderInfo.userid intValue]
                                                    block:^(QRWUserInfo *userInfo, NSError *error) {
                                                        [self stopLoadingAnimation];
                                                        QRWUserInfoViewController *userInfoViewController = [[QRWUserInfoViewController alloc] initWithUserInfo:userInfo];
                                                        [self.navigationController pushViewController:userInfoViewController animated:YES];
                                                    }];
                
            }
                break;
                
            case 7:{
                [QRWSettingsClient checkSubscriptionStatusesWithSuccessBlock:^{
                    QRWChoseSomethingViewController *statusesOptionsViewController =
                    [[QRWChoseSomethingViewController alloc]
                     initWithOptionsDictionary:[QRWSettingsClient paymentStatuses]
                     selectedIndex: [[QRWSettingsClient paymentStatuses] indexOfObject:self.orderInfo.status]
                     type:QRWChoseSomethingViewControllerTypeStrings
                     selectOptionBlock:^(NSString *selectedOption) {
                         [self startLoadingAnimation];
                         [QRWDataManager sendOrderChangeStatusRequestWithID:[self.orderInfo.orderid intValue]
                                                                 pphDetails:nil
                                                                     status:[[QRWSettingsClient getXCartVersion] isEqual:@"XCart4"] ? selectedOption : @""
                                                              paymentStatus:![[QRWSettingsClient getXCartVersion] isEqual:@"XCart4"] ? selectedOption : @""
                                                             shippingStatus:@""
                                                                      block:^(BOOL isSuccess, NSError *error) {
                                                                          [weakSelf stopLoadingAnimation];
                                                                          if (isSuccess){
                                                                              _orderInfo.status = [QRWSettingsClient paymentStatusesCodeDictionary][selectedOption];
                                                                              [weakSelf showSuccesView];
                                                                              [weakSelf.tableView reloadData];
                                                                          } else {
                                                                              [weakSelf showErrorView];
                                                                          }
                                                                      }];
                     }];
                    statusesOptionsViewController.view.frame = self.view.frame;
                    [self.navigationController pushViewController:statusesOptionsViewController animated:YES];
                }];
            }
                break;
                
            case 8:{
                [QRWSettingsClient checkSubscriptionStatusesWithSuccessBlock:^{
                    QRWChoseSomethingViewController *statusesOptionsViewController =
                    [[QRWChoseSomethingViewController alloc]
                     initWithOptionsDictionary:[QRWSettingsClient shippingStatuses]
                     selectedIndex: [[QRWSettingsClient shippingStatuses] indexOfObject:self.orderInfo.shippingStatus]
                     type:QRWChoseSomethingViewControllerTypeStrings
                     selectOptionBlock:^(NSString *selectedOption) {
                         [self startLoadingAnimation];
                         [QRWDataManager sendOrderChangeStatusRequestWithID:[self.orderInfo.orderid intValue]
                                                                 pphDetails:nil
                                                                     status:@""
                                                              paymentStatus:@""
                                                             shippingStatus:selectedOption
                                                                      block:^(BOOL isSuccess, NSError *error) {
                                                                          [weakSelf stopLoadingAnimation];
                                                                          if (isSuccess){
                                                                              _orderInfo.shippingStatus =
                                                                              [QRWSettingsClient shippingStatusesCodeDictionary][selectedOption];
                                                                              [weakSelf showSuccesView];
                                                                              [weakSelf.tableView reloadData];
                                                                          } else {
                                                                              [weakSelf showErrorView];
                                                                          }
                                                                      }];
                     }];
                    statusesOptionsViewController.view.frame = self.view.frame;
                    [self.navigationController pushViewController:statusesOptionsViewController animated:YES];
                }];
            }
                break;
        }
    } else if (indexPath.section == 1) {
        [self startLoadingAnimation];
        [QRWDataManager sendProductInfoRequestWithID:[[(QRWProduct *)self.dataArray[indexPath.row] productid] intValue]
                                               block:^(QRWProductWithInfo *product, NSError *error) {
                                                   [self stopLoadingAnimation];
                                                   QRWProductInfoViewController *productInfoViewController = [[QRWProductInfoViewController alloc] initWithProduct:product];
                                                   [self.navigationController pushViewController:productInfoViewController animated:YES];
                                               }];
        [tableView deselectRowAtIndexPath:indexPath animated:YES];
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



#pragma mark - QRWEditTextView

- (void)changeTracking
{
    [_editPriceView.priceTextField becomeFirstResponder];
    [_editPriceView.priceTextField setText:_orderInfo.tracking];
}


- (void)saveButtonPressedWithPrice:(NSString *)newPrice
{
    [self startLoadingAnimation];
    [QRWDataManager sendOrderChangeTrackingNumberRequestWithID:self.orderInfo.orderid
                                                trackingNumber:newPrice
                                                         block:^(BOOL isSuccess, NSError *error) {
        [self stopLoadingAnimation];
        [_editPriceView.priceTextField resignFirstResponder];
        [self moveEditPriceViewToHeight: self.view.frame.size.height];
        if (isSuccess){
            _orderInfo.tracking = newPrice;
            [self.tableView reloadData];
            [self showSuccesView];
        } else {
            [self showErrorView];
        }
    }];
}


- (void) moveEditPriceViewToHeight:(CGFloat) height
{
    [UIView animateWithDuration:0.3 animations:^{
        CGRect frame = _editPriceView.frame;
        frame.origin.y = height;
        _editPriceView.frame = frame;
    }];
}

#pragma mark - Keyboard appears/disappear methods


- (void)changeTheTableViewHeight:(CGFloat)heightChange
{
    CGRect frame = self.tableView.frame;
    frame.size.height += heightChange;
    self.tableView.frame = frame;
    
    [self moveEditPriceViewToHeight:(heightChange < 0) ? 0 : - kheightOfEditPriceView];
}


#pragma mark - PayPal alert

- (void)changeOrderStatusAfterPayPalHere
{
    [QRWDataManager sendOrderChangeStatusRequestWithID:[self.orderInfo.orderid intValue]
                                            pphDetails:nil
                                                status:[[QRWSettingsClient getXCartVersion] isEqual:@"XCart4"] ? @"D" : @""
                                         paymentStatus:![[QRWSettingsClient getXCartVersion] isEqual:@"XCart4"] ? @"D" : @""
                                        shippingStatus:@""
                                                 block:^(BOOL isSuccess, NSError *error) {
        if (isSuccess){
            _orderInfo.status = @"D";
            [self showSuccesView];
            [self.tableView reloadData];
            [[NSUserDefaults standardUserDefaults] setObject:[NSNumber numberWithBool:NO]
                                                      forKey:self.orderInfo.orderid];
        } else {
            [self showErrorView];
        }
    }];
}


- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if (buttonIndex == alertView.cancelButtonIndex) {
        [self changeOrderStatusAfterPayPalHere];
    } else {
        UIApplication *application = [UIApplication sharedApplication];
        if ([application canOpenURL:[NSURL URLWithString: _orderInfo.pphURLString]]){
            [application openURL:[NSURL URLWithString: _orderInfo.pphURLString]];
        } else {
            NSURL *url = [NSURL URLWithString:@"itms://itunes.apple.com/us/app/paypal-here/id505911015?mt=8"];
            [application openURL:url];
        }
    }
}

@end

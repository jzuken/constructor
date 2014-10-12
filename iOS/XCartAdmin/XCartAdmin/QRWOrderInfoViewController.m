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
    
    if (self.orderInfo && [[NSUserDefaults standardUserDefaults] objectForKey:@"ChangePPHStatus"]) {
        [self changeOrderStatusAfterPayPalHere];
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
    _editPriceView = [[QRWEditPriceView alloc] initWithFrame:CGRectMake(0, self.view.frame.size.height, self.view.frame.size.width, kheightOfEditPriceView)];
    _editPriceView.delegate = self;
    _editPriceView.priceTextField.keyboardType = UIKeyboardTypeDefault;
    [self.view addSubview:_editPriceView];
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
                if (_orderInfo.pphURLString) {
                    [cell setAccessoryType:UITableViewCellAccessoryDisclosureIndicator];
                } else {
                    [cell setAccessoryType:UITableViewCellAccessoryNone];
                }
            }
                break;
                
            case 3:{
                cell = [tableView dequeueReusableCellWithIdentifier:@"QRWOrderInfoTableViewCellStatic"];
                [cell configurateAsCellWithKey:@"Delivery method" value:_orderInfo.shipping];
                [cell setAccessoryType:UITableViewCellAccessoryNone];
            }
                break;
                
            case 4:{
                cell = [tableView dequeueReusableCellWithIdentifier:@"QRWOrderInfoTableViewCellStatic"];
                NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
                [formatter setDateFormat:@"yyyy-MM-dd HH:mm:ss zzz"];
                
                [cell configurateAsCellWithKey:@"Date"
                                         value:[formatter stringFromDate:[NSDate dateWithTimeIntervalSince1970:[self.orderInfo.date doubleValue]]]];
                [cell setAccessoryType:UITableViewCellAccessoryNone];
            }
                break;
                
            case 5:{
                cell = [tableView dequeueReusableCellWithIdentifier:@"QRWOrderInfoTableViewCellFixed"];
                [cell configurateAsCellWithKey:@"Customer" value:_orderInfo.customer];
                [cell setAccessoryType:UITableViewCellAccessoryDisclosureIndicator];
            }
                break;
                
            case 6:{
                cell = [tableView dequeueReusableCellWithIdentifier:@"QRWOrderInfoTableViewCellInfo"];
                [cell configurateAsInfoCellWithKey:@"Billing info" value:_orderInfo.billingInfo phone:_orderInfo.bPhone];
                [cell setAccessoryType:UITableViewCellAccessoryNone];
            }
                break;
                
            case 7:{
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
                [cell configurateAsCellWithKey:@"Subtotal" value:NSMoneyString(@"$",_orderInfo.subtotal)];
                [cell setAccessoryType:UITableViewCellAccessoryNone];
            }
                break;
                
            case 1:{
                cell = [tableView dequeueReusableCellWithIdentifier:@"QRWOrderInfoTableViewCellStatic"];
                [cell configurateAsCellWithKey:@"Discount" value:NSMoneyString(@"$",_orderInfo.discount)];
                [cell setAccessoryType:UITableViewCellAccessoryNone];
            }
                break;
                
            case 2:{
                cell = [tableView dequeueReusableCellWithIdentifier:@"QRWOrderInfoTableViewCellStatic"];
                [cell configurateAsCellWithKey:@"Coupon saving" value:NSMoneyString(@"$",_orderInfo.couponDiscount)];
                [cell setAccessoryType:UITableViewCellAccessoryNone];
            }
                break;
                
            case 3:{
                cell = [tableView dequeueReusableCellWithIdentifier:@"QRWOrderInfoTableViewCellStatic"];
                [cell configurateAsCellWithKey:@"Payment metod surcharge" value:NSMoneyString(@"$",_orderInfo.paymentSurcharge)];
                [cell setAccessoryType:UITableViewCellAccessoryNone];
            }
                break;
                
            case 4:{
                cell = [tableView dequeueReusableCellWithIdentifier:@"QRWOrderInfoTableViewCellStatic"];
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
        return 85;
    }
    if (indexPath.section == 0) {
        switch (indexPath.row) {
            case 6:
                return 90;
            case 7:
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
            return 8;
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
                QRWChoseSomethingViewController *statusesOptionsViewController =
                [[QRWChoseSomethingViewController alloc] initWithOptionsDictionary:_statusColorsDictionary.allKeys
                                                                     selectedIndex: [_statusColorsDictionary.allKeys indexOfObject:self.orderInfo.status]
                                                                              type:QRWChoseSomethingViewControllerTypeStrings
                                                                 selectOptionBlock:^(NSString *selectedOption) {
                                                                     [self startLoadingAnimation];
                                                                     [QRWDataManager sendOrderChangeStatusRequestWithID:[self.orderInfo.orderid intValue]
                                                                                                                 status:selectedOption
                                                                                                                  block:^(BOOL isSuccess, NSError *error) {
                                                                                                                      [self stopLoadingAnimation];
                                                                                                                      if (isSuccess){
                                                                                                                          [self.navigationController popToViewController:self animated:YES];
                                                                                                                          _orderInfo.status = selectedOption;
                                                                                                                          [self showSuccesView];
                                                                                                                          [self.tableView reloadData];
                                                                                                                      } else {
                                                                                                                          [self showErrorView];
                                                                                                                      }
                                                                                                                  }];
                }];
                statusesOptionsViewController.view.frame = self.view.frame;
                [self.navigationController pushViewController:statusesOptionsViewController animated:YES];
                
            }
                break;
                
            case 1:{
                if (![_editPriceView.priceTextField isFirstResponder]){
                    [self changeTracking];
                }
                
            }
                break;
                
            case 2:{
                if (_orderInfo.pphURLString) {
                    [[[UIAlertView alloc] initWithTitle:QRWLoc(@"PAYPALHERE")
                                                message:QRWLoc(@"PAYPALHEREPROCESS")
                                               delegate:self
                                      cancelButtonTitle:QRWLoc(@"CANCEL")
                                      otherButtonTitles:QRWLoc(@"PROCESS"), nil] show];
                }
            }
                break;
                
            case 3:{
                
            }
                break;
                
            case 5:{
                [self startLoadingAnimation];
                [QRWDataManager sendUserInfoRequestWithID:[_orderInfo.userid intValue]
                                                    block:^(QRWUserInfo *userInfo, NSError *error) {
                                                        [self stopLoadingAnimation];
                                                        QRWUserInfoViewController *userInfoViewController = [[QRWUserInfoViewController alloc] initWithUserInfo:userInfo];
                                                        [self.navigationController pushViewController:userInfoViewController animated:YES];
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
    [self moveEditPriceViewToHeight:self.tableView.frame.size.height - kheightOfEditPriceView];
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


#pragma mark - PayPal alert

- (void)changeOrderStatusAfterPayPalHere
{
    [QRWDataManager sendOrderChangeStatusRequestWithID:[self.orderInfo.orderid intValue] status:@"D" block:^(BOOL isSuccess, NSError *error) {
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

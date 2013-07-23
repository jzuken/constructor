//
//  QRWLastOrderDashboardViewController.m
//  QRealWeb_iOSApp
//
//  Created by Иван Афанасьев on 23.07.13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import "QRWLastOrderDashboardViewController.h"

@interface QRWLastOrderDashboardViewController ()

@end

@implementation QRWLastOrderDashboardViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    [dataManager sendLastOrderRequest];
    
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}





- (void) setLastOrder:(QRWLastOrder *)lastOrder
{
    _lastOrder = lastOrder;
    
    
//    UILabel *firstNameLabel = [[UILabel alloc] initWithFrame:CGRectMake(20, 100, 280, 20)];
//    firstNameLabel.text = [NSString stringWithFormat:@"FIRST NAME: %@", _lastOrder.firstname];
//    UILabel *lastNameLabel = [[UILabel alloc] initWithFrame:CGRectMake(20, 130, 280, 20)];
//    lastNameLabel.text = [NSString stringWithFormat:@"LAST NAME: %@", _lastOrder.lastname];
//    UILabel *emailNameLabel = [[UILabel alloc] initWithFrame:CGRectMake(20, 160, 280, 20)];
//    emailNameLabel.text = [NSString stringWithFormat:@"EMAIL NAME: %@", _lastOrder.email];
//    
//    [self.view addSubview:firstNameLabel];
//    [self.view addSubview:lastNameLabel];
//    [self.view addSubview:emailNameLabel];
    
    _IDLabel.text = [_lastOrder.orderid stringValue];
    _nameLabel.text = [NSString stringWithFormat:@"%@ %@", _lastOrder.firstname, _lastOrder.lastname];
    _dateLabel.text = _lastOrder.date;
    _statusLabel.text = _lastOrder.status;
    _totalPriceLabel.text = [_lastOrder.total stringValue];
}




@end

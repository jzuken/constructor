//
//  QRWLastOrderDashboardViewController.m
//  QRealWeb_iOSApp
//
//  Created by Иван Афанасьев on 23.07.13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import "QRWLastOrderDashboardViewController.h"
#import "QRWProductsLastorderDashboardViewController.h"

@interface QRWLastOrderDashboardViewController ()

@property (nonatomic, strong) QRWLastOrder *lastOrder;

@property (nonatomic, strong) QRWProductsLastorderDashboardViewController *productsLastorderDashboardViewController;

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
    [self.timeAndTypeSegmentedControl removeFromSuperview];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}


- (void) setLastOrder:(QRWLastOrder *)lastOrder
{
    _lastOrder = lastOrder;
    
    _IDLabel.text = [_lastOrder.orderid stringValue];
    _nameLabel.text = [NSString stringWithFormat:@"%@ %@", _lastOrder.firstname, _lastOrder.lastname];
    _dateLabel.text = _lastOrder.date;
    _statusLabel.text = _lastOrder.status;
    _totalPriceLabel.text = [_lastOrder.total stringValue];
    [_productButton setTitle: [NSString stringWithFormat:@"%d items >", _lastOrder.products.count]forState:UIControlStateNormal];
    [_productButton addTarget:self action:@selector(openProductsView) forControlEvents:UIControlEventTouchUpInside];
}


- (void) openProductsView
{
    _productsLastorderDashboardViewController = [[QRWProductsLastorderDashboardViewController alloc] initWithProducts:_lastOrder.products];
    [self presentViewController:_productsLastorderDashboardViewController animated:YES completion:nil];
}

@end

//
//  QRWLastOrderDashboardViewController.m
//  QRealWeb_iOSApp
//
//  Created by Иван Афанасьев on 23.07.13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import "QRWLastOrderDashboardViewController.h"
#import "QRWProductsViewControllerForModalPresent.h"

@interface QRWLastOrderDashboardViewController ()

@property (nonatomic, strong) QRWLastOrder *lastOrder;

@property (nonatomic, strong) QRWProductsViewControllerForModalPresent *productsLastorderDashboardViewController;

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
    _statusLabel.text = NSLocalizedString(_lastOrder.status, nil);
    _totalPriceLabel.text = [NSString stringWithFormat:@"%.2f$", [_lastOrder.total floatValue]];
    [_productButton setTitle: [NSString stringWithFormat:@"%d items >", _lastOrder.products.count]forState:UIControlStateNormal];
    [_productButton addTarget:self action:@selector(openProductsView) forControlEvents:UIControlEventTouchUpInside];
}


- (void) openProductsView
{
    if (!_mainStatsInfoMode) {
        _productsLastorderDashboardViewController = [[QRWProductsViewControllerForModalPresent alloc] initWithProducts:_lastOrder.products];
        _productsLastorderDashboardViewController.mainStatsMode = NO;
        [self.forPresentViewController presentViewController:_productsLastorderDashboardViewController animated:YES completion:nil];
    } else {
        _productsLastorderDashboardViewController = [[QRWProductsViewControllerForModalPresent alloc] initWithProducts:_lastOrder.products];
        _productsLastorderDashboardViewController.mainStatsMode = YES;
        [self.forPresentViewController.navigationController pushViewController:_productsLastorderDashboardViewController animated:YES];
    }
}

@end

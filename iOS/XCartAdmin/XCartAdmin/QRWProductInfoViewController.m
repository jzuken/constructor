//
//  QRWProductInfoViewController.m
//  XCartAdmin
//
//  Created by Ivan Afanasiev on 10/01/14.
//  Copyright (c) 2014 Ivan Afanasiev. All rights reserved.
//

#import "QRWProductInfoViewController.h"
#import "UIImageView+AFNetworking.h"


@interface QRWProductInfoViewController ()

@property (nonatomic, strong) QRWProductWithInfo *product;

@end

@implementation QRWProductInfoViewController

- (id) initWithProduct:(QRWProductWithInfo *)product
{
    self = [self init];
    _product = product;
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    _nameLabel.text = _product.product;
    _availiabilitySwitcher.selected = YES;
    _inStock.text = NSStringFromInt([_product.available intValue]);
    [_descriptionWebView loadHTMLString:_product.description baseURL:nil];
    _priceButton.titleLabel.text = NSStringFromFloat([_product.price floatValue]);
    
    [_imageImageView setImageWithURL:[NSURL URLWithString:_product.imageURL] placeholderImage:[UIImage imageNamed:@"button_dashboard4.png"]];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}

@end

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
{
    BOOL _isFullDescription;
}

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
    
    _isFullDescription = NO;
    
    _nameLabel.text = _product.product;
    _availiabilitySwitcher.selected = YES;
    _inStock.text = NSStringFromInt([_product.available intValue]);
    [self setDescription];
    _priceButton.titleLabel.text = NSStringFromFloat([_product.price floatValue]);
    
    [_imageImageView setImageWithURL:[NSURL URLWithString:_product.imageURL] placeholderImage:[UIImage imageNamed:@"button_dashboard4.png"]];
    
    
    [_showFull addTarget:self action:@selector(showDescription) forControlEvents:UIControlEventTouchUpInside];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    [self.navigationController setNavigationBarHidden:NO animated:YES];
    [self setNavigationBarColor:kGreyColor title: QRWLoc(@"PRODUCT_INFO")];
}



- (void) setDescription
{
    if ([@"" isEqual:_product.description]) {
        _isFullDescription = YES;
        _showFull.hidden = YES;
    }
    if ([@"" isEqual:_product.fullDescription] || [_product.description isEqual:_product.fullDescription]) {
        _showFull.hidden = YES;
    }
    
    if (_isFullDescription) {
        [_descriptionWebView loadHTMLString:_product.fullDescription baseURL:nil];
    } else {
        [_descriptionWebView loadHTMLString:_product.description baseURL:nil];
    }
}

-(void)webViewDidFinishLoad:(UIWebView *)webView
{
    CGRect frame = webView.frame;
    frame.size.height = 1;
    webView.frame = frame;
    CGSize fittingSize = [webView sizeThatFits:CGSizeZero];
    frame.size = fittingSize;
    webView.frame = frame;
    
    [self.scrollView setContentSize:CGSizeMake(self.view.frame.size.width, frame.origin.y + frame.size.height)];
}




- (void)showDescription
{
    [_showFull setTitle: _isFullDescription ? QRWLoc(@"SHOW_FULL") : QRWLoc(@"SHOW_SHORT") forState:UIControlStateNormal];
    _isFullDescription = !_isFullDescription;
    [self setDescription];
}



@end

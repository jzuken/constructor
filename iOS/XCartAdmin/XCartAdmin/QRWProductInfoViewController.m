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

@property (nonatomic, strong) QRWEditPriceView *editPriceView;

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
    
    [self addEditPriceView];
    
    _isFullDescription = NO;
    
    _nameLabel.text = _product.product;
    _availiabilitySwitcher.selected = YES;
    _inStock.text = NSStringFromInt([_product.available intValue]);
    [self setDescription];
    [_priceButton setTitle:NSStringFromFloat([_product.price floatValue]) forState:UIControlStateNormal];
    
    [_imageImageView setImageWithURL:[NSURL URLWithString:_product.imageURL] placeholderImage:[UIImage imageNamed:@"loading.gif"]];
    
    
    [_showFull addTarget:self action:@selector(showDescription) forControlEvents:UIControlEventTouchUpInside];
    [_priceButton addTarget:self action:@selector(changePrice) forControlEvents:UIControlEventTouchUpInside];
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


- (void) addEditPriceView
{
    _editPriceView = [[QRWEditPriceView alloc] initWithFrame:CGRectMake(0, self.view.frame.size.height, self.view.frame.size.width, kheightOfEditPriceView)];
    _editPriceView.delegate = self;
    [self.view addSubview:_editPriceView];
}

#pragma mark - Description

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

- (void)showDescription
{
    [_showFull setTitle: _isFullDescription ? QRWLoc(@"SHOW_FULL") : QRWLoc(@"SHOW_SHORT") forState:UIControlStateNormal];
    _isFullDescription = !_isFullDescription;
    [self setDescription];
}

#pragma mark - WebView

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


#pragma mark - QRWEditTextView

- (void) changePrice
{
    [_editPriceView.priceTextField becomeFirstResponder];
    [self moveEditPriceViewToHeight:_scrollView.frame.size.height - kheightOfEditPriceView];
    [_editPriceView.priceTextField setText:[NSString stringWithFormat:@"%.2f", [_product.price floatValue]]];
}


- (void)saveButtonPressedWithPrice:(CGFloat)newPrice
{
    [self startLoadingAnimation];
    [QRWDataManager sendProductChangePriceRequestWithID:[_product.productid intValue] newPrice:newPrice block:^(BOOL isSuccess, NSError *error) {
        [self stopLoadingAnimation];
        [_editPriceView.priceTextField resignFirstResponder];
        [self moveEditPriceViewToHeight: _scrollView.frame.size.height];
        if (isSuccess){
            _product.price = [NSNumber numberWithFloat:newPrice];
            [_priceButton setTitle:NSStringFromFloat([_product.price floatValue]) forState:UIControlStateNormal];
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


- (void) changeTheTableViewHeight: (CGFloat) heightChange
{
    [UIView animateWithDuration:0.2 animations:^{
        CGRect frame = self.scrollView.frame;
        frame.size.height += heightChange;
        self.scrollView.frame = frame;
    }];
    
    [_scrollView scrollRectToVisible:_priceButton.frame animated:YES];
}

@end

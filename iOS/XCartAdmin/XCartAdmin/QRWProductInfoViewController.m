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
    [_availiabilitySwitcher addTarget: self action: @selector(changeAvaliability) forControlEvents: UIControlEventValueChanged];
    _availiabilitySwitcher.on = [@"Y" isEqual:_product.forSale];
    _inStock.text = NSStringFromInt([_product.available intValue]);
    
    [self setDescription];
    [self.descriptionWebView.scrollView setScrollEnabled:NO];
    
    [_priceButton setTitle:NSMoneyString(@"$", NSStringFromFloat([_product.price floatValue])) forState:UIControlStateNormal];
    [[_priceButton layer] setBorderWidth:1.0f];
    [_priceButton.layer setCornerRadius:4.0];
    [_priceButton.layer setBorderColor:[kTextBlueColor CGColor]];
    
    [_imageImageView setImageWithURL:[NSURL URLWithString:_product.imageURL] placeholderImage:[UIImage imageNamed:@"loading.gif"]];
    
    
    [_showFull addTarget:self action:@selector(showDescription) forControlEvents:UIControlEventTouchUpInside];
    [_priceButton addTarget:self action:@selector(changePrice) forControlEvents:UIControlEventTouchUpInside];
    
    UITapGestureRecognizer *tapRecog = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(userTapOnScreen:)];
    [self.view addGestureRecognizer:tapRecog];
    tapRecog.delegate = self;
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
    if ([@"" isEqual:_product.productDescription]) {
        _isFullDescription = YES;
        _showFull.hidden = YES;
    }
    if ([@"" isEqual:_product.fullDescription] || [_product.productDescription isEqual:_product.fullDescription]) {
        _showFull.hidden = YES;
    }
    
    if (_isFullDescription) {
        [_descriptionWebView loadHTMLString:_product.fullDescription baseURL:nil];
    } else {
        [_descriptionWebView loadHTMLString:_product.productDescription baseURL:nil];
    }
}

#pragma mark - actions

- (void)showDescription
{
    [_showFull setTitle: _isFullDescription ? QRWLoc(@"SHOW_FULL") : QRWLoc(@"SHOW_SHORT") forState:UIControlStateNormal];
    _isFullDescription = !_isFullDescription;
    [self setDescription];
}

- (void) changeAvaliability
{
    [self startLoadingAnimation];
    [QRWDataManager sendProductChangeAvaliabilityRequestWithID:[_product.productid integerValue]
                                                   isAvaliable:_availiabilitySwitcher.on
                                                         block:^(BOOL isSuccess, NSError *error) {
                                                             [self stopLoadingAnimation];
                                                             if (isSuccess) {
                                                                 [self showSuccesView];
                                                             } else {
                                                                 [self showErrorView];
                                                             }
                                                         }];
}



- (void) changePrice
{
    [_editPriceView.priceTextField becomeFirstResponder];
    [self moveEditPriceViewToHeight:_scrollView.frame.size.height - kheightOfEditPriceView];
    [_editPriceView.priceTextField setText:[NSString stringWithFormat:@"%.2f", [_product.price floatValue]]];
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


-(BOOL) webView:(UIWebView *)inWeb shouldStartLoadWithRequest:(NSURLRequest *)inRequest navigationType:(UIWebViewNavigationType)inType {
    if ( inType == UIWebViewNavigationTypeLinkClicked ) {
        [[UIApplication sharedApplication] openURL:[inRequest URL]];
        return NO;
    }
    
    return YES;
}

#pragma mark - QRWEditTextView



- (void)saveButtonPressedWithPrice:(CGFloat)newPrice
{
    [self startLoadingAnimation];
    [QRWDataManager sendProductChangePriceRequestWithID:[_product.productid intValue] newPrice:newPrice block:^(BOOL isSuccess, NSError *error) {
        [self stopLoadingAnimation];
        [_editPriceView.priceTextField resignFirstResponder];
        [self moveEditPriceViewToHeight: _scrollView.frame.size.height];
        if (isSuccess){
            _product.price = [NSNumber numberWithFloat:newPrice];
            [_priceButton setTitle:NSMoneyString(@"$", NSStringFromFloat([_product.price floatValue])) forState:UIControlStateNormal];
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

#pragma mark - GestureRecognizer 

-(void)userTapOnScreen:(UIGestureRecognizer *)sender
{
    [_editPriceView.priceTextField resignFirstResponder];
    [self moveEditPriceViewToHeight: _scrollView.frame.size.height];
}

-(BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer shouldReceiveTouch:(UITouch *)touch
{
    if (touch.view == _editPriceView  || touch.view == _priceButton) {
        return NO;
    }
    return YES;
}

@end

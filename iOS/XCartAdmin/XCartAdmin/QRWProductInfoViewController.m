//
//  QRWProductInfoViewController.m
//  XCartAdmin
//
//  Created by Ivan Afanasiev on 10/01/14.
//  Copyright (c) 2014 Ivan Afanasiev. All rights reserved.
//

#import "QRWProductInfoViewController.h"
#import "UIImageView+AFNetworking.h"
#import "QRWSettingsClient.h"
#import "QRWChoseSomethingViewController.h"

@interface QRWProductInfoViewController ()
{
    BOOL _isFullDescription;
}

@property (nonatomic, strong) QRWEditPriceView *editPriceView;
@property (nonatomic, strong) QRWProductWithInfo *product;

@property (nonatomic, strong) QRWProductVariant *currentVariant;

@end



@implementation QRWProductInfoViewController

- (id)init
{
    self = [[UIStoryboard storyboardWithName:@"MainStoryboard" bundle:nil] instantiateViewControllerWithIdentifier:@"QRWProductInfoViewController"];
    return self;
}

- (id)initWithProduct:(QRWProductWithInfo *)product
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
    
    [[_priceButton layer] setBorderWidth:1.0f];
    [_priceButton.layer setCornerRadius:4.0];
    [_priceButton.layer setBorderColor:[kTextBlueColor CGColor]];
    
    [self showVariantData];
    
    [_showFull addTarget:self action:@selector(showDescription) forControlEvents:UIControlEventTouchUpInside];
    [_priceButton addTarget:self action:@selector(changePrice) forControlEvents:UIControlEventTouchUpInside];
    
    UITapGestureRecognizer *tapRecog = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(userTapOnScreen:)];
    [self.view addGestureRecognizer:tapRecog];
    tapRecog.delegate = self;
    
    if (self.product.variants.count > 1) {
        UIBarButtonItem *variantsBarButtonItem = [[UIBarButtonItem alloc] initWithImage:[UIImage imageNamed:@"properties.png"] style:UIBarButtonItemStylePlain target:self action:@selector(showVariantsScreen)];
        self.navigationItem.rightBarButtonItem = variantsBarButtonItem;
    }
}

- (void)showVariantData
{
    CGFloat price = self.currentVariant ? [self.currentVariant.price floatValue]:[_product.price floatValue];
    NSURL *imageURL = [NSURL URLWithString:self.currentVariant ? self.currentVariant.imageURL: _product.imageURL];
    
    [_priceButton setTitle:NSMoneyString([QRWSettingsClient getCurrency], NSStringFromFloat(price)) forState:UIControlStateNormal];
    [_imageImageView setImageWithURL:imageURL placeholderImage:[UIImage imageNamed:@"loading.gif"]];
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


- (void)addEditPriceView
{
    _editPriceView = [[QRWEditPriceView alloc] initWithFrame:CGRectMake(0, self.view.frame.size.height, self.view.frame.size.width, kheightOfEditPriceView)];
    _editPriceView.delegate = self;
    [self.view addSubview:_editPriceView];
}

- (void)showVariantsScreen
{
    __weak QRWProductInfoViewController *weakSelf = self;
    [self.navigationController pushViewController:
     [[QRWChoseSomethingViewController alloc] initWithOptionsDictionary:self.product.variants
                                                          selectedIndex:0
                                                                   type:QRWChoseSomethingViewControllerTypeOptions
                                                      selectOptionBlock:^(id selectedOption) {
                                                          weakSelf.currentVariant = selectedOption;
                                                          [weakSelf showVariantData];
                                                      }]
                                         animated:YES];
}

#pragma mark - Description

- (void)setDescription
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

- (void)changeAvaliability
{
    BOOL isSuccess = [QRWSettingsClient checkSubscriptionStatusesWithSuccessBlock:^{
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
    }];
    if(!isSuccess){
        [self.availiabilitySwitcher setOn:!self.availiabilitySwitcher.on animated:YES];
    }
}


- (void)changePrice
{
    [QRWSettingsClient checkSubscriptionStatusesWithSuccessBlock:^{
        [_editPriceView.priceTextField becomeFirstResponder];
        [_editPriceView.priceTextField setText:[NSString stringWithFormat:@"%.2f", [_product.price floatValue]]];
    }];
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


-(BOOL)webView:(UIWebView *)inWeb shouldStartLoadWithRequest:(NSURLRequest *)inRequest navigationType:(UIWebViewNavigationType)inType
{
    if ( inType == UIWebViewNavigationTypeLinkClicked ) {
        [[UIApplication sharedApplication] openURL:[inRequest URL]];
        return NO;
    }
    
    return YES;
}

#pragma mark - QRWEditTextView



- (void)saveButtonPressedWithPrice:(NSString *)newPrice
{
    [self startLoadingAnimation];
    [QRWDataManager sendProductChangePriceRequestWithID:[_product.productid intValue]
                                              variantID:self.currentVariant.variantid
                                               newPrice:newPrice
                                                  block:^(BOOL isSuccess, NSError *error) {
        [self stopLoadingAnimation];
        [_editPriceView.priceTextField resignFirstResponder];
        [self moveEditPriceViewToHeight: _scrollView.frame.size.height];
        if (isSuccess){
            _product.price = [NSNumber numberWithFloat:[newPrice floatValue]];
            [_priceButton setTitle:NSMoneyString([QRWSettingsClient getCurrency], NSStringFromFloat([_product.price floatValue])) forState:UIControlStateNormal];
            [self showSuccesView];
        } else {
            [self showErrorView];
        }
    }];
}


- (void)moveEditPriceViewToHeight:(CGFloat)height
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
    CGRect frame = self.scrollView.frame;
    frame.size.height = (heightChange > 0) ? CGRectGetHeight(self.view.frame) : CGRectGetHeight(self.view.frame) + heightChange;
    frame.size.height += (heightChange < 0) ? - kheightOfEditPriceView : kheightOfEditPriceView;
    self.scrollView.frame = frame;
    
    [_scrollView scrollRectToVisible:_priceButton.frame animated:YES];
    
    [self moveEditPriceViewToHeight:_scrollView.frame.size.height];
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

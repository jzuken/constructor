//
//  QRWEditProductViewController.m
//  QRealWeb_iOSApp
//
//  Created by Иван Афанасьев on 14.08.13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import "QRWEditProductViewController.h"

@interface QRWEditProductViewController ()

@property (nonatomic, strong) QRWProduct *product;

@end

@implementation QRWEditProductViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
    }
    return self;
}

- (id)initWithProduct:(QRWProduct *)product
{
    self = [self initWithNibName:@"QRWEditProductViewController" bundle:nil];
    _product = product;
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    [_priceTextView setText:[NSString stringWithFormat:@"%.2f", [_product.price floatValue]]];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}


- (IBAction)uploadButtonClicked:(id)sender
{
    NSNumberFormatter * formatter = [[NSNumberFormatter alloc] init];
    [formatter setNumberStyle:NSNumberFormatterDecimalStyle];
    
    _product.price = [NSNumber numberWithDouble:[_priceTextView.text doubleValue]];
    
    if ([self isDiscountValid]) {
        [dataManager uploadDeletedProductWithProduct:_product];
        [self startLoadingAnimation];
    }
}

- (BOOL) isDiscountValid
{
    BOOL answer = YES;
    
    if ([_product.price floatValue] == 0.0) {
        answer = NO;
        TLAlertView *alert = [[TLAlertView alloc] initWithTitle:NSLocalizedString(@"ERROR", nil) message:NSLocalizedString(@"ZERO_FIELD_PRODUCT", nil) inView:self.view cancelButtonTitle:NSLocalizedString(@"OK", nil) confirmButton:nil];
        [alert show];
    }
    return answer;
}


#pragma mark - GestureRecognizer delegate

-(void)userTapOnScreen:(UIGestureRecognizer *)sender
{
    [_priceTextView resignFirstResponder];
}


-(BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer shouldReceiveTouch:(UITouch *)touch
{
    if ((touch.view == self.exitButton) || (touch.view == self.uploadButton))  {
        return NO;
    }
    return YES;
}
@end

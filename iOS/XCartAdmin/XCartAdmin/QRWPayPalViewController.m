//
//  PPViewController.m
//  PPHere
//
//  Created by Messerschmidt, Tim on 14.02.13.
//  Copyright (c) 2013 PayPal Developer. All rights reserved.
//

#import "QRWPayPalViewController.h"

CGSize keyboardSize;
int keyboardHidden;      // 0 @ initialization, 1 if shown, 2 if hidden

@interface QRWPayPalViewController ()

@end

@implementation QRWPayPalViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
    self.taxField.delegate = self;
    self.priceField.delegate = self;
    self.quantityField.delegate = self;
    self.nameField.delegate = self;
    self.descriptionField.delegate = self;
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(noticeShowKeyboard:) name:UIKeyboardWillShowNotification object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(noticeHideKeyboard:) name:UIKeyboardWillHideNotification object:nil];
}


-(void) noticeShowKeyboard:(NSNotification *)inNotification {
    keyboardSize = [[[inNotification userInfo] objectForKey:UIKeyboardFrameBeginUserInfoKey] CGRectValue].size;
    keyboardHidden = 1;
    [self layoutSubviews];        // Not sure if it is called automatically, so I called it
    
    
}
-(void) noticeHideKeyboard:(NSNotification *)inNotification {
    keyboardHidden = 2;
    [self layoutSubviews];       // Not sure if it is called automatically, so I called it
    
}

- (void) layoutSubviews
{
    UIScrollView *scrollview = self.mainScroolView;
    if(keyboardHidden == 1) {
        scrollview.frame = CGRectMake(scrollview.frame.origin.x, scrollview.frame.origin.y, scrollview.frame.size.width, scrollview.frame.size.height + keyboardSize.height);
    }
    else if(keyboardHidden == 2) {
        scrollview.frame = CGRectMake(scrollview.frame.origin.x, scrollview.frame.origin.y, scrollview.frame.size.width, scrollview.frame.size.height - keyboardSize.height);
    }
}


- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField {
    [textField resignFirstResponder];
    return YES;
}


- (IBAction)onLaunchPayment:(id)sender {
    NSLog(@"Launching");
    NSMutableDictionary *shirt = [NSMutableDictionary dictionary];
    NSMutableDictionary *itemList = [NSMutableDictionary dictionary];
    NSMutableDictionary *invoice = [NSMutableDictionary dictionary];
    
    [shirt setObject:self.taxField.text forKey:@"taxRate"];
    [shirt setObject:self.priceField.text forKey:@"unitPrice"];
    [shirt setObject:self.quantityField.text forKey:@"quantity"];
    [shirt setObject:self.nameField.text forKey:@"name"];
    [shirt setObject:self.descriptionField.text forKey:@"description"];
    [shirt setObject:@"Tax" forKey:@"taxName"];
    
    NSMutableArray *items = [NSMutableArray arrayWithObject:shirt];
    [itemList setObject:items forKey:@"item"];
    
    [invoice setObject:@"DueOnReceipt" forKey:@"paymentTerms"];
    [invoice setObject:@"0" forKey:@"discountPercent"];
    [invoice setObject:@"USD" forKey:@"currencyCode"];
    [invoice setObject:@"merchant@ebay.com" forKey:@"merchantEmail"];
    [invoice setObject:@"foo@bar.com" forKey:@"payerEmail"];
    [invoice setObject:itemList forKey:@"itemList"];
    
    
    NSError *error;
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:invoice
                                                       options:(NSJSONWritingOptions)(YES ? NSJSONWritingPrettyPrinted : 0)
                                                         error:&error];
    NSString *jsonInvoice;
    if (! jsonData) {
        NSLog(@"JSON SERIALIZATION ERROR: %@", error.localizedDescription);
        jsonInvoice = @"{}";
    } else {
        jsonInvoice = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
    }
    
    NSString *encodedInvoice = [jsonInvoice stringByAddingPercentEscapesUsingEncoding:NSASCIIStringEncoding];
    
    NSString *encodedPaymentTypes = [@"cash,card,paypal" stringByAddingPercentEscapesUsingEncoding:NSASCIIStringEncoding];
    
    NSString *encodedReturnUrl = [@"myapp://handler?{result}?Type={Type}&InvoiceId={InvoiceId}&Tip={Tip}&Email={Email}&TxId={TxId}" stringByAddingPercentEscapesUsingEncoding:NSASCIIStringEncoding];
    
    NSString *pphUrlString = [NSString stringWithFormat:@"paypalhere://takePayment?accepted=%@&returnUrl=%@&invoice=%@&step=choosePayment",
                              encodedPaymentTypes, encodedReturnUrl, encodedInvoice];
    
    NSURL *pphUrl = [NSURL URLWithString:pphUrlString];
    
    NSLog(@"%@", pphUrlString);
    
    UIApplication *application = [UIApplication sharedApplication];
    if ([application canOpenURL:pphUrl]){
        [application openURL:pphUrl];
    } else {
        NSURL *url = [NSURL URLWithString:@"itms://itunes.apple.com/us/app/paypal-here/id505911015?mt=8"];
        [application openURL:url];
    }
    
    [self dissmissPayPalScreen:self.nameField];
}


-(IBAction)dissmissPayPalScreen:(id)sender
{
    if ([self.delegate respondsToSelector:@selector(onLaunchPaymentWasTapped:)] ) {
        [self.delegate onLaunchPaymentWasTapped:[sender isEqual:self.nameField]];
    }
}

@end

//
//  PPViewController.h
//  PPHere
//
//  Created by Messerschmidt, Tim on 14.02.13.
//  Copyright (c) 2013 PayPal Developer. All rights reserved.
//

#import <UIKit/UIKit.h>


@protocol QRWPayPalViewControllerDelegate <NSObject>

-(void)onLaunchPaymentWasTapped:(BOOL)success;

@end


@interface QRWPayPalViewController : UIViewController <UITextFieldDelegate>

@property (weak, nonatomic) IBOutlet UITextField *priceField;
@property (weak, nonatomic) IBOutlet UITextField *taxField;
@property (weak, nonatomic) IBOutlet UITextField *nameField;
@property (weak, nonatomic) IBOutlet UITextField *quantityField;
@property (weak, nonatomic) IBOutlet UITextField *descriptionField;

@property (weak, nonatomic) IBOutlet UIScrollView *mainScroolView;

@property (strong, nonatomic) id<QRWPayPalViewControllerDelegate> delegate;

@end

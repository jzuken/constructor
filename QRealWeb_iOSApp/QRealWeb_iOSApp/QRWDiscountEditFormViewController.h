//
//  QRWDiscountEditFormViewController.h
//  QRealWeb_iOSApp
//
//  Created by Иван Афанасьев on 06.08.13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import "QRWBaseViewController.h"
#import "VCRadioButton.h"

@interface QRWDiscountEditFormViewController : QRWBaseViewController<UIGestureRecognizerDelegate>


@property (nonatomic, strong) IBOutlet UITextField *minPriceTextView;
@property (nonatomic, strong) IBOutlet UITextField *discountTextView;

@property (nonatomic, strong) IBOutlet VCRadioButton *absoluteTypeRadioButton;
@property (nonatomic, strong) IBOutlet VCRadioButton *percentTypeRadioButton;


@property (nonatomic, strong) IBOutlet VCRadioButton *premiumMembershipRadioButton;
@property (nonatomic, strong) IBOutlet VCRadioButton *wholesalerMembershipRadioButton;
@property (nonatomic, strong) IBOutlet VCRadioButton *allMembershipRadioButton;


@property (nonatomic, strong) IBOutlet UIButton *exitButton;
@property (nonatomic, strong) IBOutlet UIButton *uploadButton;


- (IBAction)exitButtonClicked:(id)sender;
- (IBAction)uploadButtonClicked:(id)sender;

- (id)initWithDiscount: (QRWDiscount *) discount;


@end
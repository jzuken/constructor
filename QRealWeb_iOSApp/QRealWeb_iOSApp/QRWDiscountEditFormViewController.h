//
//  QRWDiscountEditFormViewController.h
//  QRealWeb_iOSApp
//
//  Created by Иван Афанасьев on 06.08.13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import "QRWEditItemViewController.h"
#import "VCRadioButton.h"

@interface QRWDiscountEditFormViewController : QRWEditItemViewController


@property (nonatomic, strong) IBOutlet UITextField *minPriceTextView;
@property (nonatomic, strong) IBOutlet UITextField *discountTextView;

@property (nonatomic, strong) IBOutlet VCRadioButton *absoluteTypeRadioButton;
@property (nonatomic, strong) IBOutlet VCRadioButton *percentTypeRadioButton;


@property (nonatomic, strong) IBOutlet VCRadioButton *premiumMembershipRadioButton;
@property (nonatomic, strong) IBOutlet VCRadioButton *wholesalerMembershipRadioButton;
@property (nonatomic, strong) IBOutlet VCRadioButton *allMembershipRadioButton;

@property (nonatomic, strong) IBOutlet UIScrollView *forOpenKeyboardScrollView;

//@property (nonatomic, strong) IBOutlet UIButton *exitButton;
//@property (nonatomic, strong) IBOutlet UIButton *uploadButton;


- (id)initWithDiscount: (QRWDiscount *) discount;


@end

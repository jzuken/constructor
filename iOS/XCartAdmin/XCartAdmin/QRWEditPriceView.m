//
//  QRWEditPriceView.m
//  XCartAdmin
//
//  Created by Ivan Afanasiev on 13/01/14.
//  Copyright (c) 2014 Ivan Afanasiev. All rights reserved.
//

#import "QRWEditPriceView.h"
#import <QuartzCore/QuartzCore.h>
#import "constants.h"

@implementation QRWEditPriceView

- (instancetype)init
{
    self = [super init];
    if (self) {
        [[NSBundle mainBundle] loadNibNamed:@"QRWEditPriceView" owner:self options:nil];
        [self addSubview:self.view];
        self.view.backgroundColor = kGreyColor;
        _priceTextField.delegate = self;
        _priceTextField.keyboardType = UIKeyboardTypeNumberPad;
        
        [_saveButton.layer setBorderWidth:1.0f];
        [_saveButton.layer setCornerRadius:4.0];
        [_saveButton.layer setBorderColor:[[UIColor whiteColor] CGColor]];
    }
    return self;
}


- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        [[NSBundle mainBundle] loadNibNamed:@"QRWEditPriceView" owner:self options:nil];
        [self addSubview:self.view];
        self.view.backgroundColor = kGreyColor;
        _priceTextField.delegate = self;
        
        [_saveButton.layer setBorderWidth:1.0f];
        [_saveButton.layer setCornerRadius:4.0];
        [_saveButton.layer setBorderColor:[[UIColor whiteColor] CGColor]];
    }
    return self;
}


- (void) awakeFromNib
{
    [super awakeFromNib];
    [self addSubview:self.view];
}


-(void)textDidChangeText:(id)sender
{
    if ([@"" isEqualToString: _priceTextField.text] ) {
        [_saveButton setEnabled:NO];
    } else {
        [_saveButton setEnabled:YES];
    }
}


- (IBAction)saveButtonClicked:(id)sender
{
    if ([_delegate respondsToSelector:@selector(saveButtonPressedWithPrice:)]) {
        [_delegate saveButtonPressedWithPrice:_priceTextField.text];
    }
}
@end

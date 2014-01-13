//
//  QRWEditPriceView.h
//  XCartAdmin
//
//  Created by Ivan Afanasiev on 13/01/14.
//  Copyright (c) 2014 Ivan Afanasiev. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol QRWEditPriceViewDelegate<NSObject>

- (void) saveButtonPressedWithPrice: (CGFloat) newPrice;

@end

@interface QRWEditPriceView : UIView<UITextFieldDelegate>

@property (nonatomic, retain) IBOutlet UIView *view;
@property (nonatomic, strong) IBOutlet UITextField *priceTextField;
@property (nonatomic, strong) IBOutlet UIButton *saveButton;

@property (nonatomic, strong) id<QRWEditPriceViewDelegate> delegate;

- (IBAction)saveButtonClicked:(id)sender;

@end

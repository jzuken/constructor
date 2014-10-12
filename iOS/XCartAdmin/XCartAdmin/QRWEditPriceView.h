//
//  QRWEditPriceView.h
//  XCartAdmin
//
//  Created by Ivan Afanasiev on 13/01/14.
//  Copyright (c) 2014 Ivan Afanasiev. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol QRWEditPriceViewDelegate<NSObject>

- (void)saveButtonPressedWithPrice:(NSString *)newPrice;

@end



@interface QRWEditPriceView : UIView<UITextFieldDelegate>

@property (nonatomic, weak) IBOutlet UIView *view;
@property (nonatomic, weak) IBOutlet UITextField *priceTextField;
@property (nonatomic, weak) IBOutlet UIButton *saveButton;

@property (nonatomic, strong) id<QRWEditPriceViewDelegate> delegate;

- (IBAction)saveButtonClicked:(id)sender;

@end

//
//  QRWUnlockViewController.m
//  XCartAdmin
//
//  Created by Ivan Afanasiev on 10/01/14.
//  Copyright (c) 2014 Ivan Afanasiev. All rights reserved.
//

#import "QRWUnlockViewController.h"

@interface QRWUnlockViewController ()

@end

@implementation QRWUnlockViewController


- (void)viewDidLoad
{
    [super viewDidLoad];
    [self setPickerStartPosition];
}


- (IBAction)enterButton:(id)sender
{
    [UIView animateWithDuration:0.5 animations:^{
        CGRect frame = self.view.frame;
        frame.origin.y = frame.size.height;
        self.view.frame = frame;
    } completion:^(BOOL finished) {
        [self.view removeFromSuperview];
    }];
}



-(void) setPickerStartPosition
{
    for (int i = 0; i < _unlockPicker.numberOfComponents; i++) {
        [_unlockPicker selectRow:50250 inComponent:i animated:NO];
    }
}

#pragma mark - PickerView

- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView
{
    return 4;
}

- (NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component
{
    return 100500;
}


- (NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component
{
    return NSStringFromInt(row % 10);
}

@end

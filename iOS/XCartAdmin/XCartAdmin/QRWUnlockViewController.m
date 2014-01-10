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
}


- (IBAction)enterButton:(id)sender
{
    
}


#pragma mark - PickerView

- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView
{
    return 4;
}

- (NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component
{
    return 10;
}


- (NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component
{
    return NSStringFromInt(component);
}

@end

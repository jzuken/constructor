//
//  QRWUnlockViewController.m
//  XCartAdmin
//
//  Created by Ivan Afanasiev on 10/01/14.
//  Copyright (c) 2014 Ivan Afanasiev. All rights reserved.
//

#import "QRWUnlockViewController.h"
#import "QRWSettingsClient.h"

@interface QRWUnlockViewController ()

@end

@implementation QRWUnlockViewController


+(void)showUnlockViewOnViewController:(UIViewController *)viewController
{
    if ([[NSUserDefaults standardUserDefaults] objectForKey:@"QRW_PINenabled"]) {
        static QRWUnlockViewController *unlockViewController = nil;
        unlockViewController = [[QRWUnlockViewController alloc] init];

        [viewController presentViewController:unlockViewController
                                                         animated:YES
                                                       completion:nil];
    }
}


- (void)viewDidLoad
{
    [super viewDidLoad];
    [self setPickerStartPosition];
}


- (IBAction)enterButton:(id)sender
{
    NSMutableString *currentPIN = [NSMutableString new];
    
    for (int i = 0; i < _unlockPicker.numberOfComponents; i++) {
        [currentPIN appendString:[self pickerView:_unlockPicker titleForRow:[_unlockPicker selectedRowInComponent:i] forComponent:i]];
    }
    
    if ([[QRWSettingsClient getUnlockKey] isEqual:currentPIN]) {
        [self dismissViewControllerAnimated:YES completion:nil];
    } else {
        [[[UIAlertView alloc] initWithTitle:QRWLoc(@"NOT_CORRECT_UNLOCK_KEY_TITLE")
                                    message:QRWLoc(@"NOT_CORRECT_UNLOCK_KEY_MESSAGE")
                                   delegate:nil
                          cancelButtonTitle:QRWLoc(@"CANCEL")
                          otherButtonTitles: nil]
         show];
    }
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

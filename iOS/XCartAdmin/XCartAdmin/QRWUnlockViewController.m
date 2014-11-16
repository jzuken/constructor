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

@property(nonatomic) BOOL editPasswordMode;

@property(nonatomic) BOOL isPresent;

@end



@implementation QRWUnlockViewController


+ (instancetype)sharedInstance
{
    static id sharedInstance = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        sharedInstance = [[self alloc] init];
    });
    return  sharedInstance;
}

- (void)showUnlockViewOnViewController:(UIViewController *)viewController
{
    [self showUnlockViewOnViewController:viewController editPasswordMode:NO];
}

- (void)showUnlockViewOnViewController:(UIViewController *)viewController editPasswordMode:(BOOL)editPasswordMode
{
    if (!self.isPresent && [(NSNumber *)[[NSUserDefaults standardUserDefaults] objectForKey:@"QRW_isLogIn"] boolValue] &&
        [(NSNumber *)[[NSUserDefaults standardUserDefaults] objectForKey:@"QRW_PINenabled"] boolValue]) {
        self.editPasswordMode = editPasswordMode;
        self.isPresent = YES;
        [self setPickerStartPosition];
        [viewController presentViewController:self
                                     animated:YES
                                   completion:nil];
    }
}

- (IBAction)enterButton:(id)sender
{
    NSMutableString *currentPIN = [NSMutableString new];
    
    for (int i = 0; i < _unlockPicker.numberOfComponents; i++) {
        [currentPIN appendString:[self pickerView:_unlockPicker titleForRow:[_unlockPicker selectedRowInComponent:i] forComponent:i]];
    }
    
    if (self.editPasswordMode) {
        [QRWSettingsClient saveUnlockKey:currentPIN];
        self.isPresent = NO;
        [self dismissViewControllerAnimated:YES completion:nil];
    } else {
        [self startLoadingAnimation];
        [QRWDataManager checkTheSubscriptionStatusWithSuccessBlock:^(NSString *status) {
            [self stopLoadingAnimation];
            if ([status isEqual:@"expired"]) {
                [QRWSettingsClient saveSubscriptionStatus:QRWSubscriptionStatusExpired];
            } else if ([status isEqual:@"active"]) {
                [QRWSettingsClient saveSubscriptionStatus:QRWSubscriptionStatusSuccess];
            } else {
                [QRWSettingsClient saveSubscriptionStatus:QRWSubscriptionStatusTrial];
            }
            
            if ([[QRWSettingsClient getUnlockKey] isEqual:currentPIN]) {
                self.isPresent = NO;
                [self dismissViewControllerAnimated:YES completion:nil];
            } else {
                [[[UIAlertView alloc] initWithTitle:QRWLoc(@"NOT_CORRECT_UNLOCK_KEY_TITLE")
                                            message:QRWLoc(@"NOT_CORRECT_UNLOCK_KEY_MESSAGE")
                                           delegate:nil
                                  cancelButtonTitle:QRWLoc(@"CANCEL")
                                  otherButtonTitles: nil]
                 show];
            }
        }];
    }
}


- (void)setPickerStartPosition
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

//
//  QRWEditItemViewController.m
//  QRealWeb_iOSApp
//
//  Created by Иван Афанасьев on 14.08.13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import "QRWEditItemViewController.h"

@interface QRWEditItemViewController ()

@end

@implementation QRWEditItemViewController


- (void)viewDidLoad
{
    [super viewDidLoad];
    
    UITapGestureRecognizer *tapRecog = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(userTapOnScreen:)];
    [self.view addGestureRecognizer:tapRecog];
    tapRecog.delegate = self;
    
}

- (void)viewWillAppear:(BOOL)animtated
{
    [super viewWillAppear:animtated];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(keyboardWillShow:) name:UIKeyboardDidShowNotification object:nil];
}

- (void)viewWillDisappear:(BOOL)animtated
{
    [[NSNotificationCenter defaultCenter] removeObserver:self];
}


#pragma mark DataManager delegate

- (void)respondsForUploadingRequest:(BOOL)status
{
    NSString *titleString;
    NSString *messageString;
    TLCompletionBlock cencelBlock;
    
    if (status) {
        titleString = NSLocalizedString(@"SUCCESS_TITLE", nil);
        messageString = NSLocalizedString(@"SUCCESS_UPLOAD_MESSAGE", nil);
        cencelBlock = ^{
            [self exitButtonClicked:nil];
        };
        
    } else {
        titleString = NSLocalizedString(@"FAIL_TITLE", nil);
        messageString = NSLocalizedString(@"FAIL_UPLOAD_MESSAGE", nil);
    }
    
    TLAlertView *alert = [[TLAlertView alloc] initWithTitle:titleString message:messageString inView:self.view cancelButtonTitle:NSLocalizedString(@"CANCEL", nil) confirmButton:nil];
    [alert handleCancel:cencelBlock handleConfirm:nil];
    [alert show];
    
    [self stopLoadingAnimation];
}

#pragma mark Actions

- (IBAction)exitButtonClicked:(id)sender
{
    [self dismissViewControllerAnimated:YES completion:nil];
}


#pragma mark - DOT on keyboard methods

- (void)keyboardWillShow:(NSNotification *)notification
{
    UIWindow *tempWindow = [[[UIApplication sharedApplication] windows] objectAtIndex:1];
    UIView *keyboard;
    
    UIButton * utilityButton = [UIButton buttonWithType:UIButtonTypeCustom];
    utilityButton.frame = CGRectMake(0, 163, 105, 53);
    
    [utilityButton.titleLabel setFont:[UIFont systemFontOfSize:35]];
    [utilityButton setTitle:@"." forState:UIControlStateNormal];
    
    [utilityButton setTitleColor:[UIColor colorWithRed:77.0f/255.0f green:84.0f/255.0f blue:98.0f/255.0f alpha:1.0] forState:UIControlStateNormal];
    [utilityButton setTitleColor:[UIColor whiteColor] forState:UIControlStateHighlighted];
    
    [utilityButton setBackgroundImage:[UIImage imageNamed:@"background.png"] forState:UIControlStateHighlighted];
    
    [utilityButton addTarget:self action:@selector(addDecimalPointToField) forControlEvents:UIControlEventTouchUpInside];
    
    [utilityButton setAutoresizingMask:(UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleRightMargin | UIViewAutoresizingFlexibleTopMargin | UIViewAutoresizingFlexibleHeight)];
    
    for(int i = 0; i < [tempWindow.subviews count]; i++){
        keyboard = [tempWindow.subviews objectAtIndex:i];
        
        if ([[[UIDevice currentDevice] systemVersion] floatValue] >= 3.2){
            if([[keyboard description] hasPrefix:@"<UIPeripheralHost"] == YES) {
                [keyboard addSubview:utilityButton];
            }
        } else {
            if([[keyboard description] hasPrefix:@"<UIKeyboard"] == YES) {
                [keyboard addSubview:utilityButton];
            }
        }
    }
}

- (void)addDecimalPointToField
{
    UITextField * objectToEdit = nil;
    
    for (UITextField * localView in self.view.subviews){
        if ([localView isFirstResponder]){
            objectToEdit = localView;
        }
    }
    
    if (objectToEdit != nil) {
        NSString * localText = [(UITextField *)objectToEdit text];
        
        NSRange separatorPosition = [localText rangeOfString:@"."];
        
        if (separatorPosition.location == NSNotFound){
            [objectToEdit insertText:[NSString stringWithFormat:@"."]];
        }
    }
}

@end

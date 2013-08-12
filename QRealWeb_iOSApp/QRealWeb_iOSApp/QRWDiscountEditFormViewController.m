//
//  QRWDiscountEditFormViewController.m
//  QRealWeb_iOSApp
//
//  Created by Иван Афанасьев on 06.08.13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import "QRWDiscountEditFormViewController.h"
#import "QRWDiscount.h"


#define kMemebershipGroup @"MemebershipGroup"
#define kTypeGroup @"TypeGroup"



@interface QRWDiscountEditFormViewController ()
{
    BOOL isEditMode;
}

@property (nonatomic, strong) QRWDiscount *discount;

@end

@implementation QRWDiscountEditFormViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}



- (id)init
{
    self = [self initWithNibName:@"QRWDiscountEditFormViewController" bundle:nil];
    isEditMode = NO;
    return self;
}


- (id)initWithDiscount: (QRWDiscount *) discount
{
    self = [self initWithNibName:@"QRWDiscountEditFormViewController" bundle:nil];
    isEditMode = YES;
    _discount = discount;
    return self;
}


- (void)viewDidLoad
{
    [super viewDidLoad];
    
    UITapGestureRecognizer *tapRecog = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(userTapOnScreen:)];
    [self.view addGestureRecognizer:tapRecog];
    tapRecog.delegate = self;
    
    [self radioButtonDidLoad];
    if (isEditMode) {
        [_minPriceTextView setText:[NSString stringWithFormat:@"%.2f", [_discount.minprice floatValue]]];
        [_discountTextView setText:[NSString stringWithFormat:@"%.2f", [_discount.discount floatValue]]];
    }
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    _premiumMembershipRadioButton = nil;
    _wholesalerMembershipRadioButton = nil;
    _allMembershipRadioButton = nil;
    _absoluteTypeRadioButton = nil;
    _percentTypeRadioButton = nil;
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


#pragma mark view elements methods

- (void) radioButtonDidLoad
{
    RadioButtonControlSelectionBlock selectionBlockForType = ^(VCRadioButton *radioButton){
        if ([radioButton isEqual:_absoluteTypeRadioButton]) {
            _discount.discountType = @"absolute";
        } else {
            _discount.discountType = @"percent";
        }
        radioButton.selected = YES;
    };
    
    RadioButtonControlSelectionBlock selectionBlockForMemebership = ^(VCRadioButton *radioButton){
        if ([radioButton isEqual:_premiumMembershipRadioButton]) {
            _discount.membershipid = [NSNumber numberWithInt:1];
        }
        if ([radioButton isEqual:_wholesalerMembershipRadioButton]) {
            _discount.membershipid = [NSNumber numberWithInt:2];
        }
        if ([radioButton isEqual:_allMembershipRadioButton]) {
            _discount.membershipid = [NSNumber numberWithInt:0];
        }
        radioButton.selected = YES;
    };
    
    _premiumMembershipRadioButton.groupName = kMemebershipGroup;
    _wholesalerMembershipRadioButton.groupName = kMemebershipGroup;
    _allMembershipRadioButton.groupName = kMemebershipGroup;
    _absoluteTypeRadioButton.groupName = kTypeGroup;
    _percentTypeRadioButton.groupName = kTypeGroup;
    
    _premiumMembershipRadioButton.selectedColor = [UIColor blueColor];
    _wholesalerMembershipRadioButton.selectedColor = [UIColor blueColor];
    _allMembershipRadioButton.selectedColor = [UIColor blueColor];
    _absoluteTypeRadioButton.selectedColor = [UIColor blueColor];
    _percentTypeRadioButton.selectedColor = [UIColor blueColor];
    
    _premiumMembershipRadioButton.controlColor = [UIColor orangeColor];
    _wholesalerMembershipRadioButton.controlColor = [UIColor orangeColor];
    _allMembershipRadioButton.controlColor = [UIColor orangeColor];
    _absoluteTypeRadioButton.controlColor = [UIColor orangeColor];
    _percentTypeRadioButton.controlColor = [UIColor orangeColor];
    
    _premiumMembershipRadioButton.selectionBlock = selectionBlockForMemebership;
    _wholesalerMembershipRadioButton.selectionBlock = selectionBlockForMemebership;
    _allMembershipRadioButton.selectionBlock = selectionBlockForMemebership;
    _absoluteTypeRadioButton.selectionBlock = selectionBlockForType;
    _percentTypeRadioButton.selectionBlock = selectionBlockForType;
    
    if (isEditMode) {
        switch ([_discount.membershipid intValue]) {
            case 0:
                _allMembershipRadioButton.selected = YES;
                break;
            case 1:
                _premiumMembershipRadioButton.selected = YES;
                break;
            case 2:
                _wholesalerMembershipRadioButton.selected = YES;
                break;
        }
        if ([@"absolute" isEqualToString:_discount.discountType]) {
            _absoluteTypeRadioButton.selected = YES;
        } else {
            _percentTypeRadioButton.selected = YES;
        }
    } else {
        _premiumMembershipRadioButton.selected = YES;
        _absoluteTypeRadioButton.selected = YES;
    }
}


#pragma mark Actions

- (IBAction)exitButtonClicked:(id)sender
{
    [self dismissViewControllerAnimated:YES completion:nil];
}

- (IBAction)uploadButtonClicked:(id)sender
{
    if (isEditMode) {
        [dataManager uploadEditedDiscountWithDiscount:_discount];
    } else {
        [dataManager uploadNewDiscountWithDiscount:_discount];
    }
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
}

#pragma mark - GestureRecognizer delegate

-(void)userTapOnScreen:(UIGestureRecognizer *)sender
{
    [_minPriceTextView resignFirstResponder];
    [_discountTextView resignFirstResponder];
}


-(BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer shouldReceiveTouch:(UITouch *)touch
{
    if ((touch.view.class == [VCRadioButton class]) || (touch.view == _exitButton) || (touch.view == _uploadButton))  {
        return NO;
    }
    return YES;
}


#pragma mark - Text Field

-(BOOL)textFieldShouldReturn:(UITextField *)textField
{
    [textField resignFirstResponder];
    return YES;
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

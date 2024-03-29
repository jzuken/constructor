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
    self = [self initWithNibName:@"QRWDiscountEditFormViewController" oldNibName:@"QRWDiscountEditFormViewControllerOld"];
    isEditMode = NO;
    
    _discount = [[QRWDiscount alloc] init];
    _discount.discount = [NSNumber numberWithInt:1];
    _discount.discountType = @"absolute";
    
    return self;
}


- (id)initWithDiscount: (QRWDiscount *) discount
{
    self = [self initWithNibName:@"QRWDiscountEditFormViewController" oldNibName:@"QRWDiscountEditFormViewControllerOld"];
    isEditMode = YES;
    _discount = discount;
    return self;
}


- (void)viewDidLoad
{
    [super viewDidLoad];
    
    [self radioButtonDidLoad];
    if (isEditMode) {
        [_minPriceTextView setText:[NSString stringWithFormat:@"%.2f", [_discount.minprice floatValue]]];
        [_discountTextView setText:[NSString stringWithFormat:@"%.2f", [_discount.discount floatValue]]];
    }
    
    _forOpenKeyboardScrollView.contentSize = _forOpenKeyboardScrollView.frame.size;
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
    
    _premiumMembershipRadioButton.selectedColor = [UIColor colorWithRed:28.0/256 green:103.0/256 blue:152.0/256 alpha:1];
    _wholesalerMembershipRadioButton.selectedColor = [UIColor colorWithRed:28.0/256 green:103.0/256 blue:152.0/256 alpha:1];
    _allMembershipRadioButton.selectedColor = [UIColor colorWithRed:28.0/256 green:103.0/256 blue:152.0/256 alpha:1];
    _absoluteTypeRadioButton.selectedColor = [UIColor colorWithRed:28.0/256 green:103.0/256 blue:152.0/256 alpha:1];
    _percentTypeRadioButton.selectedColor = [UIColor colorWithRed:28.0/256 green:103.0/256 blue:152.0/256 alpha:1];
    
    _premiumMembershipRadioButton.controlColor = [UIColor colorWithRed:153.0/256 green:159.0/256 blue:163.0/256 alpha:1];
    _wholesalerMembershipRadioButton.controlColor = [UIColor colorWithRed:153.0/256 green:159.0/256 blue:163.0/256 alpha:1];
    _allMembershipRadioButton.controlColor = [UIColor colorWithRed:153.0/256 green:159.0/256 blue:163.0/256 alpha:1];
    _absoluteTypeRadioButton.controlColor = [UIColor colorWithRed:153.0/256 green:159.0/256 blue:163.0/256 alpha:1];
    _percentTypeRadioButton.controlColor = [UIColor colorWithRed:153.0/256 green:159.0/256 blue:163.0/256 alpha:1];
    
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

- (IBAction)uploadButtonClicked:(id)sender
{
    _discount.minprice = [NSNumber numberWithDouble:[_minPriceTextView.text doubleValue]];
    _discount.discount = [NSNumber numberWithDouble:[_discountTextView.text doubleValue]];
    
    if ([self isDiscountValid]) {
        if (isEditMode) {
            [dataManager uploadEditedDiscountWithDiscount:_discount];
        } else {
            [dataManager uploadNewDiscountWithDiscount:_discount];
        }
        [self startLoadingAnimation];
    }
}

- (BOOL) isDiscountValid
{
    BOOL answer = YES;

    if ([_discount.discountType isEqualToString: @"percent"]) {
        if ([_discount.discount floatValue] >= 100.0) {
            answer = NO;
            TLAlertView *alert = [[TLAlertView alloc] initWithTitle:NSLocalizedString(@"ERROR", nil) message:NSLocalizedString(@"BIG_PERCENTS_DISCOUNT", nil) inView:self.view cancelButtonTitle:NSLocalizedString(@"OK", nil) confirmButton:nil];
            [alert show];
            return answer;
        }
    } else {
        if ([_discount.minprice floatValue] <= [_discount.discount floatValue]) {
            answer = NO;
            TLAlertView *alert = [[TLAlertView alloc] initWithTitle:NSLocalizedString(@"ERROR", nil) message:NSLocalizedString(@"BIG_DISCOUNT", nil) inView:self.view cancelButtonTitle:NSLocalizedString(@"OK", nil) confirmButton:nil];
            [alert show];
            return answer;
        }
    }
    
    if ([_discount.minprice floatValue] == 0.0 || [_discount.discount floatValue] == 0.0) {
        answer = NO;
        TLAlertView *alert = [[TLAlertView alloc] initWithTitle:NSLocalizedString(@"ERROR", nil) message:NSLocalizedString(@"ZERO_FIELDS", nil) inView:self.view cancelButtonTitle:NSLocalizedString(@"OK", nil) confirmButton:nil];
        [alert show];
    }
    return answer;
}



#pragma mark - GestureRecognizer delegate

-(void)userTapOnScreen:(UIGestureRecognizer *)sender
{
    [_minPriceTextView resignFirstResponder];
    [_discountTextView resignFirstResponder];
    [UIView animateWithDuration:0.3 animations:^{
        CGRect frame = _forOpenKeyboardScrollView.frame;
        frame.size = _forOpenKeyboardScrollView.contentSize;
        _forOpenKeyboardScrollView.frame = frame;
    }];
}


-(BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer shouldReceiveTouch:(UITouch *)touch
{
    if ((touch.view.class == [VCRadioButton class]) || (touch.view == self.exitButton) || (touch.view == self.uploadButton))  {
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

- (void)textFieldDidBeginEditing:(UITextField *)textField
{
    [UIView animateWithDuration:0.3 animations:^{
        CGRect frame = _forOpenKeyboardScrollView.frame;
        frame.size.height -= 140;
        _forOpenKeyboardScrollView.frame = frame;
    }];
}


@end

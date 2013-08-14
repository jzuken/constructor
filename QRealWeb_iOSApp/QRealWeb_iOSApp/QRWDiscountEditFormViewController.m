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
    
    _premiumMembershipRadioButton.controlColor = [UIColor grayColor];
    _wholesalerMembershipRadioButton.controlColor = [UIColor grayColor];
    _allMembershipRadioButton.controlColor = [UIColor grayColor];
    _absoluteTypeRadioButton.controlColor = [UIColor grayColor];
    _percentTypeRadioButton.controlColor = [UIColor grayColor];
    
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
    NSNumberFormatter * formatter = [[NSNumberFormatter alloc] init];
    [formatter setNumberStyle:NSNumberFormatterDecimalStyle];
    
    _discount.minprice = [NSNumber numberWithFloat:[[formatter numberFromString:_minPriceTextView.text] floatValue]];
    _discount.discount = [NSNumber numberWithFloat:[[formatter numberFromString:_discountTextView.text] floatValue]];
    
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
        }
    } else {
        if ([_discount.minprice floatValue] <= [_discount.discount floatValue]) {
            answer = NO;
            TLAlertView *alert = [[TLAlertView alloc] initWithTitle:NSLocalizedString(@"ERROR", nil) message:NSLocalizedString(@"BIG_DISCOUNT", nil) inView:self.view cancelButtonTitle:NSLocalizedString(@"OK", nil) confirmButton:nil];
            [alert show];
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


@end

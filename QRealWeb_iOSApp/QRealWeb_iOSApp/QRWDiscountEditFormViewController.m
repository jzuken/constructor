//
//  QRWDiscountEditFormViewController.m
//  QRealWeb_iOSApp
//
//  Created by Иван Афанасьев on 06.08.13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import "QRWDiscountEditFormViewController.h"

@interface QRWDiscountEditFormViewController ()

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
    return [self initWithNibName:@"QRWDiscountEditFormViewController" bundle:nil];
}


- (void)viewDidLoad
{
    [super viewDidLoad];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}


- (void)exitButtonClicked:(id)sender
{
    [self dismissViewControllerAnimated:YES completion:nil];
}

@end

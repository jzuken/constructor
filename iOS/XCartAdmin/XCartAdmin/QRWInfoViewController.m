//
//  QRWInfoViewController.m
//  XCartAdmin
//
//  Created by Иван Афанасьев on 16.09.14.
//  Copyright (c) 2014 Ivan Afanasiev. All rights reserved.
//

#import "QRWInfoViewController.h"

@interface QRWInfoViewController ()

@end

@implementation QRWInfoViewController

- (id)init
{
    self = [[UIStoryboard storyboardWithName:@"MainStoryboard" bundle:nil] instantiateViewControllerWithIdentifier:@"QRWInfoViewController"];
    return self;
}


- (void)viewDidLoad
{
    [super viewDidLoad];
    [self.navigationController setNavigationBarHidden:NO animated:YES];
    [self setNavigationBarColor:kBlueColor title:QRWLoc(@"INFORMATION")];
}

- (void)openURLWithString:(NSString *)string
{
    [[UIApplication sharedApplication] openURL:[NSURL URLWithString:string]];
}


- (IBAction)xCartAdminTapped:(id)sender
{
    [self openURLWithString:@"http://www.x-cart.com/extensions/modules/mobileadmin.html"];
}

- (IBAction)helpTapped:(id)sender
{
    [self openURLWithString:@"http://www.x-cart.com/help.html"];
}

- (IBAction)callTapped:(id)sender
{
    [self openURLWithString:[NSString stringWithFormat:@"tel:%@", [(UIButton *)sender titleLabel].text]];
}

- (IBAction)faceBookTapped:(id)sender
{
    [self openURLWithString:@"https://www.facebook.com/xcart"];
}

- (IBAction)twitterTapped:(id)sender
{
    [self openURLWithString:@"https://twitter.com/xcart"];
}


@end

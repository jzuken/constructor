//
//  QWDFiltersViewControllerViewController.m
//  QRealWebDemoProject
//
//  Created by Ivan Afanasiev on 7/8/13.
//  Copyright (c) 2013 Иван Афанасьев. All rights reserved.
//

#import "QWDFiltersViewControllerViewController.h"

@interface QWDFiltersViewControllerViewController ()

@end

@implementation QWDFiltersViewControllerViewController

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
    return [self initWithNibName:@"QWDFiltersViewControllerViewController" bundle:nil];    
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    self.navigationController.navigationBarHidden = NO;
    self.navigationController.navigationBar.barStyle = UIBarStyleBlackTranslucent;
    self.navigationItem.title = @"FILTERS";
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end

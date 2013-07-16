//
//  DLSDetailsOfDocumentViewController.m
//  FirstLineSoftwareTestProject
//
//  Created by Иван Афанасьев on 06.04.13.
//  Copyright (c) 2013 Иван Афанасьев. All rights reserved.
//

#import "QWDProductsViewController.h"
//#import "QWDFiltersViewControllerViewController.h"


@interface QWDProductsViewController ()

@end

@implementation QWDProductsViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
    }
    return self;
}

- (id)init
{
    return  (([[UIDevice currentDevice] resolution] == UIDeviceResolution_iPhoneRetina5))?
                [self initWithNibName:@"QWDProductsViewController4" bundle:nil] : [self initWithNibName:@"QWDProductsViewController3.5" bundle:nil];
}


- (void)viewDidLoad
{
    [super viewDidLoad];
    self.navigationController.navigationBarHidden = NO;
    self.navigationController.navigationBar.barStyle = UIBarStyleBlackTranslucent;
    
    self.navigationItem.title = @"TOOL";
    
    UIBarButtonItem *filterButton = [[UIBarButtonItem alloc] initWithTitle:@"Filters" style:UIBarButtonItemStylePlain target:self action:@selector(openFiltersView)];
    [self.navigationItem setRightBarButtonItem:filterButton animated:YES];
    
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}


- (void) openFiltersView
{
//    QWDFiltersViewControllerViewController *filtersViewController = [[QWDFiltersViewControllerViewController alloc] init];
//    [[self navigationController] pushViewController:filtersViewController animated:YES];
}

@end

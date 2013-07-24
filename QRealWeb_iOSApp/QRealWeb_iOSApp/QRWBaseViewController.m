//
//  FLSBaseViewController.m
//  FirstLineSoftwareTestProject
//
//  Created by Иван Афанасьев on 06.04.13.
//  Copyright (c) 2013 Иван Афанасьев. All rights reserved.
//

#import "QRWBaseViewController.h"

@interface QRWBaseViewController ()

@end

@implementation QRWBaseViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        dataManager = [QRWDataManager instance];
    }
    return self;
}


- (void)viewDidLoad
{
    [super viewDidLoad];
    [self regestraiteDataManagerDelegate];
}

-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [self regestraiteDataManagerDelegate];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}


- (void)regestraiteDataManagerDelegate
{
    dataManager.delegate = self;
}
@end

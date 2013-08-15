//
//  FLSListScrinViewController.m
//  FirstLineSoftwareTestProject
//
//  Created by Иван Афанасьев on 05.04.13.
//  Copyright (c) 2013 Иван Афанасьев. All rights reserved.
//

#import "QRWToolsScrinViewController.h"
#import "QRWProductsViewController.h"
#import "constants.h"

@interface QRWToolsScrinViewController ()

@property (nonatomic, strong) NSArray *tools;

- (void) addToolsView;

@end

@implementation QRWToolsScrinViewController

- (id)init
{
    return [self initWithNibName:@"QRWToolsScrinViewController" oldNibName:@"QRWToolsScrinViewControllerOld"]; 
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    [dataManager sendToolsRequest];
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    self.navigationController.navigationBarHidden = YES;
}

- (void) addToolsView
{
    for (int index = 0; index < _tools.count; index++) {
        UIView *toolView = [_tools objectAtIndex:index];
        
        int sideOfToolCell = self.view.frame.size.width/2;
        
        if (index%2 != 1) {
            CGRect toolViewFrame = CGRectMake((sideOfToolCell - kSideOfToolView)/2,
                                              index * sideOfToolCell /2  + (sideOfToolCell - kSideOfToolView)/2,
                                              toolView.frame.size.width,
                                              toolView.frame.size.height);
            toolView.frame = toolViewFrame;
        } else {
            CGRect toolViewFrame = CGRectMake( sideOfToolCell + (sideOfToolCell - kSideOfToolView)/2,
                                              (index - 1) * sideOfToolCell /2 + (sideOfToolCell - kSideOfToolView)/2,
                                              toolView.frame.size.width,
                                              toolView.frame.size.height);
            toolView.frame = toolViewFrame;
        }
        [self.toolsScrollView addSubview:toolView];
    }
}

#pragma mark - dataManager delegate

- (void)respondsForToolsRequest:(NSArray *)tools
{
    _tools = [NSArray arrayWithArray:tools];
    
    _toolsScrollView.contentSize = CGSizeMake(self.view.frame.size.width, self.view.frame.size.width * _tools.count / 4);
    [self addToolsView];
}


@end

//
//  QRWProductsLastorderDashboardViewController.h
//  QRealWeb_iOSApp
//
//  Created by Ivan Afanasiev on 8/1/13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import "QRWBaseViewController.h"

@interface QRWProductsViewControllerForModalPresent : QRWBaseViewController <UITableViewDataSource, UITableViewDelegate>


@property (nonatomic, strong) IBOutlet UITableView *productsTableView;
@property (nonatomic, strong) IBOutlet UIButton *exitButton;
@property (nonatomic, strong) NSArray *productsArray;


@property BOOL mainStatsMode;

- (IBAction)exitButtonClicked:(id)sender;



- (id)initWithProducts: (NSArray *) products;




@end

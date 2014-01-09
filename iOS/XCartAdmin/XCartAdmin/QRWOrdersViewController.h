//
//  QRWOrdersViewController.h
//  XCartAdmin
//
//  Created by Ivan Afanasiev on 09/01/14.
//  Copyright (c) 2014 Ivan Afanasiev. All rights reserved.
//

#import "QRWBaseViewControllerWithTableView.h"

@interface QRWOrdersViewController : QRWBaseViewControllerWithTableView


@property (strong, nonatomic) IBOutlet UISegmentedControl *ordersTypeSegmentedControl;

@end

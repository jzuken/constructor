//
//  QRWProductsViewController.h
//  XCartAdmin
//
//  Created by Ivan Afanasiev on 09/01/14.
//  Copyright (c) 2014 Ivan Afanasiev. All rights reserved.
//

#import "QRWBaseViewControllerWithTableView.h"

@interface QRWProductsViewController : QRWBaseViewControllerWithTableView


@property (strong, nonatomic) IBOutlet UISegmentedControl *productsTypeSegmentedControl;



- (id)initAsLowStock: (BOOL)isLowStock;

- (void)setIsLowStock: (BOOL)isLowStock;

@end

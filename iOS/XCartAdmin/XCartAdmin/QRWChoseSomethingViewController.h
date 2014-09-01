//
//  QRWChoseSomethingViewController.h
//  XCartAdmin
//
//  Created by Ivan Afanasyev on 01.09.14.
//  Copyright (c) 2014 Ivan Afanasiev. All rights reserved.
//

#import "QRWBaseViewControllerWithTableView.h"

@interface QRWChoseSomethingViewController : QRWBaseViewControllerWithTableView

- (id)initWithOptionsDictionary:(NSArray *)options
                  selectedIndex:(NSUInteger)selectedIndex
              selectOptionBlock:(void(^)(NSString *selectedOption))selectOptionBlock;

@end

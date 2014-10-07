//
//  QRWChoseSomethingViewController.h
//  XCartAdmin
//
//  Created by Ivan Afanasyev on 01.09.14.
//  Copyright (c) 2014 Ivan Afanasiev. All rights reserved.
//

#import "QRWBaseViewControllerWithTableView.h"

typedef NS_ENUM(NSUInteger, QRWChoseSomethingViewControllerType) {
    QRWChoseSomethingViewControllerTypeStrings,
    QRWChoseSomethingViewControllerTypeOptions
};

@interface QRWChoseSomethingViewController : QRWBaseViewControllerWithTableView

@property (nonatomic, assign) NSUInteger selectedIndex;

- (id)initWithOptionsDictionary:(NSArray *)options
                  selectedIndex:(NSUInteger)selectedIndex
                           type:(QRWChoseSomethingViewControllerType)type
              selectOptionBlock:(void(^)(NSString *selectedOption))selectOptionBlock;

@end

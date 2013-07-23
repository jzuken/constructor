//
//  QRWCustomSegmentedControl.h
//  QRealWeb_iOSApp
//
//  Created by Иван Афанасьев on 23.07.13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import <UIKit/UIKit.h>


@class QRWCustomSegmentedControl;

@protocol QRWCustomSegmentedControlDelegate

- (UIButton *) buttonFor:(QRWCustomSegmentedControl*)segmentedControl atIndex:(NSUInteger)segmentIndex;

@optional

- (void) touchUpInsideSegmentIndex:(NSUInteger)segmentIndex;
- (void) touchDownAtSegmentIndex:(NSUInteger)segmentIndex;

@end

@interface QRWCustomSegmentedControl : UIView
{
    NSObject <QRWCustomSegmentedControlDelegate> *delegate;
    NSMutableArray* buttons;
}

@property (nonatomic, strong) NSMutableArray* buttons;

- (id) initWithSegmentCount:(NSUInteger)segmentCount segmentsize:(CGSize)segmentsize dividerImage:(UIImage*)dividerImage tag:(NSInteger)objectTag delegate:(NSObject <QRWCustomSegmentedControlDelegate>*)customSegmentedControlDelegate;

@end


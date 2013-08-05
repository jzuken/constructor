//
//  QRWOrdersStatisticCell.h
//  QRealWeb_iOSApp
//
//  Created by Ivan Afanasiev on 8/1/13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface QRWOrdersStatisticCell : UITableViewCell


@property (nonatomic, strong) IBOutlet UILabel *nameLabel;
@property (nonatomic, strong) IBOutlet UILabel *lastLoginLabel;
@property (nonatomic, strong) IBOutlet UILabel *todayLabel;
@property (nonatomic, strong) IBOutlet UILabel *weekLabel;
@property (nonatomic, strong) IBOutlet UILabel *monthLabel;

@end

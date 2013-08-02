//
//  QRWUserCell.h
//  QRealWeb_iOSApp
//
//  Created by Ivan Afanasiev on 8/2/13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface QRWUserCell : UITableViewCell

@property (strong, nonatomic) IBOutlet UIImageView *userPhotoImageView;

@property (strong, nonatomic) IBOutlet UILabel *userLable;
@property (strong, nonatomic) IBOutlet UILabel *loginLable;
@property (strong, nonatomic) IBOutlet UILabel *typeLable;
@property (strong, nonatomic) IBOutlet UILabel *firstLoginLable;
@property (strong, nonatomic) IBOutlet UILabel *ordersCountLable;


@end

//
//  QRWUserOrderCell.h
//  QRealWeb_iOSApp
//
//  Created by Ivan Afanasiev on 8/14/13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface QRWUserOrderCell : UITableViewCell

@property (strong, nonatomic) IBOutlet UILabel *statusLable;
@property (strong, nonatomic) IBOutlet UILabel *dateLable;
@property (strong, nonatomic) IBOutlet UILabel *totalLable;
@property (strong, nonatomic) IBOutlet UILabel *productsLable;


@end

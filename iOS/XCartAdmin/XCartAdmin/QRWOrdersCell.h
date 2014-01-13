//
//  QRWOrdersCell.h
//  XCartAdmin
//
//  Created by Иван Афанасьев on 11.01.14.
//  Copyright (c) 2014 Ivan Afanasiev. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface QRWOrdersCell : UITableViewCell


@property (nonatomic, strong) IBOutlet UILabel *dateLabel;
@property (nonatomic, strong) IBOutlet UILabel *nameLabel;
@property (nonatomic, strong) IBOutlet UILabel *priceLabel;
@property (nonatomic, strong) IBOutlet UILabel *statusLabel;


@end

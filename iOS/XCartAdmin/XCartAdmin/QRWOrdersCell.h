//
//  QRWOrdersCell.h
//  XCartAdmin
//
//  Created by Иван Афанасьев on 11.01.14.
//  Copyright (c) 2014 Ivan Afanasiev. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface QRWOrdersCell : UITableViewCell


@property (nonatomic, weak) IBOutlet UILabel *dateLabel;
@property (nonatomic, weak) IBOutlet UILabel *nameLabel;
@property (nonatomic, weak) IBOutlet UILabel *priceLabel;
@property (nonatomic, weak) IBOutlet UILabel *statusLabel;


@end

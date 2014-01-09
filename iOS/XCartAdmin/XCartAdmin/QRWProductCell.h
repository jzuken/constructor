//
//  QRWProductCell.h
//  XCartAdmin
//
//  Created by Ivan Afanasiev on 09/01/14.
//  Copyright (c) 2014 Ivan Afanasiev. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface QRWProductCell : UITableViewCell

@property (nonatomic, strong) IBOutlet UILabel *SKULabel;
@property (nonatomic, strong) IBOutlet UILabel *inStockTypeLabel;
@property (nonatomic, strong) IBOutlet UILabel *priceLabel;

@end

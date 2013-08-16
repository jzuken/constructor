//
//  QRWProductCell.h
//  QRealWeb_iOSApp
//
//  Created by Ivan Afanasiev on 8/13/13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface QRWProductCell : UITableViewCell


@property (strong, nonatomic) IBOutlet UILabel *productLable;
@property (strong, nonatomic) IBOutlet UILabel *priceLable;
@property (strong, nonatomic) IBOutlet UILabel *avaliableLable;
@property (strong, nonatomic) IBOutlet UILabel *minAmountLable;
@property (strong, nonatomic) IBOutlet UILabel *freeShipingLable;
@property (strong, nonatomic) IBOutlet UILabel *codeLable;


@end

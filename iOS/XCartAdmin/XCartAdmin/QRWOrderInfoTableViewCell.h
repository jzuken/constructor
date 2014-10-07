//
//  QRWOrderInfoTableViewCell.h
//  XCartAdmin
//
//  Created by Иван Афанасьев on 22.05.14.
//  Copyright (c) 2014 Ivan Afanasiev. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "QRWOrderInfo.h"

@interface QRWOrderInfoTableViewCell : UITableViewCell

//items
@property(strong, nonatomic) IBOutlet UILabel *itemNameLabel;
@property(strong, nonatomic) IBOutlet UILabel *itemAmountLabel;
@property(strong, nonatomic) IBOutlet UILabel *itemCostLabel;
@property(strong, nonatomic) IBOutlet UILabel *itemOptionsLabel;


//fixed fields
@property(strong, nonatomic) IBOutlet UILabel *keyLabel;
@property(strong, nonatomic) IBOutlet UILabel *valueLabel;

//info
@property(strong, nonatomic) IBOutlet UILabel *typeInfoKeyLabel;
@property(strong, nonatomic) IBOutlet UILabel *typeInfoValueLabel;
@property(strong, nonatomic) IBOutlet UILabel *phoneLabel;

//total
@property(strong, nonatomic) IBOutlet UILabel *totalLabel;


-(void)configurateAsItemCell:(QRWOrderInfoItem *)item;

-(void)configurateAsCellWithKey:(NSString *)key value:(NSString *)value;

-(void)configurateAsInfoCellWithKey:(NSString *)key value:(NSString *)value phone:(NSString *)phone;

-(void)configurateAsTotalCellWithValue:(NSString *)value;

@end

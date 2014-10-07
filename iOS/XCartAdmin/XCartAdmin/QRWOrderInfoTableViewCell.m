//
//  QRWOrderInfoTableViewCell.m
//  XCartAdmin
//
//  Created by Иван Афанасьев on 22.05.14.
//  Copyright (c) 2014 Ivan Afanasiev. All rights reserved.
//

#import "QRWOrderInfoTableViewCell.h"
#import "constants.h"

@implementation QRWOrderInfoTableViewCell




-(void)configurateAsItemCell:(QRWOrderInfoItem *)item
{
    self.itemCostLabel.text = NSMoneyString(@"$",item.price);
    self.itemNameLabel.text = item.product;
    self.itemAmountLabel.text = item.amount;
    self.itemOptionsLabel.text = item.optionsString.length ? item.optionsString : QRWLoc(@"NO_OPTIONS");
}

-(void)configurateAsCellWithKey:(NSString *)key value:(NSString *)value
{
    self.keyLabel.text = key;
    self.valueLabel.text = value;
}

-(void)configurateAsInfoCellWithKey:(NSString *)key value:(NSString *)value phone:(NSString *)phone
{
    self.typeInfoKeyLabel.text = key;
    self.typeInfoValueLabel.text = value;
    self.phoneLabel.text = phone;
}

-(void)configurateAsTotalCellWithValue:(NSString *)value
{
    self.totalLabel.text = value;
}


@end


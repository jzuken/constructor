//
//  QRWOrderInfo.h
//  XCartAdmin
//
//  Created by Ivan Afanasyev on 22/05/14.
//  Copyright (c) 2014 Ivan Afanasiev. All rights reserved.
//

#import "QRWBaseEntety.h"

@interface QRWOrderInfo : QRWBaseEntety

@property(nonatomic, strong) NSNumber *orderid;

@property(nonatomic, copy) NSArray *items;

@property(nonatomic, copy) NSString *status;
@property(nonatomic, copy) NSString *tracking;
@property(nonatomic, copy) NSString *paymentMethod;
@property(nonatomic, copy) NSString *shipping;
@property(nonatomic, copy) NSString *title;
@property(nonatomic, copy) NSString *customer;
@property(nonatomic, copy) NSString *userid;
@property(nonatomic, strong) NSNumber *date;

@property(nonatomic, copy) NSString *bTitle;
@property(nonatomic, copy) NSString *billingInfo;
@property(nonatomic, copy) NSString *bPhone;

@property(nonatomic, copy) NSString *sTitle;
@property(nonatomic, copy) NSString *shippingingInfo;
@property(nonatomic, copy) NSString *sPhone;

@property(nonatomic, copy) NSString *subtotal;
@property(nonatomic, copy) NSString *discount;
@property(nonatomic, copy) NSString *couponDiscount;
@property(nonatomic, copy) NSString *shippingCost;
@property(nonatomic, copy) NSString *paymentSurcharge;
@property(nonatomic, copy) NSString *total;

@property(nonatomic, copy) NSString *notes;

@property(nonatomic, copy) NSString *pphURLString;

@end


@interface QRWOrderInfoItem : QRWBaseEntety

@property(nonatomic, copy) NSString *productid;
@property(nonatomic, copy) NSString *product;
@property(nonatomic, copy) NSString *price;
@property(nonatomic, copy) NSString *amount;

@end

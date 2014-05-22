//
//  QRWOrderInfo.m
//  XCartAdmin
//
//  Created by Ivan Afanasyev on 22/05/14.
//  Copyright (c) 2014 Ivan Afanasiev. All rights reserved.
//

#import "QRWOrderInfo.h"

@implementation QRWOrderInfo

- (void)buildDataByJson:(NSDictionary *)JSON
{
    [super buildDataByJson:JSON];
    
    self.status = [JSON objectForKey:@"status"];
    self.tracking = [JSON objectForKey:@"tracking"];
    self.paymentMethod  = [JSON objectForKey:@"payment_method"];
    self.shipping = [JSON objectForKey:@"shipping"];
    self.title = [JSON objectForKey:@"title"];
    self.firstname  = [JSON objectForKey:@"firstname"];
    self.lastname = [JSON objectForKey:@"lastname"];
    self.userid = [JSON objectForKey:@"userid"];
    
    
    self.bTitle  = [JSON objectForKey:@"b_title"];
    self.billingInfo = [NSString stringWithFormat:@"%@ %@\n %@ %@ , %@ %@\n %@ ", JSON[@"b_firstname"], JSON[@"b_lastname"], JSON[@"b_address"], JSON[@"b_city"], JSON[@"b_state"] , JSON[@"b_zipcode"], JSON[@"b_country"]];
    self.bPhone = [JSON objectForKey:@"b_phone"];
    
    self.sTitle  = [JSON objectForKey:@"s_title"];
    self.shippingingInfo =[NSString stringWithFormat:@"%@ %@\n %@ %@ , %@ %@\n %@ ", JSON[@"s_firstname"], JSON[@"s_lastname"], JSON[@"s_address"], JSON[@"s_city"], JSON[@"s_state"] , JSON[@"s_zipcode"], JSON[@"s_country"]];
    self.sPhone = [JSON objectForKey:@"s_phone"];
    
    
    self.subtotal = [JSON objectForKey:@"subtotal"];
    self.discount = [JSON objectForKey:@"discount"];
    self.couponDiscount  = [JSON objectForKey:@"coupon_discount"];
    self.shippingCost = [JSON objectForKey:@"shipping_cost"];
    self.total = [JSON objectForKey:@"total"];
    
    NSArray *items = [JSON objectForKey:@"details"];
    NSMutableArray *itemsSet = [NSMutableArray new];
    
    for (NSDictionary *itemDict in items) {
        QRWOrderInfoItem *item = [QRWOrderInfoItem new];
        [item buildDataByJson:itemDict];
        [itemsSet addObject:item];
    }
    
    self.items = itemsSet;
}



@end


@implementation QRWOrderInfoItem

- (void)buildDataByJson:(NSDictionary *)JSON
{
    [super buildDataByJson:JSON];
    
    self.price = [JSON objectForKey:@"price"];
    self.productid = [JSON objectForKey:@"productid"];
    self.product  = [JSON objectForKey:@"product"];
}

@end



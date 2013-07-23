//
//  QRWLastOrder.h
//  QRealWeb_iOSApp
//
//  Created by Ivan Afanasiev on 7/23/13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface QRWLastOrder : NSObject

@property (nonatomic, strong) NSNumber *orderid;
@property (nonatomic, strong) NSNumber *userid;
@property (nonatomic, strong) NSNumber *total;
@property (nonatomic, strong) NSNumber *giftcert_discount;
@property (nonatomic, strong) NSNumber *giftcert_ids;
@property (nonatomic, strong) NSNumber *subtotal;
@property (nonatomic, strong) NSNumber *discount;
@property (nonatomic, strong) NSString *coupon;
@property (nonatomic, strong) NSNumber *coupon_discount;
@property (nonatomic, strong) NSNumber *shippingid;

@property (nonatomic, strong) NSString *shipping;
@property (nonatomic, strong) NSString *tracking;
@property (nonatomic, strong) NSNumber *shipping_cost;
@property (nonatomic, strong) NSNumber *tax;
@property (nonatomic, strong) NSString *taxes_applied;
@property (nonatomic, strong) NSString *date;

@property (nonatomic, strong) NSString *firstname;
@property (nonatomic, strong) NSString *lastname;
@property (nonatomic, strong) NSString *email;


//"orderid":"1","userid":"2","membership":"","total":"55.98","giftcert_discount":"0.00","giftcert_ids":"","subtotal":"35.98","discount":"0.00","coupon":"","coupon_discount":"0.00","shippingid":"1011","shipping":"Example national delivery method1","tracking":"","shipping_cost":"10.00","tax":"0.00","taxes_applied":"a:0:{}","date":"1373309588","status":"Q","payment_method":"Phone Ordering","flag":"N","notes":"","details":"","customer_notes":"","customer":"","title":"","firstname":"Nikita","lastname":"Bumakov","company":"","url":"","email":"elengor91@gmail.com","language":"en","clickid":"0","membershipid":"0","paymentid":"4","payment_surcharge":"10.00","tax_number":"","tax_exempt":"N","init_total":"55.98","access_key":"","klarna_order_status":""
@end

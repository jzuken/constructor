//
//  QRWProductWithInfo.m
//  XCartAdmin
//
//  Created by Ivan Afanasiev on 10/01/14.
//  Copyright (c) 2014 Ivan Afanasiev. All rights reserved.
//

#import "QRWProductWithInfo.h"
#import "QRWSettingsClient.h"

@implementation QRWProductWithInfo


- (void)buildDataByJson:(NSDictionary *)JSON
{
    [super buildDataByJson:JSON];
    
    self.productDescription = [JSON objectForKey:@"descr"];
    self.fullDescription = [JSON objectForKey:@"fulldescr"];
    self.imageURL = [JSON objectForKey:@"image_url"];
    self.productid = [JSON objectForKey:@"productid"];//[NSNumber numberWithLong:(long )[JSON objectForKey:@"productid"]];
    self.available = [JSON objectForKey:@"avail"];
    self.forSale = [JSON objectForKey:@"forsale"];
    
    NSMutableArray *itemsSet = [NSMutableArray new];
    NSArray *items = [JSON objectForKey:@"variants"];
    if (![items isKindOfClass:[NSNull class]]) {
        [items enumerateObjectsUsingBlock:^(NSDictionary *itemDict, NSUInteger idx, BOOL *stop) {
            QRWProductVariant *item = [QRWProductVariant new];
            [item buildDataByJson:itemDict];
            [itemsSet addObject:item];
        }];
    }
    
    self.variants = itemsSet;
}

@end



@implementation QRWProductVariant

- (void)buildDataByJson:(NSDictionary *)JSON
{
    [super buildDataByJson:JSON];
    
    self.SKUOfVariant = [JSON objectForKey:@"productcode"];
    self.variantid = [[JSON objectForKey:@"variant_id"] isKindOfClass:[NSString class]] ?
        [JSON objectForKey:@"variant_id"]:
        [[JSON objectForKey:@"variant_id"] stringValue];
    self.price = [JSON objectForKey:@"price"];
    self.imageURL = [JSON objectForKey:@"image_path_W"];

    NSArray *items = [JSON objectForKey:@"options_arr"];
    NSMutableArray *itemsSet = [NSMutableArray new];
    for (NSDictionary *itemDict in items) {
        QRWProductVariantOption *item = [QRWProductVariantOption new];
        [item buildDataByJson:itemDict];
        [itemsSet addObject:item];
    }
    self.options = itemsSet;
}

@end



@implementation QRWProductVariantOption

- (void)buildDataByJson:(NSDictionary *)JSON
{
    [super buildDataByJson:JSON];
    
    self.optionName = [JSON objectForKey:@"classtext"];
    self.optionValue = [JSON objectForKey:@"option_name"];
}

@end

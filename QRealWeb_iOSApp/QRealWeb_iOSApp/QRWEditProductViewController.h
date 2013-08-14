//
//  QRWEditProductViewController.h
//  QRealWeb_iOSApp
//
//  Created by Иван Афанасьев on 14.08.13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import "QRWEditItemViewController.h"
#import "QRWProduct.h"

@interface QRWEditProductViewController : QRWEditItemViewController


@property (nonatomic, strong) IBOutlet UITextField *priceTextView;


- (id)initWithProduct: (QRWProduct *) product;

@end

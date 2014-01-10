//
//  QRWProductInfoViewController.h
//  XCartAdmin
//
//  Created by Ivan Afanasiev on 10/01/14.
//  Copyright (c) 2014 Ivan Afanasiev. All rights reserved.
//

#import "QRWBaseViewController.h"

@interface QRWProductInfoViewController : QRWBaseViewController

@property (strong, nonatomic) IBOutlet UILabel *nameLabel;
@property (strong, nonatomic) IBOutlet UIImageView *imageImageView;
@property (strong, nonatomic) IBOutlet UILabel *inStock;
@property (strong, nonatomic) IBOutlet UIWebView *descriptionWebView;


@property (strong, nonatomic) IBOutlet UIButton *priceButton;
@property (strong, nonatomic) IBOutlet UIButton *showFull;

@property (strong, nonatomic) IBOutlet UISwitch *availiabilitySwitcher;



- (id) initWithProduct:(QRWProductWithInfo *)product;

@end

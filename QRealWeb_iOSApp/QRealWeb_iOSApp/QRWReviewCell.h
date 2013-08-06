//
//  QRWReviewCell.h
//  QRealWeb_iOSApp
//
//  Created by Ivan Afanasiev on 8/5/13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface QRWReviewCell : UITableViewCell


@property (strong, nonatomic) IBOutlet UILabel *productLable;
@property (strong, nonatomic) IBOutlet UILabel *userLable;
@property (strong, nonatomic) IBOutlet UILabel *messageLable;


- (float) heightOfLabel;

@end

//
//  QRWUserCell.h
//  XCartAdmin
//
//  Created by Ivan Afanasiev on 24/12/13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface QRWUserCell : UITableViewCell

@property (nonatomic, strong) IBOutlet UILabel *emailLabel;
@property (nonatomic, strong) IBOutlet UILabel *userTypeLabel;
@property (nonatomic, strong) IBOutlet UILabel *lastLoginLabel;

@end

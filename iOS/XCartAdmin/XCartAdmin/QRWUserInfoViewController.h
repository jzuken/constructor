//
//  QRWUserInfoViewController.h
//  XCartAdmin
//
//  Created by Иван Афанасьев on 11.01.14.
//  Copyright (c) 2014 Ivan Afanasiev. All rights reserved.
//

#import "QRWBaseViewControllerWithTableView.h"
#import <MessageUI/MFMailComposeViewController.h>

@interface QRWUserInfoViewController : QRWBaseViewControllerWithTableView<MFMailComposeViewControllerDelegate>

- (id)initWithUserInfo: (QRWUserInfo *)userInfo;

- (IBAction)emailButtonClicked:(id)sender;
- (IBAction)callButtonClicked:(id)sender;


@end

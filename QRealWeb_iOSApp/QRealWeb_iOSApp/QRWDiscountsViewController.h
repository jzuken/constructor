//
//  QRWDiscountsViewController.h
//  QRealWeb_iOSApp
//
//  Created by Ivan Afanasiev on 8/5/13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import "QRWBaseViewController.h"

@interface QRWDiscountsViewController : QRWBaseViewController<UITableViewDataSource, UITableViewDelegate, UIActionSheetDelegate>


@property (nonatomic, strong) IBOutlet UITableView *discountsTableView;


@end

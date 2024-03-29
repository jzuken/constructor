//
//  DLSDetailsOfDocumentViewController.h
//  FirstLineSoftwareTestProject
//
//  Created by Иван Афанасьев on 06.04.13.
//  Copyright (c) 2013 Иван Афанасьев. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "QRWBaseViewController.h"

@interface QRWProductsViewController : QRWBaseViewController<UIActionSheetDelegate, UISearchBarDelegate, UITableViewDataSource, UITableViewDelegate>

@property (nonatomic, strong) IBOutlet UITableView *productsTableView;
@property (nonatomic, strong) IBOutlet UISearchBar *productsSearchBar;

@end

//
//  FLSListScrinViewController.h
//  FirstLineSoftwareTestProject
//
//  Created by Иван Афанасьев on 05.04.13.
//  Copyright (c) 2013 Иван Афанасьев. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "QWDBaseViewController.h"

@interface QWDToolsScrinViewController : QWDBaseViewController <UIScrollViewDelegate>


@property (strong, nonatomic) IBOutlet UIScrollView *toolsScrollView;


@end

//
//  QWDToolView.h
//  QRealWebDemoProject
//
//  Created by Ivan Afanasiev on 7/8/13.
//  Copyright (c) 2013 Иван Афанасьев. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface QWDToolView : UIView

-(id)initWithName: (NSString *)nameOfTool image:(UIImage *) imageOfTool actionBlock: (void (^)(void))toolButtonAction;

@end

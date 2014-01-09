//
//  QWDToolView.h
//  QRealWebDemoProject
//
//  Created by Ivan Afanasiev on 7/8/13.
//  Copyright (c) 2013 Иван Афанасьев. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface QRWToolView : UIView

-(id)initWithFrame:(CGRect)frame
              name:(NSString *)nameOfTool
   backgroundimage:(UIImage *)imageOfTool
   indicatorOfTool:(NSString *) indicator
       actionBlock:(void (^)(void))toolButtonAction;

@end

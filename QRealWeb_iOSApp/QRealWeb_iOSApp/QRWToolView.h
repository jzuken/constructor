//
//  QWDToolView.h
//  QRealWebDemoProject
//
//  Created by Ivan Afanasiev on 7/8/13.
//  Copyright (c) 2013 Иван Афанасьев. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface QRWToolView : UIView

//-(id)initWithName: (NSString *)nameOfTool image:(UIImage *) imageOfTool actionBlock: (void (^)(void))toolButtonAction;
-(id)initWithName: (NSString *)nameOfTool image:(UIImage *) imageOfTool selectedImage:(UIImage *) imageOfSelectedTool  actionBlock: (void (^)(void))toolButtonAction;

@end

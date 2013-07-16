//
//  QWDToolView.m
//  QRealWebDemoProject
//
//  Created by Ivan Afanasiev on 7/8/13.
//  Copyright (c) 2013 Иван Афанасьев. All rights reserved.
//

#import "QWDToolView.h"
#import <QuartzCore/QuartzCore.h>
#import "constants.h"
#import "UIButton+Block.h"


@implementation QWDToolView

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
    }
    return self;
}


-(id)initWithName: (NSString *)nameOfTool image:(UIImage *) imageOfTool actionBlock: (void (^)(void))toolButtonAction
{
    self = [self initWithFrame:CGRectMake(0, 0, kSideOfToolView, kSideOfToolView)];
    
    UIButton *actionButton = [UIButton buttonWithType:UIButtonTypeCustom];
    UIImageView *imageOfToolView = [[UIImageView alloc] initWithFrame:self.frame];
    UILabel *nameOfToolLabel = [[UILabel alloc] initWithFrame:CGRectMake(self.frame.origin.x, self.frame.origin.x/2, self.frame.size.width, self.frame.size.height/2)];
    
//    [actionButton addTarget:self action:@selector(toolButtonAction) forControlEvents:UIControlEventTouchUpInside];
    [actionButton setAction:@"TouchInside" withBlock:toolButtonAction];
    
    [actionButton setFrame:self.frame];
    [imageOfToolView setImage:imageOfTool];
    [nameOfToolLabel setText:nameOfTool];
    [nameOfToolLabel setTextAlignment:NSTextAlignmentCenter];
    [nameOfToolLabel setBackgroundColor:[UIColor clearColor]];
    
    [self addSubview:imageOfToolView];
    [self addSubview:nameOfToolLabel];
    [self addSubview:actionButton];
    
    self.layer.shadowOffset = CGSizeMake(5, 0);
    self.layer.shadowRadius = 5;
    self.layer.shadowOpacity = 0.5;
    self.alpha = 0.8;
    
    return self;
}

@end

//
//  QWDToolView.m
//  QRealWebDemoProject
//
//  Created by Ivan Afanasiev on 7/8/13.
//  Copyright (c) 2013 Иван Афанасьев. All rights reserved.
//

#import "QRWToolView.h"
#import <QuartzCore/QuartzCore.h>
#import "constants.h"
#import "UIButton+Block.h"


@implementation QRWToolView

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
    }
    return self;
}


-(id)initWithFrame:(CGRect) frame   name: (NSString *)nameOfTool backgroundimage:(UIImage *) imageOfTool indicatorOfTool: (NSString *) indicator  actionBlock: (void (^)(void))toolButtonAction
{
    self = [self initWithFrame:frame];
    
    UIButton *actionButton = [UIButton buttonWithType:UIButtonTypeCustom];
    UIImageView *imageOfToolView = [[UIImageView alloc] initWithFrame:self.frame];
    UILabel *nameOfToolLabel = [[UILabel alloc] initWithFrame:CGRectMake(self.frame.origin.x, self.frame.origin.x/2, self.frame.size.width, self.frame.size.height/2)];
    UILabel *numbersLabel = [[UILabel alloc] initWithFrame:CGRectMake(self.frame.origin.x, 0, self.frame.size.width, self.frame.size.height/2)];
    
    [actionButton setAction:@"TouchInside" withBlock:toolButtonAction];
    [actionButton setFrame:self.frame];
    actionButton.backgroundColor = [UIColor clearColor];
    
    [imageOfToolView setImage:imageOfTool];
    
    [nameOfToolLabel setText:nameOfTool];
    [nameOfToolLabel setTextAlignment:NSTextAlignmentCenter];
    [nameOfToolLabel setBackgroundColor:[UIColor clearColor]];
    
    [numbersLabel setText:indicator];
    [numbersLabel setTextAlignment:NSTextAlignmentCenter];
    [numbersLabel setBackgroundColor:[UIColor clearColor]];
    
    [self addSubview:imageOfToolView];
    [self addSubview:numbersLabel];
    [self addSubview:nameOfToolLabel];
    [self addSubview:actionButton];
    
    self.alpha = 0.8;
    
    return self;
}

@end

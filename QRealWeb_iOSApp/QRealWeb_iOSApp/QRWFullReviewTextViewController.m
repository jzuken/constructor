//
//  QRWFullReviewTextViewController.m
//  QRealWeb_iOSApp
//
//  Created by Ivan Afanasiev on 8/7/13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import "QRWFullReviewTextViewController.h"

@interface QRWFullReviewTextViewController ()

@property (nonatomic, strong) QRWReview *review;


@end

@implementation QRWFullReviewTextViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    self.navigationController.navigationBarHidden = NO;
    self.navigationItem.title = NSLocalizedString(@"REVIEW", nil);
    
    _productLable.text = [_review product];
    _userLable.text = [_review email];
    _messageLable.text = [_review message];
    
//    CGSize maximumLabelSize = CGSizeMake(296, FLT_MAX);
//    
//    CGSize expectedLabelSize = [[_review message] sizeWithFont:_messageLable.font constrainedToSize:maximumLabelSize lineBreakMode:_messageLable.lineBreakMode];
    
    _messageLableScrollView.contentSize = CGSizeMake(_messageLableScrollView.frame.size.width, [self heightOfTheLabel:_messageLable]);
    
    CGRect messageFrame = _messageLable.frame;
    DLog(@"Size of text is: x = %f y = %f", messageFrame.size.width, messageFrame.size.height);
    messageFrame.size.height = [self heightOfTheLabel:_messageLable];
    DLog(@"Size of text is: x = %f y = %f", messageFrame.size.width, messageFrame.size.height);
    _messageLable.frame = messageFrame;
    DLog(@"Size of text is: x = %f y = %f", _messageLable.frame.size.width, _messageLable.frame.size.height);
    
    [_messageLableScrollView addSubview:_messageLable];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}

- (id)initWithReview: (QRWReview *) review
{
    self = [self initWithNibName:@"QRWFullReviewTextViewController" bundle:nil];
    _review = review;
    return self;
}


- (CGFloat) heightOfTheLabel:(UILabel *)label
{
    UITextView *textView = [[UITextView alloc] init];
    textView.text = label.text;
    textView.font = label.font;
    textView.frame = CGRectMake(0, 0, label.frame.size.width, 10);
    return textView.contentSize.height;
}



@end

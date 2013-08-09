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

@property (strong, nonatomic) UILabel *messageLable;

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

}

- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    
    _messageLable = [[UILabel alloc] initWithFrame:CGRectMake(5, 0, self.view.frame.size.width - 10, 0)];
    
    _messageLable.text = [_review message];
    _messageLable.numberOfLines = 0;
    _messageLable.textAlignment = NSTextAlignmentCenter;
    _messageLable.backgroundColor = [UIColor clearColor];
    
    CGRect messageFrame = _messageLable.frame;
    messageFrame.size.height = [self heightOfTheLabel:_messageLable];
    _messageLable.frame = messageFrame;
        
    _messageLableScrollView.contentSize = CGSizeMake(self.view.frame.size.width, [self heightOfTheLabel:_messageLable]);
    [_messageLableScrollView addSubview:_messageLable];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}

- (void)exitButtonClicked:(id)sender
{
    [self dismissViewControllerAnimated:YES completion:nil];
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

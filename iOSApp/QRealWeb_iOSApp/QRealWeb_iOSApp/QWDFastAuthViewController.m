//
//  QWDFastAuthViewController.m
//  QRealWebDemoProject
//
//  Created by Ivan Afanasiev on 7/8/13.
//  Copyright (c) 2013 Иван Афанасьев. All rights reserved.
//

#import "QWDFastAuthViewController.h"
#import "QWDToolsScrinViewController.h"


#define SYNCHRONIZE_CAROUSELS NO

@interface QWDFastAuthViewController ()

@end

@implementation QWDFastAuthViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (id)init
{
    return [self initWithNibName:@"QWDFastAuthViewController" bundle:nil];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    _passcodeCarousel.type = iCarouselTypeLinear;
    _passcodeCarousel.centerItemWhenSelected = NO;
    _passcodeCarousel.delegate = self;
    _passcodeCarousel.dataSource = self;
    [_passcodeCarousel setCurrentItemIndex:2];
    _passcodeCarousel.scrollEnabled = NO;
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}



- (void)showFirstRunMessage
{
    [_firstLoginLabel setText: NSLocalizedString(@"FIRST_LOGIN_MESSAGE", nil)];
}


#pragma mark - iCarousel methods

- (NSUInteger)numberOfItemsInCarousel:(iCarousel *)carousel;
{
    if (carousel == _passcodeCarousel) {
        return 5;
    } else {
        return 10;
    }
}
- (UIView *)carousel:(iCarousel *)carousel viewForItemAtIndex:(NSUInteger)index reusingView:(UIView *)view
{
    if ([carousel isEqual:_passcodeCarousel]) {
        iCarousel *verticalCarousel = [[iCarousel alloc] initWithFrame:CGRectMake(0.0f, 0.0f, 110.0f, self.view.bounds.size.height - 400)];
        verticalCarousel.dataSource = self;
        verticalCarousel.delegate = self;
        verticalCarousel.vertical = YES;
        verticalCarousel.type = iCarouselTypeCylinder;
        verticalCarousel.scrollOffset = 0.0f;
        verticalCarousel.tag = index;
        
        return verticalCarousel;
    } else {
        UIView *numberView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 60, 60)];
        
        UIImageView *placeholderNumberView = [[UIImageView alloc] initWithFrame:numberView.frame];
        [placeholderNumberView setImage:[UIImage imageNamed:@"Black_white_Apple_background.jpg"]];
        
        UILabel *numberLabel = [[UILabel alloc] initWithFrame:numberView.frame];
        [numberLabel setText:[NSString stringWithFormat:@"%d", index]];
        [numberLabel setTextAlignment:NSTextAlignmentCenter];
        numberLabel.backgroundColor = [UIColor clearColor];
        numberLabel.textColor = [UIColor whiteColor];
        
        [numberView addSubview:placeholderNumberView];
        [numberView addSubview:numberLabel];
        
        return numberView;
    }
}
- (CGFloat)carouselItemWidth:(iCarousel *)carousel
{
    return 65.0f;
}



- (void)carouselDidScroll:(iCarousel *)carousel
{
    if (carousel == _passcodeCarousel){
        for (iCarousel *subCarousel in _passcodeCarousel.visibleItemViews){
            NSInteger index = subCarousel.tag;
            CGFloat offset = [_passcodeCarousel offsetForItemAtIndex:index];
            subCarousel.viewpointOffset = CGSizeMake(-offset * _passcodeCarousel.itemWidth, 0.0f);
            subCarousel.contentOffset = CGSizeMake(-offset * _passcodeCarousel.itemWidth, 0.0f);
        }
    }
    else if (SYNCHRONIZE_CAROUSELS){
        for (iCarousel *subCarousel in _passcodeCarousel.visibleItemViews){
            subCarousel.scrollOffset = carousel.scrollOffset;
        }
    }
}

- (CGFloat)carousel:(iCarousel *)carousel valueForOption:(iCarouselOption)option withDefault:(CGFloat)value
{
    switch (option){
        case iCarouselOptionShowBackfaces:{
            return NO;
        }
        case iCarouselOptionVisibleItems: {
            if (carousel == _passcodeCarousel) {
                return value + 2;
            }
            return value;
        }
        case iCarouselOptionCount:{
            if (carousel != _passcodeCarousel){
                return 8;
            }
            return value;
        }
        default: {
            return value;
        }
    }
}


- (IBAction)enterToShopAction:(id)sender
{
    QWDToolsScrinViewController *listOfDocumentsViewController = [[QWDToolsScrinViewController alloc] init];
    [self.navigationController pushViewController:listOfDocumentsViewController animated:YES];
}
@end

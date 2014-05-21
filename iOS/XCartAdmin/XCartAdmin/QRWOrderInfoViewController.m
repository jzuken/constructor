//
//  QRWOrderInfoViewController.m
//  XCartAdmin
//
//  Created by Ivan Afanasiev on 14/01/14.
//  Copyright (c) 2014 Ivan Afanasiev. All rights reserved.
//

#import "QRWOrderInfoViewController.h"

@interface QRWOrderInfoViewController ()


@end

@implementation QRWOrderInfoViewController

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
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    
}


- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 100;
}

- (void)configureCell:(UITableViewCell *)cell atIndexPath:(NSIndexPath *)indexPath
{
    [cell setAccessoryType:UITableViewCellAccessoryNone];
}


- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    UIView *headerView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 320, 30)];
    headerView.backgroundColor = kGreyColor;
    
    UILabel *nameLabel = [[UILabel alloc] initWithFrame:CGRectMake(0, 1, 320, 29)];
    nameLabel.font = [UIFont systemFontOfSize:15];
    QRWReview *review = [self.dataArray objectAtIndex:section];
    nameLabel.text = review.product;
    nameLabel.textColor = kTextBlueColor;
    nameLabel.adjustsFontSizeToFitWidth=YES;
    nameLabel.minimumScaleFactor = 0.5;
    nameLabel.backgroundColor = [UIColor whiteColor];
    
    [headerView addSubview:nameLabel];
    
    return headerView;
}

@end

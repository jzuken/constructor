//
//  QRWBaseViewControllerWithTableView.m
//  XCartAdmin
//
//  Created by Ivan Afanasiev on 24/12/13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import "QRWBaseViewControllerWithTableView.h"

@interface QRWBaseViewControllerWithTableView ()

@end

@implementation QRWBaseViewControllerWithTableView




- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return [_dataArray count];
}


- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return 1;
}




- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSArray *topLevelObjects = [[NSBundle mainBundle] loadNibNamed:NSStringFromClass([_baseCell class]) owner:self options:nil];
    UITableViewCell *cell = [topLevelObjects objectAtIndex:0];
    
    [self configureProductCell:cell atIndexPath:indexPath];
    
    return cell;
}

- (void)configureProductCell:(UITableViewCell *)cell atIndexPath:(NSIndexPath *)indexPath
{
    
}

@end

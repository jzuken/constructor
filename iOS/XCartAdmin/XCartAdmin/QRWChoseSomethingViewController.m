//
//  QRWChoseSomethingViewController.m
//  XCartAdmin
//
//  Created by Ivan Afanasyev on 01.09.14.
//  Copyright (c) 2014 Ivan Afanasiev. All rights reserved.
//

#import "QRWChoseSomethingViewController.h"

@interface QRWChoseSomethingViewController ()

@property (nonatomic, copy) void (^selectOption)(NSString *selectedOption);
@property (nonatomic, assign) NSUInteger selectedIndex;

@end

@implementation QRWChoseSomethingViewController

- (id)initWithOptionsDictionary:(NSArray *)options
                  selectedIndex:(NSUInteger)selectedIndex
              selectOptionBlock:(void(^)(NSString *selectedOption))selectOptionBlock
{
    self = [super init];
    if (self) {
        self.dataArray = options;
        self.selectOption = selectOptionBlock;
        self.selectedIndex = selectedIndex;
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    [self.navigationController setNavigationBarHidden:NO animated:YES];
    [self.tableView setShowsPullToRefresh:NO];
    [self.tableView setShowsInfiniteScrolling:NO];
}

#pragma mark

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return 1;
}

-(CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    return 0;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:NSStringFromClass([UITableViewCell class])];
    if (!cell) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault
                                      reuseIdentifier:NSStringFromClass([UITableViewCell class])];
    }
    
    cell.textLabel.text = QRWLoc(self.dataArray[indexPath.section]);
    if (indexPath.section == self.selectedIndex) {
        [cell setAccessoryType:UITableViewCellAccessoryCheckmark];
    }
    
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [self.tableView deselectRowAtIndexPath:indexPath animated:YES];
    
    for (UIView *subview in self.tableView.subviews) {
        if ([subview isKindOfClass:[UITableViewCell class]]) {
            [(UITableViewCell *)subview setAccessoryType:UITableViewCellAccessoryNone];
        }
    }
    
    [[self tableView:tableView cellForRowAtIndexPath:indexPath] setAccessoryType:UITableViewCellAccessoryCheckmark];
    
    if (self.selectOption) {
        [self startLoadingAnimation];
        self.selectOption(self.dataArray[indexPath.section]);
    }
}

@end

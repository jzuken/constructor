//
//  QRWChoseSomethingViewController.m
//  XCartAdmin
//
//  Created by Ivan Afanasyev on 01.09.14.
//  Copyright (c) 2014 Ivan Afanasiev. All rights reserved.
//

#import "QRWChoseSomethingViewController.h"

#import "QRWChoseOptionTableViewCell.h"
#import "QRWProductWithInfo.h"

@interface QRWChoseSomethingViewController ()

@property (nonatomic, copy) void (^selectOption)(NSString *selectedOption);
@property (nonatomic, assign) QRWChoseSomethingViewControllerType type;

@end

@implementation QRWChoseSomethingViewController

- (id)init
{
    self = [[UIStoryboard storyboardWithName:@"MainStoryboard" bundle:nil] instantiateViewControllerWithIdentifier:@"QRWChoseSomethingViewController"];
    return self;
}

- (id)initWithOptionsDictionary:(NSArray *)options
                  selectedIndex:(NSUInteger)selectedIndex
                           type:(QRWChoseSomethingViewControllerType)type
              selectOptionBlock:(void(^)(NSString *selectedOption))selectOptionBlock;
{
    self = [self init];
    if (self) {
        self.type = type;
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

#pragma mark - tableView

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
    UITableViewCell *cell;
    
    switch (self.type) {
        case QRWChoseSomethingViewControllerTypeStrings:{
            cell = [tableView dequeueReusableCellWithIdentifier:NSStringFromClass([UITableViewCell class])];
            if (!cell) {
                cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault
                                              reuseIdentifier:NSStringFromClass([UITableViewCell class])];
            }
            
            cell.textLabel.text = QRWLoc(self.dataArray[indexPath.section]);
        }
            break;
            
        default:{
            cell = [tableView dequeueReusableCellWithIdentifier:NSStringFromClass([QRWChoseOptionTableViewCell class])];
            if (!cell) {
                cell = [[QRWChoseOptionTableViewCell alloc] initWithStyle:UITableViewCellStyleDefault
                                                          reuseIdentifier:NSStringFromClass([QRWChoseOptionTableViewCell class])];
            }
            
            QRWProductVariant *variant = self.dataArray[indexPath.section];
            
            NSMutableString *optionsString = [NSMutableString new];
            [variant.options enumerateObjectsUsingBlock:^(QRWProductVariantOption *option, NSUInteger idx, BOOL *stop) {
                [optionsString appendString:[NSString stringWithFormat:@"%@: %@ \n", option.optionName, option.optionValue]];
            }];
            
            [(QRWChoseOptionTableViewCell *)cell SKULabel].text = [variant SKUOfVariant];
            [(QRWChoseOptionTableViewCell *)cell optionsLabel].text = optionsString;
        }
            break;
    }
    
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
        self.selectOption(self.dataArray[indexPath.section]);
    }
}

@end

//
//  FLSDataManager.m
//  FirstLineSoftwareTestProject
//
//  Created by Иван Афанасьев on 06.04.13.
//  Copyright (c) 2013 Иван Афанасьев. All rights reserved.
//

#import "FLSDataManager.h"
#import "constants.h"
#import "QWDToolView.h"
#import "QWDProductsViewController.h"
#import "QWDToolsScrinViewController.h"

@implementation FLSDataManager

static FLSDataManager *_instance;



-(id)init
{
    if (_instance != nil) {
        return _instance;
    }
    self = [super init];
    return self;
}

+ (FLSDataManager *)instance
{
    @synchronized(self){
        if (_instance == nil) {
            _instance = [FLSDataManager new];
        }
    }
    return _instance;
}

-(void)sendAuthorizationRequestWithLogin:(NSString *)login andPassowrd:(NSString *)password
{
    [_delegate respondsForAuthRequest:([kTestUsername isEqualToString:login] && [kTestPassword isEqualToString:password])];

}

-(void)sendToolsRequest
{
    NSMutableArray *tools = [[NSMutableArray alloc] init];
    
    void (^actionBlock)(void) = ^{
        QWDProductsViewController *productsViewController = [[QWDProductsViewController alloc] init];
        [[(QWDToolsScrinViewController *)_delegate navigationController] pushViewController:productsViewController animated:YES];
    };
    
    for (int index = 0; index < 10; index++) {
        QWDToolView *toolView = [[QWDToolView alloc] initWithName:[NSString stringWithFormat:@"TOOL %d", index] image:[UIImage imageNamed:@"Black_white_Apple_background.jpg"] actionBlock:actionBlock];
        [tools addObject:toolView];
    }
    
    [_delegate respondsForToolsRequest:tools];
    
}

@end


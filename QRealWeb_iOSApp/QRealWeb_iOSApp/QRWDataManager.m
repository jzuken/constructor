//
//  FLSDataManager.m
//  FirstLineSoftwareTestProject
//
//  Created by Иван Афанасьев on 06.04.13.
//  Copyright (c) 2013 Иван Афанасьев. All rights reserved.
//

#import "QRWDataManager.h"
#import "constants.h"
#import "URLsList.h"

#import "QRWToolView.h"
#import "QRWProductsViewController.h"
#import "QRWToolsScrinViewController.h"
#import "QRWMainScrinViewController.h"

#import "QRWDashboardViewController.h"

@implementation QRWDataManager

static QRWDataManager *_instance;



-(id)init
{
    if (_instance != nil) {
        return _instance;
    }
    self = [super init];
    return self;
}

+ (QRWDataManager *)instance
{
    @synchronized(self){
        if (_instance == nil) {
            _instance = [QRWDataManager new];
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
        [[(QRWMainScrinViewController *)_delegate navigationController] pushViewController:[[QRWProductsViewController alloc] init] animated:YES];
    };
    
    void (^dashboardActionBlock)(void) = ^{
        [[(QRWMainScrinViewController *)_delegate navigationController] pushViewController:[[QRWDashboardViewController alloc] init] animated:YES];
    };
    
    for (int index = 0; index < 6; index++) {
        QRWToolView *toolView;
        switch (index) {
            case 0:
                toolView = [[QRWToolView alloc] initWithName:@"" image:[UIImage imageNamed:@"dashboardIcon.jpg"] actionBlock:dashboardActionBlock];
                break;
            
            case 1:
                toolView = [[QRWToolView alloc] initWithName:@"" image:[UIImage imageNamed:@"usersIcon.jpg"] actionBlock:actionBlock];
                break;
                
            case 2:
                toolView = [[QRWToolView alloc] initWithName:@"" image:[UIImage imageNamed:@"discountsIcon.jpg"] actionBlock:actionBlock];
                break;
                
            case 3:
                toolView = [[QRWToolView alloc] initWithName:@"" image:[UIImage imageNamed:@"productsIcon.jpg"] actionBlock:actionBlock];
                break;
                
            case 4:
                toolView = [[QRWToolView alloc] initWithName:@"" image:[UIImage imageNamed:@"reviewsIcon.jpg"] actionBlock:actionBlock];
                break;
                
            case 5:
                toolView = [[QRWToolView alloc] initWithName:@"" image:[UIImage imageNamed:@"logoutIcon.jpg"] actionBlock:actionBlock];
                break;
                
            default:
                break;
        }
        [tools addObject:toolView];
    }
    
    [_delegate respondsForToolsRequest:tools];
    
}



- (void)sendLastOrderRequest
{
    QRWDownloader *lastOrderDownloader = [[QRWDownloader alloc] initWithRequestURL:[NSURL URLWithString:url_lastOrderURL]];
    [lastOrderDownloader startDownloadWithDelegate:self];
}

- (void)sendLastOrderResponse: (NSDictionary *) jsonDictionary
{
    QRWLastOrder *lastOrder = [[QRWLastOrder alloc] init];
    
    
    NSNumberFormatter * formatter = [[NSNumberFormatter alloc] init];
    [formatter setNumberStyle:NSNumberFormatterDecimalStyle];
    
    lastOrder.lastname = [jsonDictionary objectForKey:@"lastname"];
    lastOrder.firstname = [jsonDictionary objectForKey:@"firstname"];
    lastOrder.email = [jsonDictionary objectForKey:@"email"];
    lastOrder.email = [jsonDictionary objectForKey:@"email"];
    lastOrder.status = [jsonDictionary objectForKey:@"status"];
    lastOrder.date = [jsonDictionary objectForKey:@"date"];
    
    lastOrder.orderid = [formatter numberFromString: (NSString *)[jsonDictionary objectForKey:@"orderid"]];
    lastOrder.userid = [formatter numberFromString: (NSString *)[jsonDictionary objectForKey:@"userid"]];
    lastOrder.total = [formatter numberFromString: (NSString *)[jsonDictionary objectForKey:@"total"]];
    
    [_delegate respondsForLastOrderRequest:lastOrder];
}



- (void)downloadWasFinishedWithData:(NSMutableData *)jsonData forRequestURL:(NSURL *)requesrURL
{
    NSError *error;
    NSMutableString *result = [[NSMutableString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
    [result deleteCharactersInRange:NSMakeRange(0, 1)];
    [result deleteCharactersInRange:NSMakeRange(result.length - 1, 1)];
    
    DLog(@"Receive data: %@", result);
    NSDictionary *jsonDictionary = [NSJSONSerialization JSONObjectWithData:[result dataUsingEncoding:NSUTF8StringEncoding] options:kNilOptions error:&error];
    
    if ([url_lastOrderURL isEqualToString:requesrURL.absoluteString]) {
        [self sendLastOrderResponse:jsonDictionary];
    }
}


- (void)downloadWasFailedWithError:(NSError *)error forRequestURL:(NSURL *)requesrURL
{
    
}



@end


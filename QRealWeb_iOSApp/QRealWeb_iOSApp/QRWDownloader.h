//
//  QRWDownloader.h
//  QRealWeb_iOSApp
//
//  Created by Ivan Afanasiev on 7/23/13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import <Foundation/Foundation.h>


@protocol QRWDownloaderDelegate

- (void) downloadWasFinishedWithData: (NSMutableData *) jsonData forRequestURL:(NSURL *) requesrURL;
- (void) downloadWasFailedWithError: (NSError *) error forRequestURL:(NSURL *) requesrURL;;

@end



@interface QRWDownloader : NSObject<NSURLConnectionDelegate>
{
    @private
    NSMutableData *jsonData;
}

@property (nonatomic, strong) id<QRWDownloaderDelegate> delegate;
@property (nonatomic, strong) NSURL *requestURL;

- (id) initWithRequestURL: (NSURL *) requestURL;
- (void) startDownloadWithDelegate: (id<QRWDownloaderDelegate> ) delegate;

@end

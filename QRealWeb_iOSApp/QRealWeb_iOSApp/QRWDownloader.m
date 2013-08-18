//
//  QRWDownloader.m
//  QRealWeb_iOSApp
//
//  Created by Ivan Afanasiev on 7/23/13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import "QRWDownloader.h"

@implementation QRWDownloader


- (id)initWithRequestURL:(NSURL *)requestURL
{
    self = [self init];
    _requestURL = requestURL;
    return self;
}

- (void)startDownloadWithDelegate:(id<QRWDownloaderDelegate>)delegate
{
    _delegate = delegate;
    
    NSURLRequest *theRequest=[NSURLRequest requestWithURL:_requestURL
                                              cachePolicy:NSURLRequestUseProtocolCachePolicy
                                          timeoutInterval:60.0];

    NSURLConnection *theConnection=[[NSURLConnection alloc] initWithRequest:theRequest delegate:self];
    if (theConnection) {
        jsonData = [NSMutableData data];
    } else {
        NSLog(@"Connection failed");
    }
    
//    NSURLProtocol
}

#pragma mark NSURLConnection methods

- (void)connection:(NSURLConnection *)connection didReceiveResponse:(NSURLResponse *)response
{
    // This method is called when the server has determined that it
    // has enough information to create the NSURLResponse.
	
    // It can be called multiple times, for example in the case of a
    // redirect, so each time we reset the data.
	
    // receivedData is an instance variable declared elsewhere.
}

- (void)connection:(NSURLConnection *)connection didReceiveData:(NSData *)data
{
    [jsonData appendData:data];
    [_delegate downloadWasFinishedWithData:jsonData forRequestURL:_requestURL];
}

- (void)connection:(NSURLConnection *)connection didFailWithError:(NSError *)error
{
    [_delegate downloadWasFailedWithError:error forRequestURL:_requestURL];
}

- (void)connectionDidFinishLoading:(NSURLConnection *)connection
{
    // do something with the data
    // receivedData is declared as an instance variable elsewhere
    // in this example, convert data (from plist) to a string and then to a dictionary
}

@end

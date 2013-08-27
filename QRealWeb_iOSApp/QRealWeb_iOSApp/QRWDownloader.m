//
//  QRWDownloader.m
//  QRealWeb_iOSApp
//
//  Created by Ivan Afanasiev on 7/23/13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import "QRWDownloader.h"

@interface QRWDownloader ()

@property (nonatomic, strong) NSMutableURLRequest *theRequest;

@end



@implementation QRWDownloader


- (id)initWithRequestURL:(NSURL *)requestURL
{
    self = [self init];
    _requestURL = requestURL;
    _theRequest = [NSMutableURLRequest requestWithURL:_requestURL
                                   cachePolicy:NSURLRequestUseProtocolCachePolicy
                               timeoutInterval:60.0];
    return self;
}



- (id)initWithRequestURL:(NSURL *)requestURL parametres:(NSDictionary *)parametres
{
    self = [self init];
    _requestURL = requestURL;
    _theRequest = [NSMutableURLRequest requestWithURL:_requestURL
                                   cachePolicy:NSURLRequestUseProtocolCachePolicy
                               timeoutInterval:60.0];
    
    for (NSString *parametr in [parametres allKeys]) {
        [_theRequest addValue:[parametres objectForKey:parametr] forHTTPHeaderField:parametr];
    }
    return self;
}


- (void)startDownloadWithDelegate:(id<QRWDownloaderDelegate>)delegate
{
    _delegate = delegate;

    NSURLConnection *theConnection=[[NSURLConnection alloc] initWithRequest:_theRequest delegate:self];
    if (theConnection) {
        jsonData = [NSMutableData data];
    } else {
        NSLog(@"Connection failed");
    }
}

#pragma mark NSURLConnection methods


- (void)connection:(NSURLConnection *)connection didReceiveData:(NSData *)data
{
    [jsonData appendData:data];
    [_delegate downloadWasFinishedWithData:jsonData forRequestURL:_requestURL];
}

- (void)connection:(NSURLConnection *)connection didFailWithError:(NSError *)error
{
    [_delegate downloadWasFailedWithError:error forRequestURL:_requestURL];
}


- (BOOL)connection:(NSURLConnection *)connection canAuthenticateAgainstProtectionSpace:(NSURLProtectionSpace *)protectionSpace {
    return [protectionSpace.authenticationMethod isEqualToString:NSURLAuthenticationMethodServerTrust];
}

- (void)connection:(NSURLConnection *)connection didReceiveAuthenticationChallenge:(NSURLAuthenticationChallenge *)challenge {
    if ([challenge.protectionSpace.authenticationMethod isEqualToString:NSURLAuthenticationMethodServerTrust])
        if ([[_requestURL mutableCopy] containsObject:challenge.protectionSpace.host])
            [challenge.sender useCredential:[NSURLCredential credentialForTrust:challenge.protectionSpace.serverTrust] forAuthenticationChallenge:challenge];
    
    [challenge.sender continueWithoutCredentialForAuthenticationChallenge:challenge];
}

@end

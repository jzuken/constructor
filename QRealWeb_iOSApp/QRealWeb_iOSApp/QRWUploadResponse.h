//
//  QRWUploadResponse.h
//  QRealWeb_iOSApp
//
//  Created by Ivan Afanasiev on 8/6/13.
//  Copyright (c) 2013 Ivan Afanasiev. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface QRWUploadResponse : NSObject


@property (nonatomic, strong) NSNumber *objectID;
@property (nonatomic, strong) NSString *uploadData;
@property (nonatomic, strong) NSString *uploadType;
@property (nonatomic, strong) NSString *uploadStatus;


@end

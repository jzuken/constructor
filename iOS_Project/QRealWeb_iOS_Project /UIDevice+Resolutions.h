//
//  UIDevice+Resolutions.h
//  FirstLineSoftwareTestProject
//
//  Created by Иван Афанасьев on 05.04.13.
//  Copyright (c) 2013 Иван Афанасьев. All rights reserved.
//

#import <UIKit/UIKit.h>

enum {
    UIDeviceResolution_Unknown,
    UIDeviceResolution_iPhoneStandard,    // iPhone 1,3,3GS Standard Display  (320x480px)
    UIDeviceResolution_iPhoneRetina4,    // iPhone 4,4S Retina Display 3.5"  (640x960px)
    UIDeviceResolution_iPhoneRetina5,    // iPhone 5 Retina Display 4"       (640x1136px)
    UIDeviceResolution_iPadStandard,    // iPad 1,2,mini Standard Display   (1024x768px)
    UIDeviceResolution_iPadRetina             // iPad 3 Retina Display            (2048x1536px)
}; typedef NSUInteger UIDeviceResolution;



@interface UIDevice (Resolutions)

- (UIDeviceResolution)resolution;

NSString *NSStringFromResolution(UIDeviceResolution resolution);

@end

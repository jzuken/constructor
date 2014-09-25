//
//  QRWSettingsViewController.m
//  XCartAdmin
//
//  Created by Иван Афанасьев on 26.09.14.
//  Copyright (c) 2014 Ivan Afanasiev. All rights reserved.
//

#import "QRWSettingsViewController.h"
#import "QRWUnlockViewController.h"

@interface QRWSettingsViewController ()

@end

@implementation QRWSettingsViewController


- (id)init
{
    self = [[UIStoryboard storyboardWithName:@"MainStoryboard" bundle:nil] instantiateViewControllerWithIdentifier:@"QRWSettingsViewController"];
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    [self.navigationController setNavigationBarHidden:NO animated:YES];
    [self setNavigationBarColor:kBlueColor title:QRWLoc(@"SETTINGS")];
}

- (IBAction)passwordSwitchAction:(UISwitch *)passwordSwitch
{
    [[NSUserDefaults standardUserDefaults] setObject:[NSNumber numberWithBool:passwordSwitch.isOn] forKey:@"QRW_PINenabled"];
    
    if (passwordSwitch.isOn) {
        [QRWUnlockViewController showUnlockViewOnViewController:self editPasswordMode:YES];
    }
}

- (IBAction)pushNotificationsSwitchAction:(UISwitch *)pushNotificationsSwitch
{
    pushNotificationsSwitch.isOn ? [[UIApplication sharedApplication] registerForRemoteNotifications] : [[UIApplication sharedApplication] unregisterForRemoteNotifications];
}

@end

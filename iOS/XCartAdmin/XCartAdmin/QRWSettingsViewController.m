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

@property(nonatomic, weak) IBOutlet UISwitch *passwordSwitch;

@property(nonatomic, weak) IBOutlet UISwitch *pushNotificationsSwitch;

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
    
    [self.passwordSwitch setOn:[(NSNumber *)[[NSUserDefaults standardUserDefaults] objectForKey:@"QRW_PINenabled"] boolValue]];
    [self.pushNotificationsSwitch setOn:[[UIApplication sharedApplication] isRegisteredForRemoteNotifications]];
}

- (IBAction)passwordSwitchAction:(UISwitch *)passwordSwitch
{
    [[NSUserDefaults standardUserDefaults] setObject:[NSNumber numberWithBool:passwordSwitch.isOn] forKey:@"QRW_PINenabled"];
    
    if (passwordSwitch.isOn) {
        [[QRWUnlockViewController sharedInstance] showUnlockViewOnViewController:self editPasswordMode:YES];
    }
}

- (IBAction)pushNotificationsSwitchAction:(UISwitch *)pushNotificationsSwitch
{
    pushNotificationsSwitch.isOn ? [[UIApplication sharedApplication] registerForRemoteNotifications] : [[UIApplication sharedApplication] unregisterForRemoteNotifications];
}

@end

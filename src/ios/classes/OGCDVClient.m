//  Copyright © 2016 Onegini. All rights reserved.

#import <Cordova/CDVViewController.h>
#import "OGCDVClient.h"
#import "OGCDVConstants.h"
#import "OneginiConfigModel.h"
#import "OGCDVMobileAuthenticationClient.h"
#import "OGCDVMobileAuthenticationRequestClient.h"

@implementation OGCDVClient {
}

- (void)pluginInitialize
{
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(applicationDidFinishLaunchingNotification:)
                                                 name:UIApplicationDidFinishLaunchingNotification object:nil];
}

- (void)applicationDidFinishLaunchingNotification:(NSNotification *)notification
{
    self.launchNotificationUserInfo = notification.userInfo[UIApplicationLaunchOptionsRemoteNotificationKey];
}

- (void)start:(CDVInvokedUrlCommand *)command
{
    [self.commandDelegate runInBackground:^{
        [[[ONGClientBuilder new] build] start:^(BOOL result, NSError *error) {
            if (error != nil) {
                [self sendErrorResultForCallbackId:command.callbackId withError:error];
                return;
            }

            OGCDVMobileAuthenticationClient *mobileAuthClient = [(CDVViewController *)self.viewController getCommandInstance:OGCDVPluginClassMobileAuthenticationClient];
            if (mobileAuthClient.pendingDeviceToken != nil) {
                [[ONGUserClient sharedInstance] storeDevicePushTokenInSession:mobileAuthClient.pendingDeviceToken];
                mobileAuthClient.pendingDeviceToken = nil;
            }
            NSDictionary *config = @{OGCDVPluginKeyResourceBaseURL: OneginiConfigModel.configuration[ONGResourceBaseURL]};
            [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:config] callbackId:command.callbackId];
            [self handleLaunchNotification];
        }];
    }];
}

- (void)handleLaunchNotification
{
    NSDictionary *userInfo = self.launchNotificationUserInfo;

    if (userInfo != nil) {
        OGCDVMobileAuthenticationRequestClient *mobileAuthenticationRequestClient = [(CDVViewController *)self.viewController getCommandInstance:OGCDVPluginClassMobileAuthenticationRequestClient];
        [[ONGUserClient sharedInstance] handleMobileAuthenticationRequest:userInfo delegate:mobileAuthenticationRequestClient];
    }
}

@end

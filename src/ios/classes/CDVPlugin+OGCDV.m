//  Copyright © 2016 Onegini. All rights reserved.

#import "CDVPlugin+OGCDV.h"
#import "OGCDVConstants.h"

@implementation CDVPlugin (OGCDV)

- (void)sendErrorResultForCallbackId:(NSString *)callbackId withMessage:(NSString *)errorMessage
{
    [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsDictionary:@{OGCDVPluginKeyErrorDescription: errorMessage}]
                                callbackId:callbackId];
}

- (void)sendErrorResultForCallbackId:(NSString *)callbackId withErrorCode:(int)code andMessage:(NSString *)errorMessage
{
    NSDictionary *result = @{
        OGCDVPluginKeyErrorCode: @(code),
        OGCDVPluginKeyErrorDescription: errorMessage
    };
    [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsDictionary:result] callbackId:callbackId];
}

- (void)sendErrorResultForCallbackId:(NSString *)callbackId withError:(NSError *)error
{
    NSString *errorMessage;
    int errorCode;
    if (error == nil) {
        errorMessage = OGCDVPluginErrInternalError;
        errorCode = OGCDVPluginErrCodePluginInternalError;
    } else {
        errorMessage = [NSString stringWithFormat:@"%@\n%@", error.localizedDescription, error.localizedRecoverySuggestion];
        errorCode = error.code;
    }
    [self sendErrorResultForCallbackId:callbackId withErrorCode:errorCode andMessage:errorMessage];
}

@end

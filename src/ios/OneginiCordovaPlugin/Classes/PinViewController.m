//
//  PinViewController.m
//  OneginiCordovaPlugin
//
//  Created by Stanisław Brzeski on 19/01/16.
//  Copyright © 2016 Onegini. All rights reserved.
//

#import "PinViewController.h"
#import "PopupViewController.h"
#import "MessagesModel.h"
#import "OGNColorFileParser.h"

@interface PinViewController ()

@property (nonatomic) NSArray *pin;
@property (nonatomic) NSArray *pinSlots;
@property (nonatomic) NSMutableArray *pinEntry;

@property (weak, nonatomic) IBOutlet UILabel *titleLabel;
@property (weak, nonatomic) IBOutlet UILabel *errorLabel;
@property (weak, nonatomic) IBOutlet UIButton *helpButton;
@property (weak, nonatomic) IBOutlet UIButton *pinForgottenButton;
@property (weak, nonatomic) IBOutlet UIView *headerView;
@property (weak, nonatomic) IBOutlet UIView *backgroundView;
@property (weak, nonatomic) IBOutlet UIView *pinBackgroundView;

@property (nonatomic) UIActivityIndicatorView *spinner;
@property (nonatomic) PopupViewController *popupViewController;

@property (weak, nonatomic) IBOutlet UIButton *key1;
@property (weak, nonatomic) IBOutlet UIButton *key2;
@property (weak, nonatomic) IBOutlet UIButton *key3;
@property (weak, nonatomic) IBOutlet UIButton *key4;
@property (weak, nonatomic) IBOutlet UIButton *key5;
@property (weak, nonatomic) IBOutlet UIButton *key6;
@property (weak, nonatomic) IBOutlet UIButton *key7;
@property (weak, nonatomic) IBOutlet UIButton *key8;
@property (weak, nonatomic) IBOutlet UIButton *key9;
@property (weak, nonatomic) IBOutlet UIButton *key0;
@property (weak, nonatomic) IBOutlet UIButton *backKey;

@property (weak, nonatomic) IBOutlet UIImageView *pinSlot1;
@property (weak, nonatomic) IBOutlet UIImageView *pinSlot2;
@property (weak, nonatomic) IBOutlet UIImageView *pinSlot3;
@property (weak, nonatomic) IBOutlet UIImageView *pinSlot4;
@property (weak, nonatomic) IBOutlet UIImageView *pinSlot5;
@property (weak, nonatomic) IBOutlet UIImageView *pin1;
@property (weak, nonatomic) IBOutlet UIImageView *pin2;
@property (weak, nonatomic) IBOutlet UIImageView *pin3;
@property (weak, nonatomic) IBOutlet UIImageView *pin4;
@property (weak, nonatomic) IBOutlet UIImageView *pin5;

@end

@implementation PinViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
    self.pinEntry = [NSMutableArray new];
    [self.helpButton setTitle:[MessagesModel messageForKey:@"HELP_LINK_TITLE"] forState:UIControlStateNormal];
    [self.pinForgottenButton setTitle:[MessagesModel messageForKey:@"PIN_FORGOTTEN_TITLE"] forState:UIControlStateNormal];
    [self setColors];
    [self hideBackKey];
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    self.mode = self.mode;
    [self initPopupViewController];
}

- (void)setColors
{
    self.headerView.backgroundColor = [OGNColorFileParser colorForKey:kOGNPinscreenHeaderBackground];
    [self.helpButton setTitleColor:[OGNColorFileParser colorForKey:kOGNPinscreenHeaderHelpLabelText] forState:UIControlStateNormal];
    self.titleLabel.textColor = [OGNColorFileParser colorForKey:kOGNPinscreenTitle];
    [self.pinForgottenButton setTitleColor:[OGNColorFileParser colorForKey:kOGNPinscreenForgottenLabel] forState:UIControlStateNormal];
    self.backgroundView.backgroundColor = [OGNColorFileParser colorForKey:kOGNPinscreenBackground];
    self.pinBackgroundView.backgroundColor = [OGNColorFileParser colorForKey:kOGNPinKeyboardBackground];

    for (UIButton *keyboardButton in @[self.key1, self.key2, self.key3, self.key4, self.key5, self.key6, self.key7, self.key8, self.key9, self.key0, self.backKey]) {
        keyboardButton.backgroundColor = [OGNColorFileParser colorForKey:kOGNPinKeyboardButtonBackground];
        [keyboardButton setTitleColor:[OGNColorFileParser colorForKey:kOGNPinKeyboardButtonText] forState:UIControlStateNormal];
    }
}

- (void)invalidPinWithReason:(NSString *)message
{
    [self reset];

    self.errorLabel.text = message;
}

- (NSArray *)pin
{
    if (_pin == nil)
        _pin = @[self.pin1, self.pin2, self.pin3, self.pin4, self.pin5];
    return _pin;
}

- (NSArray *)pinSlots
{
    if (_pinSlots == nil)
        _pinSlots = @[self.pinSlot1, self.pinSlot2, self.pinSlot3, self.pinSlot4, self.pinSlot5];
    return _pinSlots;
}

- (void)setMode:(PINEntryModes)mode
{
    _mode = mode;
    switch (mode) {
        case PINCheckMode:
            self.pinForgottenButton.hidden = NO;
            self.titleLabel.text = [MessagesModel messageForKey:@"LOGIN_PIN_SCREEN_TITLE"];
            self.errorLabel.text = @"";
            break;
        case PINRegistrationMode:
            self.pinForgottenButton.hidden = YES;
            self.titleLabel.text = [MessagesModel messageForKey:@"CREATE_PIN_SCREEN_TITLE"];
            self.errorLabel.text = @"";
            break;
        case PINRegistrationVerififyMode:
            self.pinForgottenButton.hidden = YES;
            self.titleLabel.text = [MessagesModel messageForKey:@"CONFIRM_PIN_SCREEN_TITLE"];
            self.errorLabel.text = @"";
            break;
        case PINChangeCheckMode:
            self.pinForgottenButton.hidden = NO;
            self.titleLabel.text = [MessagesModel messageForKey:@"LOGIN_BEFORE_CHANGE_PIN_SCREEN_TITLE"];
            self.errorLabel.text = @"";
            break;
        case PINChangeNewPinMode:
            self.pinForgottenButton.hidden = YES;
            self.titleLabel.text = [MessagesModel messageForKey:@"CHANGE_PIN_SCREEN_TITLE"];
            self.errorLabel.text = @"";
            break;
        case PINChangeNewPinVerifyMode:
            self.pinForgottenButton.hidden = YES;
            self.titleLabel.text = [MessagesModel messageForKey:@"CONFIRM_CHANGE_PIN_SCREEN_TITLE"];
            self.errorLabel.text = @"";
            break;
        case PINFingerprintCheckMode:
            self.pinForgottenButton.hidden = NO;
            self.titleLabel.text = [MessagesModel messageForKey:@"LOGIN_BEFORE_FINGERPRINT_ENROLLMENT_TITLE"];
            self.errorLabel.text = @"";
            break;
        default:
            self.titleLabel.text = @"";
            self.errorLabel.text = @"";
            break;
    }
}

- (IBAction)keyPressed:(UIButton *)key
{
    if (self.pinEntry == nil) {
        self.pinEntry = [NSMutableArray new];
    }

    if (self.pinEntry.count >= self.pin.count) {
#ifdef DEBUG
        NSLog(@"max entries PIN reached");
#endif
        return;
    }

    [self.pinEntry addObject:[NSString stringWithFormat:@"%ld", (long)key.tag]];

    [self evaluatePinState];
}

- (IBAction)backKeyPressed:(id)sender
{
    [self.pinEntry removeLastObject];

    [self updatePinStateRepresentation];
}

- (void)evaluatePinState
{
    [self updatePinStateRepresentation];

    if (self.pinEntry.count == self.pin.count) {
        [self showProgressIndicator];
        [self.delegate pinEntered:[self.pinEntry componentsJoinedByString:@""]];
    }
}

- (void)showProgressIndicator
{
    self.spinner = [[UIActivityIndicatorView alloc] initWithFrame:CGRectMake(0.0, 0.0, 40.0, 40.0)];
    self.spinner.activityIndicatorViewStyle = UIActivityIndicatorViewStyleGray;
    self.spinner.center = self.view.center;
    [self.spinner startAnimating];
    [self.view addSubview:_spinner];
}

- (void)hideProgressIndicator
{
    if (self.spinner) {
        dispatch_async(dispatch_get_main_queue(), ^{
            [self.spinner removeFromSuperview];
        });
    }
}

- (void)reset
{
    for (int i = 0; i < self.pinEntry.count; i++) {
        self.pinEntry[i] = @"#";
    }
    self.pinEntry = [NSMutableArray new];
    [self updatePinStateRepresentation];
    [self hideProgressIndicator];
}

- (void)updatePinStateRepresentation
{
    for (int i = 0; i < self.pin.count; i++) {
        UIView *pin = self.pin[i];
        [UIView animateWithDuration:0.2 animations:^{
            pin.alpha = i < self.pinEntry.count ? 1.0 : 0.0;
        }];
    }
    for (UIImageView *pinslot in self.pinSlots) {
        pinslot.image = [UIImage imageNamed:@"pinslot"];
    }
    if (self.pinEntry.count < self.pinSlots.count) {
        ((UIImageView *)[self.pinSlots objectAtIndex:self.pinEntry.count]).image = [UIImage imageNamed:@"pinslotSelected"];
    }

    if (self.pinEntry.count == 0) {
        [self hideBackKey];
    } else {
        self.backKey.alpha = 1;
        self.backKey.enabled = YES;
    }
}

- (void)hideBackKey
{
    self.backKey.alpha = 0;
    self.backKey.enabled = NO;
}

- (void)initPopupViewController
{
    self.popupViewController = [[PopupViewController alloc] initWithNibName:@"PopupViewController" bundle:nil];
    self.popupViewController.view.layer.masksToBounds = NO;
    self.popupViewController.view.layer.shadowRadius = 50;
    self.popupViewController.view.layer.shadowColor = [[UIColor colorWithWhite:0 alpha:1] CGColor];
    self.popupViewController.view.layer.shadowOpacity = 0.5;
}

- (void)centerPopupView
{
    if (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPad) {
        int popupWidth = 740;
        int popupHeight = 380;
        self.popupViewController.view.frame = CGRectMake(self.view.bounds.size.width / 2 - popupWidth / 2, self.view.bounds.size.height / 2 - popupHeight / 2, popupWidth, popupHeight);
    } else {
        self.popupViewController.view.frame = CGRectMake(self.view.bounds.size.width / 2 - self.popupViewController.view.frame.size.width / 2, self.view.bounds.size.height / 2 - MIN(self.popupViewController.view.frame.size.height, self.view.bounds.size.height) / 2, self.popupViewController.view.frame.size.width, MIN(self.popupViewController.view.frame.size.height, self.view.bounds.size.height));
    }
}

- (void)showPopupView
{
    [self addChildViewController:self.popupViewController];
    [self centerPopupView];
    [self.view addSubview:self.popupViewController.view];
    [self.popupViewController didMoveToParentViewController:self];
}

- (void)closePopupView
{
    [self.popupViewController willMoveToParentViewController:nil];
    [self.popupViewController.view removeFromSuperview];
    [self.popupViewController removeFromParentViewController];
}

- (IBAction)helpButtonClicked:(id)sender
{
    PopupViewController *popupViewController = self.popupViewController;

    switch (self.mode) {
        case PINCheckMode:
            popupViewController.titleLabel.text = [MessagesModel messageForKey:@"LOGIN_PIN_HELP_TITLE"];
            [self.popupViewController setPopupMessage:[MessagesModel messageForKey:@"LOGIN_PIN_HELP_MESSAGE"]];
            break;
        case PINRegistrationMode:
            popupViewController.titleLabel.text = [MessagesModel messageForKey:@"CREATE_PIN_HELP_TITLE"];
            [self.popupViewController setPopupMessage:[MessagesModel messageForKey:@"CREATE_PIN_HELP_MESSAGE"]];
            break;
        case PINRegistrationVerififyMode:
            popupViewController.titleLabel.text = [MessagesModel messageForKey:@"CONFIRM_PIN_HELP_TITLE"];
            [self.popupViewController setPopupMessage:[MessagesModel messageForKey:@"CONFIRM_PIN_HELP_MESSAGE"]];
            break;
        case PINChangeCheckMode:
            popupViewController.titleLabel.text = [MessagesModel messageForKey:@"LOGIN_BEFORE_CHANGE_PIN_HELP_TITLE"];
            [self.popupViewController setPopupMessage:[MessagesModel messageForKey:@"LOGIN_BEFORE_CHANGE_PIN_HELP_MESSAGE"]];
            break;
        case PINChangeNewPinMode:
            popupViewController.titleLabel.text = [MessagesModel messageForKey:@"CHANGE_PIN_HELP_TITLE"];
            [self.popupViewController setPopupMessage:[MessagesModel messageForKey:@"CHANGE_PIN_HELP_MESSAGE"]];
            break;
        case PINChangeNewPinVerifyMode:
            popupViewController.titleLabel.text = [MessagesModel messageForKey:@"CONFIRM_CHANGE_PIN_HELP_TITLE"];
            [self.popupViewController setPopupMessage:[MessagesModel messageForKey:@"CONFIRM_CHANGE_PIN_HELP_MESSAGE"]];
            break;
        case PINFingerprintCheckMode:
            popupViewController.titleLabel.text = [MessagesModel messageForKey:@"LOGIN_BEFORE_FINGERPRINT_ENROLLMENT_HELP_TITLE"];
            [self.popupViewController setPopupMessage:[MessagesModel messageForKey:@"LOGIN_BEFORE_FINGERPRINT_ENROLLMENT_HELP_MESSAGE"]];
            break;
        default:
            break;
    }
    [popupViewController.proceedButton setTitle:[MessagesModel messageForKey:@"HELP_POPUP_OK"] forState:UIControlStateNormal];
    [self showPopupView];

    popupViewController.cancelButtonVisible = NO;

    __weak PinViewController *weakSelf = self;
    popupViewController.proceedBlock = ^{
        [weakSelf closePopupView];
    };
    popupViewController.cancelBlock = ^{
        [weakSelf closePopupView];
    };
    popupViewController.closeBlock = ^{
        [weakSelf closePopupView];
    };
}

- (IBAction)forgotPinClicked:(id)sender
{
    self.popupViewController.titleLabel.text = [MessagesModel messageForKey:@"DISCONNECT_FORGOT_PIN_TITLE"];
    [self.popupViewController setPopupMessage:[MessagesModel messageForKey:@"DISCONNECT_FORGOT_PIN"]];
    [self.popupViewController.proceedButton setTitle:[MessagesModel messageForKey:@"CONFIRM_POPUP_OK"] forState:UIControlStateNormal];
    [self.popupViewController.cancelButton setTitle:[MessagesModel messageForKey:@"CONFIRM_POPUP_CANCEL"] forState:UIControlStateNormal];
    [self showPopupView];

    self.popupViewController.cancelButtonVisible = YES;

    __weak PinViewController *weakSelf = self;
    self.popupViewController.proceedBlock = ^{
        [weakSelf closePopupView];
        [weakSelf.delegate pinForgotten];
    };
    self.popupViewController.cancelBlock = ^{
        [weakSelf closePopupView];
    };
    self.popupViewController.closeBlock = ^{
        [weakSelf closePopupView];
    };
}

- (NSUInteger)supportedInterfaceOrientations
{
    return self.supportedOrientations;
}

@end

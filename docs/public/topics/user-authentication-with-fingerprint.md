# User authentication with fingerprint

<!-- toc -->

## Introduction

The Onegini Cordova plugin allows you to authenticate users with the fingerprint scanner, if one is available on the device. Fingerprint can be used for both regular authentication as well as mobile authentication. Users will be able to scan their fingerprint as many times as the OS will allow them to. If the OS fingerprint API returns an error for any reason (for example in the case of too many failed attempts), the Onegini Cordova plugin revokes fingerprint authentication and performs a fallback to PIN authentication.

### Differences between Android and iOS

It should be noted that there are significant differences between Fingerprint on Android and Touch ID on iOS. As a result, some methods may be available on only one of the operating systems. This will be specified where applicable.

## Enabling fingerprint authentication

In order to enable fingerprint authentication for a user, the Onegini Cordova plugin provides the [`onegini.user.authenticators.registerNew`](../reference/user/authenticators.md#oneginiuserauthenticatorsregisternew). This function requires the user to authenticate. As a result, it is necessary to implement the UI associated with the `onPinRequest` method, in addition to the `onSuccess` and `onError` methods.

**Example code for registering the fingerprint authenticator:**

```js
onegini.user.authenticators.registerNew({ authenticatorType: "Fingerprint" })
    .onPinRequest((actions, options) => {
      var pin = prompt("Please enter your PIN");
      actions.providePin(pin);
    })
    .onSuccess(() => {
      alert("Success!");
    })
    .onError((err) => {
      alert("Error!\n\n" + err.description);
    });
```

Fingerprint authentication may not be available on every device. In this case, or if the authenticator has already been registered, the above method will return an error.

To request a list of available authenticators that have not yet been registered, the plugin exposes the [`onegini.user.authenticators.getNotRegistered`](../reference/user/authenticators.md#oneginiuserauthenticatorsgetnotregistered) function, which takes the `profileId` of the desired user as argument. If the device does not meet the fingerprint requirements, the fingerprint authenticator will not be present in the returned array of of authenticators.

Note that registering a new authenticator does not set it as the preferred authenticator for the user, which is PIN by default. To change this, [`onegini.user.authenticators.setPreferred`](../reference/user/authenticators#oneginiuserauthenticatorssetpreferred) can be used.

**Example code to set fingerprint as the preferred authenticator:**

```js
onegini.user.authenticators.setPreferred({ authenticatorType: "Fingerprint" })
    .then(() => {
      alert("Fingerprint set as preferred authenticator!");
    })
    .catch((err) => {
      alert("Error!\n\n" + err.description);
    });
```

## Authenticating a user with fingerprint

Once the fingerprint authenticator has been registered and set as the preferred authententicator, the user is able to authenticate using fingerprint. The method to do so is the same as for PIN, the [`onegini.user.authenticate`](../reference/user/authenticate.md) method.

However, if fingerprint authentication is a possibility for the user, extra handler methods must be implemented. This is in addition to the PIN specific methods (which are necessary in case of fallback to PIN).

**Example code to log in a user with fingerprint:**

```js
onegini.user.authenticate({ profileId: "profileIdOfUser" })
    .onPinRequest((actions, options) => {
      var pin = prompt("Please enter your PIN");
      actions.providePin(pin);
    })
    .onFingerprintRequest((actions) => {
      alert("Accepting fingerprint authentication request");
      actions.acceptFingerprint({ iosPrompt: "Log in to Cordova Example App" });
    })
    .onFingerprintCaptured(() => {
      alert("Fingerprint captured! Waiting for verification.");
    })
    .onFingerprintFailed(() => {
      alert("Incorrect fingerprint! Please try again.");
    })
    .onSuccess(() => {
      alert("Authentication success!");
    })
    .onError(() => {
      alert("Authentication error!\n\n" + err.description);
    });
```

Note that `onFingerprintCaptured` and `onFingerprintFailed` will only be called on Android, as Touch ID on iOS does not emit these events.

If the user fails to authenticate using fingerprint too many times (this limit is set by the OS), the fingerprint authenticator is automatically deregistered and the relevant tokens are revoked by the Onegini Cordova plugin. At this point, a fallback to PIN is performed, and the user is request to enter their PIN via `onPinRequest`. If this too, fails, `onError` will be called, signalling the authentication has failed.

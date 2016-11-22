# Authentication Handler

<!-- toc -->

## Introduction

When a user is required to Authenticate for certain methods, the Onegini Cordova Plugin will return an `AuthenticationHandler`.
This object can be used to register callbacks which will be called when certain authentication steps are required.

Example: when a user's PIN is required, the `onPinRequest` callback will be called.
```js
let handler = onegini.user.authenticate({
  profileId: "W8DUJ2"
});

handler.onPinrequest((actions, options) => {
  // Called when the SDK requests a users PIN.
});
```

The moment `onPinRequest` is called is the time to ask a user for his PIN.
After the user has entered his PIN, you can supply it to the SDK by calling an `action`, in this case `providePin`.
This simple code will use the `prompt` to as for the PIN.

```js
handler.onPinRequest((actions, options) => {
  let pin = prompt("Please enter your PIN. You have " + options.remainingFailureCount + " attempts remaining");
  actions.providePin(pin);
})
```

If the PIN entered is incorrect the `onPinRequest` handler will be called with a new request.
The `options` parameters contains additional parameters like max allowed entry attempts and remaining attempts.
If the PIN is correct, the `onSuccess` handler will be called.

```js
handler.onSuccess(() => {
  alert("Authentication success!");
});
```

## Methods

The following methods can registered to handle different authentication events:
- [`onPinRequest`](#onpinrequest)
- [`onCreatePinRequest`](#oncreatepinrequest)
- [`onFingerprintRequest`](#onfingerprintrequest)
- [`onFingerprintCaptured`](#onfingerprintcaptured)
- [`onFingerprintFailed`](#onfingerprintfailed)
- [`onSuccess`](#onsuccess)
- [`onError`](#onerror)

### `onPinRequest`
This method is called when the user's PIN is required. The pin can be supplied using `actions.providePin`

```js
handler.onPinRequest((actions, options) => {
  actions.providePin('12346');
});
```

### `onCreatePinRequest`
This method is called when a new PIN needs to be created. The new PIN can be created with `actions.createPin`

```js
handler.onCreatePinRequest((actions, options) => {
  console.log("Creating a new PIN with of length " + options.pinLength);
  actions.createPin('12346');
});
```

### `onFingerprintRequest`

This method is called when the user is required to be authenticated with fingerprint.
The following three actions can be called in response:
- acceptFingerprint. _Start reading fingerprint sensor_
- denyFingerprint. _Cancel this authentication attempt_ 
- fallbackToPin. _Ask for a PIN instead, you will receive a PIN request._

The `acceptFingerprint` actions can take and object with an `iosPrompt` value to display on the TouchID prompt.

```js
handler.onFingerprintRequest((actions) => {
  console.log("Accepting fingerprint request");
  actions.acceptFingerprint({ iosPrompt: 'Login to myApp' });
});
```

### `onFingerprintCaptured`

This method is called when the fingerprint sensor detects a finger is placed on the sensor.
It is not yet determined if the fingerprint is correct.
This method can be used to provide feedback to your users so they are aware the device has detected their finger.

Note: this method is never called on iOS. TouchID on iOS uses it's own native UI to provide feedback to users.

```js
handler.onFingerprintCaptured(() => {
  console.log("Fingerprint captured");
});
```

### `onFingerprintFailed`

This method is called when the fingerprint previously detected was not recognized as the correct fingerprint.
This method can be used to provide feedback to your users so the are aware the device did not recognize their fingerprint.
```js
handler.onFingerprintCaptured(() => {
  console.log("Fingerprint incorrect!");
});
```

### `onSuccess`
This method is called when the user has been successfully authenticated. You now perform actions that require authentication.

```js
handler.onSuccess(() => {
  console.log("You have been successfully authenticated!");
});
```

### `onError`

This method is called when the authentication has failed.
The failure can be due to several errors like failed to connect to network or when the user has exceeded the maximum attempts at entering a PIN and has been deregistered.

```js
handler.onError((err) => {
  console.log("Something went wrong while trying to authenticate! ", err);
})
```

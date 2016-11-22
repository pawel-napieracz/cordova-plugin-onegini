# Authentication Handler

## Overview

When a user is required to Authenticate for certain methods, the Onegini Cordova Plugin will return an `AuthenticationHandler`.
This object can be used to register callbacks which will be called when certain authentication steps are required.

Example: when a users PIN code is required, the `onPinRequest` callback will be called.
```js
let handler = onegini.user.authenticate({
  profileId: "W8DUJ2"
});

handler.onPinrequest((actions, options) => {
  // Called when the SDK requests a users pincode.
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
If the PIN code is correct, the `onSuccess` handler will be called.

```js
handler.onSuccess(() => {
  alert("Authentication success!");
});
```

## Methods

The following methods can be called when certain events occur:

- onPinRequest
- onCreatePinRequest
- onFingerprintRequest
- onFingerprintCaptured
- onFingerprintFailed
- onError

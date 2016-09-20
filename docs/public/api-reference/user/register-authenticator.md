# Register authenticator

You can register additional authenticators for the currently logged in user. So when the user is logged in and `onegini.user.authenticators.getNotRegistered` indicates additional authenticators are available, you can register them with this function. 

Similar to authentication this function is a two-step process.

## `onegini.user.authenticators.registerNew`

This function takes a mandatory first argument with the following properties:

| Property | Default | Description |
| --- | --- | --- |
| `authenticatorId` | - | The Authenticator Id as received from `onegini.user.authenticators.getNotRegistered`

```js
onegini.user.authenticators.registerNew(
  {
    authenticatorId: "com.onegini.authenticator.FINGERPRINT"
  },

  // success callback
  function () {
    console.log("The provided Authenticator Id can be registered");
  },

  // error callback
  function (err) {
    console.log("Error: " + err.description);
  }
);
```

The error callback contains an object with these properties:

| Property | Example | Description |
| --- | --- | --- |
| `code` | 9001 | The error code
| `description` | "Invalid Pin" | Human readable error description

## `onegini.user.authenticators.providePin`

After the success callback of `onegini.user.authenticators.registerNew` has been invoked and a Pin code is required, ask the user the Pin he previously configured. Once done send this Pin to this function: 

| Property | Default | Description |
| --- | --- | --- |
| `pin` | - | The Pin the user previously configured

```js
onegini.user.authenticators.providePin(
  {
    pin: "28649"
  },

  // success callback
  function () {
    console.log("Authenticator successfully registered");
  },

  // error callback
  function (err) {
    console.log("Error: " + err.description);
  }
);
```

The error callback contains an object with these properties:

| Property | Example | Description |
| --- | --- | --- |
| `code` | 9001 | The error code
| `description` | "Invalid Pin" | Human readable error description
| `maxFailureCount ` | 3 | The maximum amount of consecutive Pin failures
| `remainingFailureCount ` | 1 | The amount of remaining Pin failures

# Reuthentication

In cases where you want to reassure the user is who you expect him to be, a reauthentication can be triggered.

Similar to regular authentication this function is a two-step process. In fact, the implementation is near identical so this should look very familiar if you already know how to authenticate a user.

## `onegini.user.reauthenticate.start`

This function takes a mandatory first argument with the following properties:

| Property | Default | Description |
| --- | --- | --- |
| `profileId` | - | The Profile Id you previously "remembered" during registration

```js
onegini.user.reauthenticate.start(
  {
    profileId: "W8DUJ2"
  },

  // success callback
  function () {
    console.log("The provided Profile Id is allowed to reauthenticate");
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

## `onegini.user.reauthenticate.providePin`

After the success callback of `onegini.user.reauthenticate.start` has been invoked and a Pin code is required, ask the user the Pin he previously configured. Once done send this Pin to this function: 

| Property | Default | Description |
| --- | --- | --- |
| `pin` | - | The Pin the user previously configured

```js
onegini.user.reauthenticate.providePin(
  {
    pin: "28649"
  },

  // success callback
  function () {
    console.log("User successfully reauthenticated");
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

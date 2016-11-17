# Authentication

The end-user can authenticate himself using any registered authenticators, for instance by entering his PIN. As the developer, however, you will need to take two steps to complete this process. The first is to notify the SDK that you want to authenticate a certain profile ID. Based on the response, you will need to ask the user to authenticate via the appropriate method.

<!-- TODO verify correctness, also: in case nr of remaining attempts is available in the successCb we could document that in the example and an additional reason for this two-step approach -->

This two-step approach is necessary since the user may have multiple means of authentication, and you need to show the correct UI. Additional, the user's profile may have been invalidated serverside, so showing an authentication dialog is not the best next step in that case.

## `onegini.user.authenticate.start`

This function takes a mandatory first argument with the following properties:

| Property | Default | Description |
| --- | --- | --- |
| `profileId` | - | The profile ID you previously stored during registration

```js
onegini.user.authenticate.start(
  {
    profileId: "W8DUJ2"
  },

  // success callback
  function () {
    console.log("The provided Profile Id is allowed to authenticate");
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

## `onegini.user.authenticate.providePin`

After the success callback of `onegini.user.authenticate.start` has been invoked and PIN authentication is required, ask the user to provide the PIN he previously configured. Once done send the PIN to this function: 

| Property | Default | Description |
| --- | --- | --- |
| `pin` | - | The Pin the user previously configured

```js
onegini.user.authenticate.providePin(
  {
    pin: "28649"
  },

  // success callback
  function () {
    console.log("User successfully authenticated");
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

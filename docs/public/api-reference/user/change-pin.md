# Change Pin

Once authenticated, a user is able to change his PIN code.

It is recommended to implement this process in two steps: first request and validate the currently configured PIN, then request and validate the new PIN. To accomodate this the plugin provides the two following functions.

## `onegini.user.changePin.start`

This function takes a mandatory first argument with the following properties:

| Property | Default | Description |
| --- | --- | --- |
| `pin` | - | The currently configured PIN

```js
onegini.user.changePin.start(
  {
    pin: "28649"
  },

  // success callback
  function () {
    console.log("The current Pin was correctly entered");
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

## `onegini.user.changePin.createPin`

After the success callback of `onegini.user.changePin.start ` has been invoked and a Pin code is required, ask the user the Pin he wants to configure. Once done send this Pin to this function: 

| Property | Default | Description |
| --- | --- | --- |
| `pin` | - | The new Pin the user wants to configure

```js
onegini.user.changePin.createPin(
  {
    pin: "93621"
  },

  // success callback
  function () {
    console.log("Pin successfully changed");
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

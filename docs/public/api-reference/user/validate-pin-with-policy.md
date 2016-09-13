# Validate Pin with Policy

At any moment in the app's lifecycle you can check whether or not a certain Pin code meets the policy you've configured server side.

## `onegini.user.validatePinWithPolicy`

This function takes a mandatory first argument with the following properties:

| Property | Default | Description |
| --- | --- | --- |
| `pin` | - | The Pin the user wants to verify agains the policy

The success callback will be invoked if the Pin is valid according to the policy. The error callback is invoked otherwise, which the usual error code and description explaining why it's not a valid Pin code.

```js
onegini.user.validatePinWithPolicy(
  {
    pin: "28649"
  },
  
  // success callback
  function () {
    console.log("The Pin is OK");
  },
  
  // error callback
  function (err) {
    // the Pin is invalid
    console.log("Error: " + err.description);
  }
);
```

The error callback contains an object with these properties:

| Property | Example | Description |
| --- | --- | --- |
| `code` | 9001 | The error code
| `description` | "Invalid Pin" | Human readable error description

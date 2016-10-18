# Set Preferred Authenticator

In case a user has multiple authenticators (PIN, Fingerprint, ..) you can have him indicate which one he prefers by using this function:

## `onegini.user.authenticators.setPreferred`

This function takes a mandatory first argument with the following properties:

| Property | Default | Description |
| --- | --- | --- |
| `authenticatorId` | - | The Authenticator Id as received from `onegini.user.authenticators.getNotRegistered`

```js
onegini.user.authenticators.setPreferred(
  {
    authenticatorId: "com.onegini.authenticator.FINGERPRINT"
  },

  // success callback
  function () {
    console.log("The provided Authenticator Id is now the preferred authenticator");
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

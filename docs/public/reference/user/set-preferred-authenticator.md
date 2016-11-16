# Set Preferred Authenticator

When a user has multiple registered authenticators (PIN, fingerprint, ...), you may want to allow them to choose which authenticator they prefer.

## `onegini.user.authenticators.setPreferred`

This function takes a mandatory first argument with the following properties:

| Property | Example | Description |
| --- | --- | --- |
| `authenticatorType` | "Fingerprint" | The authenticator type as received from `onegini.user.authenticators.getRegistered`

```js
onegini.user.authenticators.setPreferred(
  // the authenticator type the user would like to set as
  // preferred
  {
    authenticatorType: "Fingerprint"
  },

  // success callback
  function () {
    console.log("The provided authenticator ID is now the preferred authenticator");
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
| `code` | 8005 | The error code
| `description` | "Onegini: No user authenticated." | Human readable error description

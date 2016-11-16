# Get Preferred Authenticator

When a user has multiple registered authenticators (PIN, fingerprint, ...), you may need to retrieve the authenticator set as preferred.

## `onegini.user.authenticators.getPreferred`

```js
onegini.user.authenticators.getPreferred(
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

The success callback contains an object with these properties:

| Property | Example | Description |
| --- | --- | --- |
| `authenticatorType` | "PIN" | The authenticator type
| `authenticatorId` | "PIN" | The authenticator ID, which distinguishes between authenticators of type "Custom"

The error callback contains an object with these properties:

| Property | Example | Description |
| --- | --- | --- |
| `code` | 8005 | The error code
| `description` | "Onegini: No user authenticated." | Human readable error description

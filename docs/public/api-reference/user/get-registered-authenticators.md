# Get Registered Authenticators

A user may have registered multiple authenticators (Pin, Fingerprint, ..). This function offers a way to retrieve them:

## `onegini.user.authenticators.getRegistered`

```js
onegini.user.authenticators.getRegistered(
  // success callback
  function (result) {
    for (var r in result) {
      var auth = result[r];
      console.log("Authenticator: " + auth.authenticatorId);
    }
  },

  // error callback
  function (err) {
    console.log("Error: " + err.description);
  }
);
```

The success callback contains an _Array of objects_ with these properties:

| Property | Example | Description |
| --- | --- | --- |
| `authenticatorId` | - | An Authenticator Id associated with the user

The error callback contains an object with these properties:

| Property | Example | Description |
| --- | --- | --- |
| `code` | 9001 | The error code
| `description` | "Invalid Pin" | Human readable error description

# Get Not Registered Authenticators

A user may want to register additional authenticators (Fingerprint, FIDO, ..). This function offers a way to retrieve the list of available authenticators:

## `onegini.user.authenticators.getNotRegistered`

```js
onegini.user.authenticators.getNotRegistered(
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
| `authenticatorId` | - | An Authenticator Id not associated with the user

The error callback contains an object with these properties:

| Property | Example | Description |
| --- | --- | --- |
| `code` | 9001 | The error code
| `description` | "Invalid Pin" | Human readable error description

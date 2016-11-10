# Get All Authenticators

This function offers a way to retrieve all available authenticators.

## `onegini.user.authenticators.getAll`

```js
onegini.user.authenticators.getAll(
  {
    profileId: "W8DUJ2"
  },

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

The success callback contains an _array of objects_ with these properties:

| Property | Example | Description |
| --- | --- | --- |
| `authenticatorId` | - | An authenticator ID associated with the authenticator

The error callback contains an object with these properties:

| Property | Example | Description |
| --- | --- | --- |
| `code` | 9001 | The error code
| `description` | "Invalid Pin" | Human readable error description

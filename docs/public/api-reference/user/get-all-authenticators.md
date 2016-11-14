# Get All Authenticators

This function offers a way to retrieve all available authenticators for a given user.

## `onegini.user.authenticators.getAll`

This function takes a mandatory first argument with the following properties:

| Property | Example | Description |
| --- | --- | --- |
| `profileID` | "W8DUJ2" | The profile ID as received from `onegini.user.registration`

```js
onegini.user.authenticators.getAll(
  // the profile for which you'd like to retrieve available
  // authenticators
  {
    profileId: "W8DUJ2"
  },

  // success callback
  function (result) {
    for (var r in result) {
      var auth = result[r];
      console.log("Authenticator: " + auth.authenticatorType);
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
| `authenticatorType` | "PIN" | The authenticator type
| `authenticatorId` | "PIN" | The authenticator ID, which distinguishes between authenticators of type "Custom"

The error callback contains an object with these properties:

| Property | Example | Description |
| --- | --- | --- |
| `code` | 8003 | The error code
| `description` | "Onegini: No registered user found." | Human readable error description

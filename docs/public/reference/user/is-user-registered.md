# Is User Registered

You can check at any time if a certain profile ID has been registered by using this function:

## `onegini.user.isUserRegistered`

This function takes a mandatory first argument with the following properties:

| Property | Default | Description |
| --- | --- | --- |
| `profileId` | - | A profile ID you want to check

```js
onegini.user.isUserRegistered(
  {
    profileId: "W8DUJ2"
  },

  // success callback
  function (registered) {
    console.log(registered ? "Yes" : "No");
  },

  // error callback
  function (err) {
    console.log("Error: " + err.description);
  }
);
```

The success callback will be true or false based on the outcome of the check against the profile ID.

The error callback contains an object with these properties:

| Property | Example | Description |
| --- | --- | --- |
| `code` | 9001 | The error code
| `description` | "Invalid Pin" | Human readable error description

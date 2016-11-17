# Get User Profiles

A user may have multiple profiles. This function offers a way to retrieve them:

## `onegini.user.getUserProfiles`

```js
onegini.user.getUserProfiles(
  // success callback
  function (result) {
    for (var r in result) {
      var profile = result[r];
      console.log("Profile: " + profile.profileId);
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
| `profileId` | - | A Profile Id associated with the user

The error callback contains an object with these properties:

| Property | Example | Description |
| --- | --- | --- |
| `code` | 9001 | The error code
| `description` | "Invalid Pin" | Human readable error description

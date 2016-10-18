# Get Authenticated User Profile

A user may have multiple profiles. This function offers check if, and which one is currently authenticated:

## `onegini.user.getAuthenticatedUserProfile`

```js
onegini.user.getAuthenticatedUserProfile(
  // success callback
  function (result) {
    console.log("Profile: " + result.profileId);
  },

  // error callback
  function (err) {
    console.log("No authenticated user found. Description: " + err.description);
  }
);
```

The success callback contains an object with these properties:

| Property | Example | Description |
| --- | --- | --- |
| `profileId` | - | A Profile Id associated with the user

The error callback may be invoked in case the user was not authenticated, and contains an object with these properties:

| Property | Example | Description |
| --- | --- | --- |
| `code` | 9001 | The error code
| `description` | "Invalid Pin" | Human readable error description

# Get Authenticated User Profile

This method returns the currently authenticated User Profile. The method fails when no user is authenticated 

## `onegini.user.getAuthenticatedUserProfile`

```js
onegini.user.getAuthenticatedUserProfile()
  .then((userProfile) => {
    console.log("Authenticated userProfile:", userProfile);
  })
  .catch(() => {
    console.log("No user authenticated");
  })
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

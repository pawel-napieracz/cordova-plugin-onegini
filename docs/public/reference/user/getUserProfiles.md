# Get User Profiles

The Onegini SDK keeps a set of profiles that you have created in the SDK. This method allows you to receive all existing profiles.

## `onegini.user.getUserProfiles`

- Used to get an _array_ of all existing userProfiles.

```js
onegini.user.getUserProfiles()
  .then((userProfiles) => {
    console.log("User profiles registered: ", userProfiles);
  })
  .catch((err) => {
    console.log("Something went wrong!", err)
  });
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

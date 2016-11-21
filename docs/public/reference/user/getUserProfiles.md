# Get User Profiles

- Used to get an _array_ of all existing userProfiles.

## `onegini.user.getUserProfiles`

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

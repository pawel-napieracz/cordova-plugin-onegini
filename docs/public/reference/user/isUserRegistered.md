# Is User Registered

You can check at any time if a certain profile ID has been registered by using this method:

## `onegini.user.isUserRegistered`

- Requires an object with a `profileId`:

| Property | Default | Description |
| --- | --- | --- |
| `profileId` | - | A profile ID you want to check

```js
onegini.user.isUserRegistered({
    profileId: "W8DUJ2"
  })
  .then((isRegistered) => {
    if (isRegistered) {
      // The userProfile is already registered.
      console.log("That userProfile is registered!");
    } else {
      // The userProfile is not yet registered.
      // Use oneigni.user.register to register a userProfile.
      console.log("That user is not registered.");
    }
  })
  .catch((err) => {
    console.log("Something went wrong!", err);
  });
);
```

The success callback will be true or false based on the outcome of the check against the profile ID.

The error callback contains an object with these properties:

| Property | Example | Description |
| --- | --- | --- |
| `code` | 9001 | The error code
| `description` | "Invalid Pin" | Human readable error description

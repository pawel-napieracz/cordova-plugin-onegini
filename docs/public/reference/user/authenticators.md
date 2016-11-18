# Authenticators

Authenticators are used to verify the identity of a user.
The Onegini Cordova Plugin currently supports PIN and Fingerprint authenticators.
The Fingerprint authenticator is only available on devices with a fingerprint sensor.

## `onegini.user.authenticators.getAll`

- Used to get an array of authenticators available for a specific user.
- Requires an object containing a `profileId`.

| Property | Example | Description |
| --- | --- | --- |
| `profileID` | "W8DUJ2" | The profile ID as received from `onegini.user.registration`

```js
onegini.user.authenticators.getAll(
  {
    profileId: "W8DUJ2"
  })
  .then(function(authenticators) {
    console.log("Avaialbe authenticators", authenticators);
  })
  .catch(function(err) {
    if (err.code === 8003) {
      console.log("That user does not exist!");
    } else {
      console.log("Something went wrong! Error:", err);
    }
  });
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

## `onegini.user.authenticators.getNotRegistered`

- Used to get an array of authenticators that are _not currently registered_ for a specific user.
- Requires an object containing a `profileId`:

| Property | Example | Description |
| --- | --- | --- |
| `profileID` | "W8DUJ2" | The profile ID as received from `onegini.user.register`

```js
onegini.user.authenticators.getAll(
  {
    profileId: "W8DUJ2"
  })
  .then(function(authenticators) {
    console.log("Avaialbe authenticators", authenticators);
  })
  .catch(function(err) {
    if (err.code === 8003) {
      console.log("That user does not exist!");
    } else {
      console.log("Something went wrong! Error:", err);
    }
  });
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
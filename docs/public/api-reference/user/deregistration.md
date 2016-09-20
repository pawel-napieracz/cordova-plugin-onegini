# Deregistration

Deregistration is the process of remove registration of a user (profile) from the device and the server.

## `onegini.user.deregister`

This function takes a mandatory first argument with the following properties:

| Property | Default | Description |
| --- | --- | --- |
| `profileId` | - | The Profile Id you previously "remembered" during registration

```js
onegini.user.deregister(
  {
    profileId: "W8DUJ2"
  },

  // success callback
  function () {
    console.log("The provided Profile Id is no longer registered");
  },

  // error callback
  function (err) {
    console.log("Error: " + err.description);
  }
);
```

The error callback contains an object with these properties:

| Property | Example | Description |
| --- | --- | --- |
| `code` | 9001 | The error code
| `description` | "Invalid Pin" | Human readable error description

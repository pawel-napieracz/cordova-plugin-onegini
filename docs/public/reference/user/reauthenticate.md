# Reuthentication

<!-- toc -->

In cases where you want assurance that the user is who you expect them to be, a reauthentication can be triggered. Just like the [`authenticate`](authenticate.md) method, this method returns an [`AuthenticationHandler`](AuthenticationHandler.md).

## `onegini.user.reauthenticate`

- Used to reauthenticate a user.
- Requires an object containing a `profileId`.
- Returns a new AuthenticationHandler.

| Property | Default | Description |
| --- | --- | --- |
| `profileId` | - | The profile ID you previously stored during registration

**Example for reauthentication with PIN:**

```js
onegini.user.reauthenticate({
      profileId: "some profile ID"
    })
    .onPinRequest((actions, options) => {
      var pin = prompt("Please enter your PIN");
      actions.providePin(pin);
    })
    .onSuccess(() => {
      alert("Reauthentication success!");
    })
    .onError((err) => {
      alert("Reauthentication error!\n\n" + err.description);
    });
```

Note that if the user is able to register other authenticators, and set them as preferred, the handler methods used for those authenticators will need to be implemented as well. See the documentation on the [`AuthenticationHandler`](AuthenticationHandler.md) for more information.

The error callback contains an object with these properties:

| Property | Example | Description |
| --- | --- | --- |
| `code` | 9000 | The error code
| `description` | "Due to a problem with the device internet connection it was not possible to initiate the requested action" | Human readable error description

# Reuthentication

<!-- toc -->

In cases where you want to reassure the user is who you expect him to be, a reauthentication can be triggered.
Just like the [`authenticate`](authenticate.md) method this method returns an `AuthenticationHandler`.

## `onegini.user.reauthenticate`

- Used to reauthenticate a user.
- Requires an object containing a `profileId`.
- Returns a new AuthenticationHandler.

| Property | Default | Description |
| --- | --- | --- |
| `profileId` | - | The profile ID you previously stored during registration

Example for reauthentication with PIN.

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

The error callback contains an object with these properties:

| Property | Example | Description |
| --- | --- | --- |
| `code` | 9000 | The error code
| `description` | "Due to a problem with the device internet connection it was not possible to initiate the requested action" | Human readable error description

# Registration

Before a user can authenticate using PIN or Fingerprint, a user will have to register with your Token Server. Registration can be initiated with this method.

## `onegini.user.register`

- Returns a new `AuthenticationHandler`.
- Takes an optional object with a `scopes` property:

| Property | Default | Description |
| --- | --- | --- |
| `scopes` | server configuration | An array of scopes the user will register for (optional)

```js
onegini.user.register()
  .onCreatePinRequest(function (actions, options) {
    var pin = prompt("Create your " + options.pinLength + " digit PIN");
    actions.createPin(pin);
  })
  .onSuccess(function () {
    alert('Registration success!');
  })
  .onError(function (err) {
    alert('Registration error!\n\n' + err.description)
  });
```

The success callback contains an _Array of objects_ with these properties:

| Property | Example | Description |
| --- | --- | --- |
| `profileId` | "W8DUJ2" | A Profile Id associated with the user
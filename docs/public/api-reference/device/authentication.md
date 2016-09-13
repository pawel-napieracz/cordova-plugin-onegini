# Device authentication

This function can be used to authenticate a device for specific (or the default) scopes.

## `onegini.device.authenticate`

This function takes an optional first argument with the following properties:

| Property | Default | Description |
| --- | --- | --- |
| `scopes` | server configuration | An array of scopes the device will authenticate for

```js
onegini.device.authenticate(
  {
    scopes: ["read"]
  },

  // success callback
  function () {
    console.log("The device has been authenticated");
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

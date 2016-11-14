# Logout

For security reasons it is always a good idea to explicity logout a user. You can use this function to do so.

## `onegini.user.logout`

```js
onegini.user.logout(
  // success callback
  function () {
    console.log("Logout successful");
  },

  // error callback
  function (err) {
    console.log("Error: " + err.description);
  }
);
```

The error callback contains an object with the following properties.

| Property | Example | Description |
| --- | --- | --- |
| `code` | 8000 | The error code
| `description` | "Onegini: Internal plugin error." | Human readable error description

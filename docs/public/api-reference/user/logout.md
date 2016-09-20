# Logout

For security reasons it's always a good idea to explicity logout a user. You can use this function to do so:

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

The error callback contains an object with these properties:

| Property | Example | Description |
| --- | --- | --- |
| `code` | 9001 | The error code
| `description` | "Invalid Pin" | Human readable error description

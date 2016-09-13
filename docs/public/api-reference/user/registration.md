# Registration

Registering a user is a two-step process: first the user needs to perform an OAuth, then he needs to configure a Pin. These steps are reflected by the plugin API. Just take these steps in this order:

## `onegini.user.register.start`

This function takes an optional first argument with the following properties:

| Property | Default | Description |
| --- | --- | --- |
| `scopes` | server configuration | An array of scopes the user will register for

```js
onegini.user.register.start(
  {
    scopes: ["read"]
  },

  // success callback
  function (result) {
    console.log("Required Pin length: " + result.pinLength);
  },
  
  // error callback
  function (err) {
    console.log("Error description: " + err.description);
  }
);
```

The success callback contains an object with these properties:

| Property | Example | Description |
| --- | --- | --- |
| `pinLength` | 5 | The required Pin length that's configured on the server

The error callback contains an object with these properties:

| Property | Example | Description |
| --- | --- | --- |
| `code` | 9001 | The error code
| `description` | "Invalid Pin" | Human readable error description

## `onegini.user.register.createPin`

The success callback of `onegini.user.register.start` will provide you with the Pin length. Use this information to ask the user the Pin code he wants to configure. Once done send the Pin to this function, which takes a mandatory first argument with the following properties:

| Property | Default | Description |
| --- | --- | --- |
| `pin` | - | The Pin the user wants to register himself with

```js
onegini.user.register.createPin(
  {
    pin: "28649"
  },
  
  // success callback
  function (result) {
    console.log("Created Profile Id: " + profileId);
  },
  
  // error callback
  function (err) {
    console.log("Error: " + err.description);
  }
);
```

The success callback returns an object with these properties:

| Property | Example | Description |
| --- | --- | --- |
| `profileId` | W8DUJ2 | The profile Id representing the registered user

The error callback contains an object with these properties:

| Property | Example | Description |
| --- | --- | --- |
| `code` | 9001 | The error code
| `description` | "Invalid Pin" | Human readable error description

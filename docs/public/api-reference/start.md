# Bootstrapping the Onegini SDK

The first thing you'll want to do when your app starts is initializing the Onegini SDK. This will perform a few checks and report an error in case of trouble.

## `onegini.start`

This function takes an optional first argument with the following properties:

| Property | Default | Description |
| --- | --- | --- |
| `secureXhr` | `false` | Allow the plugin to intercept XHR GET/POST/.. webview requests and route them through the plugin for added security


```js
// As with all Cordova plugins you must wait for `deviceready` to fire
document.addEventListener('deviceready', function () {
  onegini.start(
      {
        secureXhr: true
      },

      // success callback
      function () {
        // you can now call any other method on the Onegini object
        console.log("Onegini is ready");
      },

      // error callback
      function (err) {
        console.log("Error description: " + err.description);
      }
  );
}, false);
```

The error callback contains an object with these properties:

| Property | Example | Description |
| --- | --- | --- |
| `code` | 9001 | The error code
| `description` | "Invalid Pin" | Human readable error description

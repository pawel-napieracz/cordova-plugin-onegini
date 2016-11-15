# API Reference

This section describes the APIs that are exposed by the Onegini Cordova Plugin.

## API

  * [Bootstrapping, do this first: `onegini.start`](start.md)
  * [User-related API's: `onegini.user.*`](user/introduction.md)
  * [Device-related API's: `onegini.device.*`](device/introduction.md)

## Using promises instead of callbacks

All API functions accept an optional success callback and error callback function.

If Promises are more your style then you may be happy to learn that all functions can be used as a Promise as well.

For example, if you want to use `start` with callbacks, do:

```js
onegini.user.authenticate.start(
  {
    profileId: "W8DUJ2"
  },

  // success callback
  function () {
    console.log("The provided profile ID is allowed to authenticate");
  },

  // error callback
  function (err) {
    console.log("Error: " + err.description);
  }
);
```

...or as a Promise, do:

```js
onegini.user.authenticate.start({
    profileId: "W8DUJ2"
}).then(function () {
    console.log("The provided profile ID is allowed to authenticate");
}, function (err) {
    console.log("Error: " + err.description);
});
```

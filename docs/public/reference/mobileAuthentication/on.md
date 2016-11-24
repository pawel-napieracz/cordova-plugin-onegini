# Handling mobile authentication requests

<!-- toc -->

When a user is enrolled for mobile authentication, they are able to receive and respond to mobile authentication requests. The Onegini Cordova Plugin facilitates this by allowing you to register handler methods that implement the UI.

## `onegini.mobileAuthentication.on`

This method is used to register handlers for different mobile authentication methods. The various methods (**push**, **push with PIN**, etc.) can be [configured](https://docs.onegini.com/token-server/topics/mobile-apps/mobile-authentication/mobile-authentication.html#configure-authentication-properties) in the Token Server. Once configured, the handlers for these various methods need to be registered using `on`, which takes the method string as parameter.

| Parameter | Description |
| --- | --- |
| `method` | The mobile authentication method you wish to register a handler for

Currently, the available method strings are `"confirmation"` (push), `"pin"` (push with PIN), and `"fingerprint"` (push with fingerprint).

The `on` method returns a [`MobileAuthenticationHandler`](MobileAuthenticationHandler.md) object, which can be used to implement the various handler methods.

**Example handler registration for push with PIN:**

```js
onegini.mobileAuthentication.on("pin")
    .onPinRequest((actions, request) => {
      console.log("New mobile authentication request", request);

      // Ask the user if they want to accept or deny the request, and enter
      // their PIN in case of accept. In this example, the user accepts the
      // request and enters a PIN of "12346".
      let userAcceptedRequest = true;
      let pin = "12346";

      if (userAcceptedRequest) {
        actions.accept(pin);
      } else {
        actions.deny();
      }
    })
    .onSuccess(() => {
      alert("Mobile authentication request success!");
    })
    .onError((err) => {
      alert("Mobile authentication request failed!\n\n" + err.description);
    });
```

See the documentation for `MobileAuthenticationHandler` for more details on how to implement the UI.

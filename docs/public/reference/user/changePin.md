# changePin

<!-- toc -->

Once authenticated, a user is able to change his PIN code. Changing a users PinCode is done through an [AuthenticationHandler](AuthenticationHandler.md)

## `onegini.user.changePin`

This functions takes no arguments, it uses the currently authenticated user.
After calling this method. You have to supply an `onPinRequest` and `onCreatePinRequest` callback. These callbacks will be verify the current pin and create a new pin.
If the flow fails completely the (e.g user is deregistered because as a result of exceeding the maximum allowed pin entries) the `onError` callback will be called with an error object.

```js
onegini.user.changePin()
  .onPinRequest((actions, options) => {
    var pin = prompt("Please enter your pin");
    actions.providePin(pin);
  })
  .onCreatePinRequest((actions) => {
    var pin = prompt("Enter your new pin");
    actions.createPin(pin);
  })
  .onSuccess(() => {
    alert('Change pin success!');
  })
  .onError((err) => {
    alert('Change pin Error!\n\n' + err.description)
  });
```
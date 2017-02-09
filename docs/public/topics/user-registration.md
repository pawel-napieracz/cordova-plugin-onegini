# User registration

## Introduction

User registration is a fundamental part of Onegini. As developer you have a couple options to handle process:

## Using default webview.

To register a user with a simple webview or browser to login on your Token Server:
```js
onegini.user.register()
  .onCreatePinRequest((actions, options) => {
    let pin = prompt(`Create your {options.pinLength} digit pin`);
    actions.createPin(pin);
  })
  .onSuccess(() => {
    alert('Registration success!');
  })
  .onError((err) => {
    alert('Registration failed: ' + err.description);
    console.error(err);
  });
```

This will open the default system browser on Android and a `WKWebView` on iOS.

## Using SFSafariViewController on iOS

The `WKWebView` on iOS can be replaced with `SFSafariViewController`.
This allows your users to access Safari extensions like password managers,
it also allows users to bring their cookies from your website so they might continue an already existing session started in Safari.

To enable `SFSafariViewController` set the following preference in your `config.xml`:
```xml
<platform name="ios">
  <preference name="OneginiWebView" value="SFSafariViewController"/>
</platform>
```

iOS also needs know about your redirect URL scheme. This is done through setting `CFBundleURLSchemes` in your `-Info.plist`,
refer to [Apple's documentation](https://developer.apple.com/library/content/documentation/Carbon/Conceptual/LaunchServicesConcepts/LSCConcepts/LSCConcepts.html) for further details.

The plugin will now use `SFSafariViewController` to open the registration endpoint. Note that `SFSafariViewController` is available on iOS 9 and later, the plugin will fall back to WKWebView on iOS 8.

## Using a JavaScript callback

User registration can be handled entirely through JS. This way you have full control over the registration flow.

To prevent the plugin from opening any browser, set the `OneginiWebView` preference to `disabled` in your `config.xml`:
```xml
<preference name="OneginiWebView" value="disabled"/>
```

Handle the `onRegistrationRequest` event and obtain the callback URL yourself:
```js
onegini.user.register()
  .onRegistrationRequest((actions, options) => {
    // Some logic to fetch the callback URL
    let registrationUrl = registerWithUrl(options.url);
    actions.handleRegistrationUrl(registrationUrl);
  })
  .onCreatePinRequest((actions, options) => {
    let pin = prompt(`Create your {options.pinLength} digit pin`);
    actions.createPin(pin);
  })
  .onSuccess((result) => {
    alert('Registration success!');
  })
  .onError((err) => {
    alert('Registration failed: ' + err.description);
    console.error(err);
  });
```

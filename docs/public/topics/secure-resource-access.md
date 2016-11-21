# Secure resource access

<!-- toc -->

## Secure resource access

There are two main ways that your app can securely access resources like account information, transactions, etc. from a back-end or resource server. The app can fetch resources anonymously, authenticating only the device itself, or it can fetch resources on behalf of a user. In the latter case, the user will need to be logged in.

The Onegini Cordova plugin exposes functions to perform both types of resource call. In each case, it ensures the confidentiality and authenticity of the payload. The application itself is responsible for the structure and/or processing of the payload.

## Fetching anonymous resources

A device can use its OAuth credentials to authenticate itself with the Token Server, and obtain an access token. An anonymous resource call can be used in cases where a user does not need to be logged in or even registered in order to use certain functionality, or access some resource.

The Onegini Cordova plugin exposes the [`onegini.resource.fetch`](../reference/resource/fetch.md) function in order to allow anonymous resource calls. The `anonymous` property must be set to `true`.

**Example code to fetch an anonymous resource:**

```js
onegini.resource.fetch({
      url: "https://my.server.com/resources/device/app-details",
      anonymous: true
    })
    .then((response) => {
      alert("Anonymous resource call success!\n\nResponse: " + response.status);
    })
    .catch((err) => {
      alert("Anonymous resource call error!\n\n" + err.description);
    });
```

In case of success, the `response` object also contains the body and any headers that were included. For more details, see the documentation for [fetch](../reference/resource/fetch.md).

## Fetching resources on behalf of a user

In order to successfully request a resource for a specific user, the client credentials must be valid and the user must have a valid access token. In other words, the user must be logged in before a resource call can be made on their behalf. This type of resource request should be used to fetch sensitive data that requires user authentication, like account details and transaction history.

The same function as before (`onegini.resource.fetch`) is used to perform resource calls on behalf of a user. This time, the `anonymous` property should not be set.

**Example code to fetch resources on behalf of a user:**

```js
onegini.resource.fetch({
      url: "https://my.server.com/resources/user/transaction-history",
      headers: {
        "My-Header-String": "String"
        "My-Header-Int": 42
      }
    })
    .then((response) => {
      alert("Resource call success!\n\nResponse: " + response.status);
    })
    .catch((err) => {
      alert("Resource call error!\n\n" + err.description);
    });
```

For details of the response and error objects, see the documention for [fetch](../reference/resource/fetch.md).

## Intercepting XHR requests

TODO: This section

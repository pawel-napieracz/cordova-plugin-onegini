# Fetch

Resources can be fetched by the device anonymously, or on behalf of a logged in user.

## `onegini.resource.fetch`

This function takes one mandatory argument, the `url`. The `url` can be absolute, or relative to the resource base url configured for the client. It also accepts several optional arguments such as `anonymous` which must be set to `true` for anonymous resource calls, and a number of arguments related to the REST call.

| Property | Default | Description |
| --- | --- | --- |
| `url` | - | The address of the resource being fetched
| `anonymous` | `false` | A boolean distinguishing between anonymous resource calls and those on behalf of a user
| `method` | `GET` | The method of the REST request
| `headers` | `{}` | The headers of the REST request
| `body` | - | The body of the REST request (only available for the methods that support a body)

**Example anonymous resource call:**

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

**Example resource call on behalf of user:**

```js
onegini.resource.fetch({
      url: "https://my.server.com/resources/user/transaction-history",
      headers: {
        "My-Header-String": "String",
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

The success callback contains an object with these properties:

| Property | Example | Description |
| --- | --- | --- |
| `status` | 200 | The HTTP status code of the server response
| `headers` | `{ Content-Type: "application/json" }` | The HTTP headers of the server response
| `body` | - | The body of the server response

The error callback differs depending on whether the plugin got a response from the server, or encountered an error in the plugin or SDK. In the former case, the error callback will contain an object similar to that of the success callback, except with a non-200 `status`.

In the latter case, the error callback will contain an object with the following properties:

| Property | Example | Description |
| --- | --- | --- |
| `code` | 8000 | The error code
| `description` | "Onegini: Internal plugin error" | Human readable error description

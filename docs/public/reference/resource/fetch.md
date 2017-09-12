# Fetch

<!-- toc -->

Resources can be fetched using user authentication, implicit authentication or anonymous authentication. Refer to the [topic guide](../../topics/secure-resource-access.md)
for more information about the differences between these authentication methods.

## `onegini.resource.fetch`

This function takes one mandatory argument, the `url`. The `url` can be absolute, or relative to the resource base url configured for the client. It also accepts several optional arguments such as `anonymous` which must be set to `true` for anonymous resource calls, and a number of arguments related to the REST call.

| Property  | Default                      | Description                                                                             |
| --------- | ---------------------------- | --------------------------------------------------------------------------------------- |
| `url`     | -                            | The address of the resource being fetched                                               |
| `auth`    | `onegini.resource.auth.USER` | The authentication method used for authenticating with the token server                 |
| `method`  | `GET`                        | The method of the REST request                                                          |
| `headers` | `{}`                         | The headers of the REST request                                                         |
| `body`    | -                            | The body of the REST request (only available for the methods that support a body)       |

The method only succeeds when an http success status (within the 200-299 range) is returned by the server.
If no request could be made to the server or if the server returns an error status the error callback is called.
The auth property can be on of `onegini.resource.auth.USER`, `onegini.resource.auth.IMPLICIT` or `onegini.resource.auth.ANONYMOUS`.

**Example resource call:**

This call uses implicit authentication to request a resource, also sending an `X-Foo` header with value `bar`.

```js
onegini.resource.fetch({
      url: "https://my.server.com/resources/device/account-balance",
      auth: onegini.resource.auth.IMPLICIT,
      headers: {
        'X-Foo': 'bar'
      }
    })
    .then((response) => {
      alert("Anonymous resource call success!\n\nResponse: " + response.status);
    })
    .catch((err) => {
      alert("Anonymous resource call error!");
    });
```

The success callback contains an object with these properties:

| Property       | Example                                | Description                                               |
| -------------- | -------------------------------------- | --------------------------------------------------------- |
| `status`       | 200                                    | The HTTP status code of the server response               |
| `statusText`   | "OK"                                   | The HTTP status text of the server response               |
| `headers`      | `{ Content-Type: "application/json" }` | The HTTP headers of the server response                   |
| `body`         | -                                      | The response body encoded as a UTF-8 string               |
| `rawBody`      | -                                      | An `arrayBuffer` containing the data of the response body |
| `json`         | -                                      | The response body parsed as UTF-8 encoded JSON            |

The error callback contains an object with the usual error code and description.

In addition, it contains an `httpResponse` if an http request was performed (might not be the case, for example when an IOException occurs).
The `httpResponse` object contains the same `status`, `statusText`, `headers` and `body` properties as a success response. 

| Property       | Example                        | Description                        |
| ---            | ---                            | ---                                |
| `httpResponse` | See success callback           | The HTTP Response from the server. |
| `code`         | 8000                           | The error code                     |
| `description`  | Onegini: Internal plugin error | Human readable error description   |

# Enroll for mobile authentication

<!-- toc -->

Authenticated users can enroll for mobile authentication, allowing them to receive and respond to push notifications.

## `onegini.mobileAuthentication.enroll`

This function enrolls the currently logged in user for mobile authentication.

**Example enrollment for a logged in user:**

```js
onegini.mobileAuthentication.enroll()
    .then(() => {
      alert("Enrollment success!");
    })
    .catch((err) => {
      alert("Enrollment error!\n\n" + err.description);
    });
```

The error callback contains an object with the following properties:

| Property | Example | Description |
| --- | --- | --- |
| `code` | 8000 | The error code
| `description` | "Onegini: Internal plugin error" | Human readable error description

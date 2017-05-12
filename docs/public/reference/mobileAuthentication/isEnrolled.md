# Check if the user is enrolled for mobile auth

<!-- toc -->

In order to perform mobile authentication a user must be enrolled. This convenience method checks whether the currently authenticated user is enrolled for 
mobile auth. Therefore you don't have to keep track of this on your own.

## `onegini.mobileAuth.isEnrolled`

This function checks whether the currently authenticated user is enrolled for mobile auth. 

**Example enrollment check for a logged in user:**

```js
onegini.mobileAuth.isEnrolled()
    .then((isEnrolled) => {
      if (isEnrolled) {
        alert("The user is enrolled.");
      } else {
        alert("The user is not enrolled.");
      }
    })
    .catch((err) => {
      alert("error!\n\n" + err.description);
    });
```

The error callback contains an object with the following properties:

| Property | Example | Description |
| --- | --- | --- |
| `code` | 8000 | The error code
| `description` | "Onegini: Internal plugin error" | Human readable error description

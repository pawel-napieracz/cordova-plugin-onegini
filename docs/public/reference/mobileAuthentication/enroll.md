# Enroll for mobile authentication

<!-- toc -->

Mobile authentication is a feature where the end-user uses his/her phone to confirm a transaction or login. The mobile authentication feature is an extensive
 feature that has a number of different possibilities. E.g. there are different ways that mobile authentication is triggered / received on a mobile device:
- With push notifications; The user gets a push notification on his phone to alert him that a mobile authentication transaction is pending.
- With an One-Time-Password (OTP); The user provides an OTP in order to confirm a mobile authentication transaction. Since the OTP is long it is likely that 
the OTP is transformed into a QR code and the user scans this code with his mobile device.

Only authenticated users can enroll for mobile authentication.

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

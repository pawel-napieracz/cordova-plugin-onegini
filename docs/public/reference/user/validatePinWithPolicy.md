# Validate Pin with Policy

At any moment in the app's lifecycle you can check whether or not a certain Pin code meets the policy you've configured server side.
A Pin Policy can be used to ensure your users can not register with an unsecure PIN.

## `onegini.user.validatePinWithPolicy`

- Requires an object with a `pin` property:

| Property | Default | Description |
| --- | --- | --- |
| `pin` | - | The Pin the user wants to verify agains the policy

The success callback will be invoked if the Pin is valid according to the policy. The error callback is invoked otherwise, which the usual error code and description explaining why it's not a valid Pin code.

```js
onegini.user.validatePinWithPolicy(
  {
    pin: "28649"
  })
  .then(() => {
    console.log("The PIN is OK");
  })
  .catch((err) => {
    console.log("This PIN is invalid. Error:", err);
  });
  }
);
```

The error callback contains an object with these properties:

| Property | Example | Description |
| --- | --- | --- |
| `code` | 9011 | The error code
| `description` | "Provided PIN was marked as blacklisted on the Token Server." | Human readable error description

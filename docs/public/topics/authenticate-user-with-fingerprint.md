# Authenticate user with fingerprint

<!-- toc -->

## Introduction

The Onegini Cordova plugin allows you to authenticate users with the fingerprint scanner (if available on the device). This can be used for both regular authentication as well as mobile authentication. Users will be able to scan their fingerprint as many times as the OS will allow them to. If the OS fingerprint API returns an error for any reason (for example in the case of too many failed attempts), the Onegini Cordova plugin revokes fingerprint authentication and performs a fallback to PIN authentication.

## Enabling fingerprint authentication

In order to enable fingerprint authentication for a user, you will first need to request a list of available authenticators that have not yet been registered. This can be done using the function [`onegini.user.authenticators.getNotRegistered`](../reference/user/get-not-registered-authenticators.md), which takes the `profileId` of the desired user as argument. This function returns an array of authenticator objects. You can then register the chosen authenticator by providing the `authenticatorType` property to [`onegini.user.authenticators.setPreferred`](../reference/user/set-preferred.md). If the device doesn't meet the fingerprint requirements, the fingerprint authenticator will not be present in the array of of authenticators.

**Example code for registering the fingerprint authenticator**

```js
```

Note that registering a new authenticator does not set it as the preferred authenticator for the user, which is PIN by default. To change this, [`onegini.user.authenticators.setPreferred`](../reference/user/set-preferred-authenticator.md) can be used.

## Authentication handler

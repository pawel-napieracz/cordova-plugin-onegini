var OneginiDemo = (function () {
  return {
    start: function () {
      onegini.start(
          {},
          function () {
            console.log("Onegini is ready to go!");
          },
          function (err) {
            alert("Error!\n\n" + err.description);
          }
      );
    },

    isRegistered: function () {
      onegini.user.isRegistered(
          function (registered) {
            alert(registered ? "YES" : "NO");
          },
          function (err) {
            alert("Error!\n\n" + err.description);
          }
      );
    },

    startRegistration: function () {
      var that = this;
      onegini.user.register.start(
          {
            //scopes: ["read"]
          },
          function (result) {
            console.log("onegini.user.register.start success, now calling onegini.user.register.createPin. " + JSON.stringify(result));
            // added a little timeout so the embedded browser has time to disappear
            setTimeout(function () {
              that.registrationCreatePin(result.pinLength);
            }, 900);
          },
          function (err) {
            alert("Error!\n\n" + err.description);
          }
      );
    },

    registerFingerprintAuthenticator: function () {
      onegini.user.authenticators.registerNew({authenticatorId: "com.onegini.authenticator.TouchID"})
          .onPinRequest(function (actions, options) {
            var pin = prompt("Please enter your " + options.pinLength + " digit pin", "12346");
            actions.providePin(pin);
          })
          .onSuccess(function () {
            alert("Success!");
          })
          .onError(function (err) {
            alert("Error!\n\n" + err.description);
          });
    },

    setFingerprintAuthenticator: function () {
      onegini.user.authenticators.setPreferred({
            authenticatorId: "com.onegini.authenticator.TouchID"
          },
          function () {
            alert("Success!")
          },
          function (err) {
            alert("Error!\n\n" + err.description);
          });
    },

    registrationCreatePin: function (pinLength) {
      var pin = prompt("Please enter your " + pinLength + " digit Pin", "12346");
      if (!pin) {
        return;
      }
      onegini.user.register.createPin(
          {
            pin: pin
          },
          function (result) {
            alert("Success!\n\nProfile created: " + result.profileId);
          },
          function (err) {
            alert("Error!\n\n" + err.description);
          }
      );
    },

    getUserProfiles: function () {
      var that = this;
      onegini.user.getUserProfiles(
          function (result) {
            that.userProfiles = result;
            alert("Success!\n\nUser profiles:\n" + JSON.stringify(result));
          },
          function (err) {
            alert("Error!\n\n" + err.description);
          }
      );
    },

    deregister: function () {
      var profileId = this.userProfiles && this.userProfiles.length > 0 ? this.userProfiles[0].profileId : null;
      profileId = prompt("Please enter the profileId", profileId);
      if (!profileId) {
        return;
      }
      onegini.user.deregister(
          {
            profileId: profileId
          },
          function () {
            alert("Deregister success!");
          },
          function (err) {
            alert("Error!\n\n" + err.description);
          }
      );
    },

    startDeviceAuthentication: function () {
      onegini.device.authenticate(
          {
            scopes: ["read"] // optional
          },
          function () {
            alert("Success!");
          },
          function (err) {
            alert("Error!\n\n" + err.description);
          }
      );
    },

    startUserAuthentication: function () {
      var profileId = this.userProfiles && this.userProfiles.length > 0 ? this.userProfiles[0].profileId : null;
      profileId = prompt("Please enter the profileId", profileId);
      if (!profileId) {
        return;
      }

      onegini.user.authenticate(profileId)
          .onPinRequest(function (actions, options) {
            var pin = prompt("Please enter your " + options.pinLength + " digit pin", "12346");
            actions.providePin(pin);
          })
          .onFingerprintRequest(function (actions) {
            alert("Accepting fingerprint authentication request");
            actions.acceptFingerprint({ iosPrompt: "Login to Cordova Example App" });
          })
          .onFingerprintCaptured(function () {
            console.info("Authentication: Fingerprint captured");
          })
          .onFingerprintFailed(function () {
            console.info("Authentication: Fingerprint failed");
          })
          .onSuccess(function () {
            alert("Authentication success!");
          })
          .onError(function (err) {
            alert("Authentication error!\n\n" + err.description);
          });
    },

    reauthenticate: function () {
      var profileId = this.userProfiles && this.userProfiles.length > 0 ? this.userProfiles[0].profileId : null;
      profileId = prompt("Please enter the profileId", profileId);
      if (!profileId) {
        return;
      }
      var that = this;
      onegini.user.reauthenticate.start(
          {
            profileId: profileId
          },
          function (result) {
            console.log("onegini.user.reauthenticate.start success, now calling onegini.user.reauthenticate.providePin. " + JSON.stringify(result));
            that.providePin(result.pinLength);
          },
          function (err) {
            alert("Error!\n\n" + err.description);
          }
      );
    },

    isUserRegistered: function () {
      var profileId = this.userProfiles && this.userProfiles.length > 0 ? this.userProfiles[0].profileId : null;
      profileId = prompt("Please enter the profileId", profileId);
      if (!profileId) {
        return;
      }
      var that = this;
      onegini.user.isUserRegistered(
          {
            profileId: profileId
          },
          function (registered) {
            alert("Registered? " + (registered ? "Yes" : "No"));
          },
          function (err) {
            alert("Error!\n\n" + err.description);
          }
      );
    },

    getAuthenticatedUserProfile: function () {
      onegini.user.getAuthenticatedUserProfile(
          function (result) {
            alert("Success!\n\ProfileId: " + result.profileId);
          },
          function (err) {
            alert(err.description);
          }
      );
    },

    startChangePin: function () {
      onegini.user.changePin()
          .onPinRequest(function (actions, options) {
            var pin = prompt("Please enter your " + options.pinLength + " digit pin", "12346");
            actions.providePin(pin);
          })
          .onCreatePinRequest(function (actions) {
            var pin = prompt("Enter your new pin");
            actions.createPin(pin);
          })
          .onSuccess(function () {
            alert('Change pin success!');
          })
          .onError(function (err) {
            alert('Change pin Error!\n\n' + err.description)
          });
    },

    getRegisteredAuthenticators: function () {
      onegini.user.authenticators.getRegistered(
          function (result) {
            alert("Success!\n\n" + JSON.stringify(result));
          },
          function (err) {
            alert("Error!\n\n" + err.description);
          }
      );
    },

    getNotRegisteredAuthenticators: function () {
      onegini.user.authenticators.getNotRegistered(
          function (result) {
            alert("Success!\n\n" + JSON.stringify(result));
          },
          function (err) {
            alert("Error!\n\n" + err.description);
          }
      );
    },

    enrollForMobileAuthentication: function () {
      onegini.mobileAuthentication.enroll(
          function () {
            alert("Success!");
          },
          function (err) {
            alert(err.description);
          }
      );
    },

    logout: function () {
      onegini.user.logout(
          function () {
            alert("Logout success!");
          },
          function (err) {
            alert(err.description);
          }
      );
    }
  };
})();
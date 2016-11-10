var OneginiDemo = (function () {
  return {
    start: function () {
      onegini.start(
          {},
          function () {
            console.log("Onegini is ready to go!");
            OneginiDemo.registerHandlers();
          },
          function (err) {
            alert("Error!\n\n" + err.description);
          }
      );
    },

    registerHandlers: function () {
      onegini.mobileAuthentication.on("confirmation")
          .onRequest(function (actions, request) {
            console.log("Mobile Authentication Confirmation request", request);

            navigator.notification.confirm(request.message, function (buttonIndex) {
              if (buttonIndex === 1) {
                actions.accept();
              }
              else {
                actions.reject();
              }
            }, "Mobile Authentication Request", ["Accept", "Reject"]);
          })
          .onSuccess(function () {
            alert("Mobile authentication request success!");
          })
          .onError(function (err) {
            alert("Mobile authentication request failed!");
            console.error("Mobile authentication request failed: ", err);
          });

      onegini.mobileAuthentication.on("pin")
          .onRequest(function (actions, request) {
            console.log("Mobile Authentication PIN request", request);

            navigator.notification.prompt(request.message, function (results) {
              if (results.buttonIndex === 1) {
                actions.accept(results.input1);
              }
              else {
                actions.reject();
              }
            }, "Mobile Authentication Request", ["Accept", "Reject"], "12346");
          })
          .onSuccess(function () {
            alert("Mobile authentication request success!");
          })
          .onError(function (err) {
            alert("Mobile authentication request failed!");
            console.error("Mobile authentication request failed: ", err);
          });

      onegini.mobileAuthentication.on("fingerprint")
          .onRequest(function (actions, request) {
            console.log("Mobile authentication fingerprint request", request);

            navigator.notification.confirm(request.message, function (buttonIndex) {
              if (buttonIndex === 1) {
                actions.accept();
              }
              else {
                actions.reject();
              }
            }, "Mobile Authentication Request", ["Accept", "Reject"]);
          })
          .onFingerprintCaptured(function () {
            console.info("Mobile Authentication event: fingerprint captured");
          })
          .onFingerprintFailed(function () {
            console.info("Mobile Authentication event: fingerprint failed");
          })
          .onSuccess(function (actions, request) {
            alert("Mobile authentication request success!");
          })
          .onError(function (err) {
            alert("Mobile authentication request failed!");
            console.error("Mobile authentication request failed: ", err);
          })
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
      onegini.user.register()
          .onCreatePinRequest(function (actions, options) {
            var pin = prompt("Create your " + options.pinLength + "digit pin", "12346");
            actions.createPin(pin);
          })
          .onSuccess(function () {
            alert('Registration success!');
          })
          .onError(function (err) {
            alert('Registration error!\n\n' + err.description)
          });
    },

    registerFingerprintAuthenticator: function () {
      onegini.user.authenticators.registerNew({authenticatorType: "Fingerprint"})
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
            authenticatorType: "Fingerprint"
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
            actions.acceptFingerprint({iosPrompt: "Login to Cordova Example App"});
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
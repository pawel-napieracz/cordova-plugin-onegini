var OneginiDemo = (function () {
  return {
    start: function () {
      onegini.start(
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
              that.createPin(result.pinLength);
            }, 900);
          },
          function (err) {
            alert("Error!\n\n" + err.description);
          }
      );
    },

    createPin: function (pinLength) {
      var pin = prompt("Please enter your " + pinLength + " digit Pin", "12346" /* default */);
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
            alert("Success!\n\User profiles:\n" + JSON.stringify(result));
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

    startAuthentication: function () {
      var profileId = this.userProfiles && this.userProfiles.length > 0 ? this.userProfiles[0].profileId : null;
      profileId = prompt("Please enter the profileId", profileId);
      if (!profileId) {
        return;
      }
      var that = this;
      onegini.user.authenticate.start(
          {
            profileId: profileId
          },
          function (result) {
            console.log("onegini.user.authenticate.start success, now calling onegini.user.authenticate.providePin. " + JSON.stringify(result));
            that.providePin(result.pinLength);
          },
          function (err) {
            alert("Error!\n\n" + err.description);
          }
      );
    },

    providePin: function () {
      var pin = prompt("Please enter your Pin", "12346" /* default */);
      if (!pin) {
        return;
      }
      onegini.user.authenticate.providePin(
          {
            pin: pin
          },
          function (result) {
            alert("Authentication succeeded!");
          },
          function (err) {
            alert("Error!\n\n" + JSON.stringify(err));
          }
      );
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

    getAuthenticatedUserProfile: function () {
      onegini.user.getAuthenticatedUserProfile(
          function (result) {
            alert("Success!\n\ProfileId: " + result.profileId);
          },
          function (err) {
            alert(err.description);
          }
      );
    }
  }
})();
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
      onegini.user.startRegistration(
          {
            scopes: ["read"]
          },
          function (result) {
            that.pinLength = result.pinLength;
            // added a little timeout so the embedded browser has time to disappear
            setTimeout(function () {
              alert("Success!\n\n" + JSON.stringify(result));
            }, 900);
          },
          function (err) {
            alert("Error!\n\n" + err.description);
          }
      );
    },

    createPin: function () {
      var pin = prompt("Please enter your " + this.pinLength + " digit Pin", "12346" /* default */);
      if (!pin) {
        return;
      }
      onegini.user.createPin(
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

    getUserProfiles: function() {
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

    startAuthentication: function() {
      var profileId = this.userProfiles && this.userProfiles.length > 0 ? this.userProfiles[0].profileId : null;
      profileId = prompt("Please enter the profileId", profileId);
      onegini.user.startAuthentication(
        {
          profileId: profileId
        },
        function(result) {
          alert("Success!\n\n" + JSON.stringify(result));
        },
        function(err) {
          alert("Error!\n\n" + err.description);
        }
      );
    },

    checkPin: function() {
      var pin = prompt("Please enter your Pin", "12346" /* default */);
      if (!pin) {
        return;
      }
      onegini.user.checkPin(
        {
          pin: pin
        },
        function(result) {
          alert("Authentication succeeded!");
        },
        function(err) {
          alert("Error!\n\n" + JSON.stringify(err));
        }
      );
    }
  }
})();
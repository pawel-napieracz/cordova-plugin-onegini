var OneginiDemo = (function() {
  return {
    start: function() {
      onegini.start(
          function() {
            console.log("Onegini is ready to go!");
          },
          function(err) {
            alert("Error!\n\n" + err.description);
          }
      );
    },

    isRegistered: function() {
      onegini.user.isRegistered(
        function(registered) {
          alert(registered ? "YES" : "NO");
        },
        function(err) {
          alert("Error!\n\n" + err.description);
        }
      );
    },

    startRegistration: function() {
      var that = this;
      onegini.user.startRegistration(
          {
            scopes: ["read"]
          },
          function(result) {
            that.pinLength = result.pinLength;
            // added a little timeout so the embedded browser has time to disappear
            setTimeout(function() {
              alert("Success!\n\n" + JSON.stringify(result));
            }, 300);
          },
          function(err) {
            alert("Error!\n\n" + err.description);
          }
      );
    },

    createPIN: function() {
      var pin = prompt("Please enter your " + this.pinLength + " digit PIN", "12346" /* default */);
      onegini.user.createPIN(
          {
            pin: pin
          },
          function(result) {
            alert("Success!\n\nProfile created: " + result.profileId);
          },
          function(err) {
            alert("Error!\n\n" + err.description);
          }
      );
    }
  }
})();
var OneginiDemo = (function() {
  return {
    start: function() {
      onegini.start(
          function() {
            alert("Success!");
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
            alert("Success!\n\n" + JSON.stringify(result));
          },
          function(err) {
            alert("Error!\n\n" + err.description);
          }
      );
    },

    createPIN: function() {
      var pin = prompt("Please enter your " + this.pinLength + " digit PIN", "12345");
      onegini.user.createPIN(
          {
            pin: pin
          },
          function(result) {
            alert("Success!\n\n" + JSON.stringify(result));
          },
          function(err) {
            alert("Error!\n\n" + err.description);
          }
      );
    }
  }
})();
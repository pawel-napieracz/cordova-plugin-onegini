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

    generateRegistrationChallenge: function() {
      onegini.user.generateRegistrationChallenge(
          {
            scopes: ["read"]
          },
          function(result) {
            alert("Success!\n\n" + JSON.stringify(result));
          },
          function(err) {
            alert("Error!\n\n" + err.description);
          }
      );
    },

    setPIN: function() {
      onegini.user.setPIN(
          {
            pin: "12345"
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
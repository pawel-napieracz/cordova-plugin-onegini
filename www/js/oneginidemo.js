var OneginiDemo = (function() {
  return {
    start: function() {
      onegini.start(
          function() {
            alert("OK!");
          },
          function(err) {
            alert("Error. Type: " + err.type + ", description: "+ err.description);
          }
      );
    },

    // TODO, more of the same
    anyOtherFunction: function() {
      // ..
    }
  }
})();
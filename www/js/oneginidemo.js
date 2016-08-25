var OneginiDemo = (function() {
  return {
    start: function() {
      onegini.start(
          function() {
            alert("OK!");
          },
          function(err) {
            alert("Not OK: " + err);
          }
      );
    },

    // TODO, more of the same
    anyOtherFunction: function() {
      // ..
    }
  }
})();
module.exports = (function() {
  function isArray(obj) {
    return Object.prototype.toString.call(obj) === '[object Array]';
  }

  return {
    isArray: isArray
  };
})();
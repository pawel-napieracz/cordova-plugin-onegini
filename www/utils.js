module.exports = (function() {
  var exec = require('cordova/exec');

  function isArray(obj) {
    return Object.prototype.toString.call(obj) === '[object Array]';
  }

  function promiseOrCallbackExec(serviceName, methodName, args, successCb, failureCb) {
    if (!args) {
      args = [];
    }
    else if (!isArray(args)) {
      args = [args];
    }

    if (successCb) {
      return exec(successCb, failureCb, serviceName, methodName, args);
    }
    else {
      return new Promise(function (resolve, reject) {
        exec(resolve, reject, serviceName, methodName, args);
      });
    }
  }

  return {
    isArray: isArray,
    promiseOrCallbackExec: promiseOrCallbackExec
  };
})();
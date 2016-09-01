module.exports = (function() {
  var exec = require('cordova/exec');

  function isArray(obj) {
    return Object.prototype.toString.call(obj) === '[object Array]';
  }

  function promiseOrCallbackExec(serviceName, methodName, args, successCb, failureCb) {
    args = sanitizeCordovaArgs(args);

    if (successCb) {
      return exec(successCb, failureCb, serviceName, methodName, args);
    }
    else {
      return new Promise(function (resolve, reject) {
        exec(resolve, reject, serviceName, methodName, args);
      });
    }
  }

  function callbackExec(serviceName, methodName, args, successCb, failureCb) {
    if (!successCb) {
      throw new TypeError("Onegini: missing argument for method. '" + methodName + "' requires a Success Callback");
    }

    args = sanitizeCordovaArgs(args);
    return exec(successCb, failureCb, serviceName, methodName, args);
  }

  function sanitizeCordovaArgs(args) {
    if (!args) {
      args = [];
    }
    else if (!isArray(args)) {
      args = [args];
    }

    return args;
  }

  return {
    isArray: isArray,
    promiseOrCallbackExec: promiseOrCallbackExec,
    callbackExec: callbackExec
  };
})();
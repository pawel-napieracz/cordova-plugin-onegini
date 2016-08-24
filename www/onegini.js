'use strict';

module.exports = (function () {
  const exec = require('cordova/exec');
  const utils = require('./utils');

  function promiseOrCallbackExec(methodName, args, successCb, failureCb) {
    if (!args) {
      args = [];
    }
    else if (!utils.isArray(args)) {
      args = [args];
    }

    if (successCb) {
      return exec(successCb, failureCb, 'OneginiCordovaClient', methodName, args);
    }
    else {
      return new Promise((resolve, reject) => {
        exec(resolve, reject, 'OneginiCordovaClient', methodName, args);
      });
    }
  }

  function start(successCb, failureCb) {
    return promiseOrCallbackExec('start', false, successCb, failureCb);
  }

  return {
    start: start
  };
})();
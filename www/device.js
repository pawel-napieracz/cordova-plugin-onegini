module.exports = (function () {
  var utils = require('./utils');

  function register(scopes, successCb, failureCb) {
    return utils.promiseOrCallbackExec('OneginiClient', 'start', scopes, successCb, failureCb);
  }

  return {
    register: register
  };
})();
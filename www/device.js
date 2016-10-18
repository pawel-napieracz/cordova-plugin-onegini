module.exports = (function () {
  var utils = require('./utils');

  function authenticate(options, successCb, failureCb) {
    return utils.promiseOrCallbackExec('OneginiDeviceAuthenticationClient', 'authenticate', options, successCb, failureCb);
  }

  return {
    authenticate: authenticate
  };
})();
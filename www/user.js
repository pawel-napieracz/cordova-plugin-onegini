module.exports = (function () {
  var utils = require('./utils');

  function startRegistration(options, successCb, failureCb) {
    return utils.promiseOrCallbackExec('OneginiUserRegistrationClient', 'startRegistration', options, successCb, failureCb);
  }

  function createPIN(options, successCb, failureCb) {
    return utils.promiseOrCallbackExec('OneginiUserRegistrationClient', 'createPIN', options, successCb, failureCb);
  }

  return {
    startRegistration: startRegistration,
    createPIN: createPIN
  };
})();
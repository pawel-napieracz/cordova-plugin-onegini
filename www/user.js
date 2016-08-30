module.exports = (function () {
  var utils = require('./utils');

  function startRegistration(options, successCb, failureCb) {
    return utils.promiseOrCallbackExec('OneginiUserRegistrationClient', 'startRegistration', options.scopes, successCb, failureCb);
  }

  function createPin(options, successCb, failureCb) {
    if (!successCb) {
      throw new TypeError("Onegini: missing argument for createPin: no success callback provided");
    }
    utils.promiseOrCallbackExec('OneginiUserRegistrationClient', 'createPin', options.pin, successCb, failureCb);
  }

  function getRegisteredUsers(successCb, failureCb) {
    return utils.promiseOrCallbackExec('OneginiUserRegistrationClient', 'getRegisteredUsers', [], successCb, failureCb);
  }

  return {
    startRegistration: startRegistration,
    createPIN: createPIN,
    getRegisteredUsers: getRegisteredUsers
    createPin: createPin
  };
})();
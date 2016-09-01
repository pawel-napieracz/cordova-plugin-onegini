module.exports = (function () {
  var utils = require('./utils');

  function startRegistration(options, successCb, failureCb) {
    return utils.promiseOrCallbackExec('OneginiUserRegistrationClient', 'startRegistration', options, successCb, failureCb);
  }

  function createPin(options, successCb, failureCb) {
    utils.callbackExec('OneginiUserRegistrationClient', 'createPin', options, successCb, failureCb);
  }

  function getUserProfiles(successCb, failureCb) {
    return utils.promiseOrCallbackExec('OneginiUserRegistrationClient', 'getUserProfiles', [], successCb, failureCb);
  }

  function startAuthentication(options, successCb, failureCb) {
    return utils.promiseOrCallbackExec('OneginiUserAuthenticationClient', 'startAuthentication', options, successCb, failureCb);
  }

  function checkPin(options, successCb, failureCb) {
    utils.callbackExec('OneginiUserAuthenticationClient', 'checkPin', options, successCb, failureCb);
  }

  return {
    startRegistration: startRegistration,
    createPin: createPin,
    getUserProfiles: getUserProfiles,
    startAuthentication: startAuthentication,
    checkPin: checkPin
  };
})();
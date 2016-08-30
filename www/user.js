module.exports = (function () {
  var utils = require('./utils');

  function startRegistration(options, successCb, failureCb) {
    return utils.promiseOrCallbackExec('OneginiUserRegistrationClient', 'startRegistration', options, successCb, failureCb);
  }

  function createPin(options, successCb, failureCb) {
    if (!successCb) {
      throw new TypeError("Onegini: missing argument for createPin: no success callback provided");
    }
    // no promise possible as this may be called more than once
    utils.promiseOrCallbackExec('OneginiUserRegistrationClient', 'createPin', options, successCb, failureCb);
  }

  function getUserProfiles(successCb, failureCb) {
    return utils.promiseOrCallbackExec('OneginiUserRegistrationClient', 'getUserProfiles', [], successCb, failureCb);
  }

  return {
    startRegistration: startRegistration,
    createPin: createPin,
    getUserProfiles: getUserProfiles
  };
})();
module.exports = (function () {
  var utils = require('./utils');

  function startRegistration(options, successCb, failureCb) {
    return utils.promiseOrCallbackExec('OneginiUserRegistrationClient', 'startRegistration', options, successCb, failureCb);
  }

  function createPin(options, successCb, failureCb) {
    if (!successCb) {
      throw new TypeError("Onegini: missing success callback for createPin");
    }
    if (!options || !options.pin) {
      throw new TypeError("Onegini: missing 'pin' argument for createPin");
    }
    // not returning a promise as this function may return more than once
    utils.callbackExec('OneginiUserRegistrationClient', 'createPin', options, successCb, failureCb);
  }

  function getUserProfiles(successCb, failureCb) {
    return utils.promiseOrCallbackExec('OneginiUserRegistrationClient', 'getUserProfiles', [], successCb, failureCb);
  }

  function startAuthentication(options, successCb, failureCb) {
    if (!options || !options.profileId) {
      throw new TypeError("Onegini: missing 'profileId' argument for createPin");
    }
    return utils.promiseOrCallbackExec('OneginiUserAuthenticationClient', 'startAuthentication', options, successCb, failureCb);
  }

  function checkPin(options, successCb, failureCb) {
    if (!successCb) {
      throw new TypeError("Onegini: missing argument for checkPin: no success callback provided");
    }
    if (!options || !options.pin) {
      throw new TypeError("Onegini: missing 'pin' argument for checkPin");
    }
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
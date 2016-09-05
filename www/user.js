module.exports = (function () {
  var utils = require('./utils');

  var authenticate = {
    start: function (options, successCb, failureCb) {
      if (!options || !options.profileId) {
        throw new TypeError("Onegini: missing 'profileId' argument for register.start");
      }
      return utils.promiseOrCallbackExec('OneginiUserAuthenticationClient', 'start', options, successCb, failureCb);
    },

    providePin: function(options, successCb, failureCb) {
      if (!options || !options.pin) {
        throw new TypeError("Onegini: missing 'pin' argument for providePin");
      }
      utils.callbackExec('OneginiUserAuthenticationClient', 'providePin', options, successCb, failureCb);
    }
  };

  var reauthenticate = {
    start: function (options, successCb, failureCb) {
      if (!options || !options.profileId) {
        throw new TypeError("Onegini: missing 'profileId' argument for reauthenticate.start");
      }
      return utils.promiseOrCallbackExec('OneginiUserAuthenticationClient', 'reauthenticate', options, successCb, failureCb);
    },

    providePin: function (options, successCb, failureCb) {
      if (!options || !options.pin) {
        throw new TypeError("Onegini: missing 'pin' argument for reauthenticate.providePin");
      }
      utils.callbackExec('OneginiUserAuthenticationClient', 'providePin', options, successCb, failureCb);
    }
  };

  var register = {
    start: function (options, successCb, failureCb) {
      return utils.promiseOrCallbackExec('OneginiUserRegistrationClient', 'start', options, successCb, failureCb);
    },

    createPin: function (options, successCb, failureCb) {
      if (!options || !options.pin) {
        throw new TypeError("Onegini: missing 'pin' argument for createPin");
      }
      utils.callbackExec('OneginiUserRegistrationClient', 'createPin', options, successCb, failureCb);
    }
  };

  function deregister(options, successCb, failureCb) {
    if (!options || !options.profileId) {
      throw new TypeError("Onegini: missing 'profileId' argument for deregister");
    }
    return utils.promiseOrCallbackExec('OneginiUserDeregistrationClient', 'deregister', options, successCb, failureCb);
  }

  function getUserProfiles(successCb, failureCb) {
    return utils.promiseOrCallbackExec('OneginiUserRegistrationClient', 'getUserProfiles', [], successCb, failureCb);
  }

  return {
    authenticate: authenticate,
    reauthenticate: reauthenticate,
    register: register,
    deregister: deregister,
    getUserProfiles: getUserProfiles
  };
})();
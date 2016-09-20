module.exports = (function () {
  var utils = require('./utils');

  var authenticate = {
    start: function (options, successCb, failureCb) {
      if (!options || !options.profileId) {
        throw new TypeError("Onegini: missing 'profileId' argument for authenticate.start");
      }
      return utils.promiseOrCallbackExec('OneginiUserAuthenticationClient', 'start', options, successCb, failureCb);
    },

    providePin: function(options, successCb, failureCb) {
      if (!options || !options.pin) {
        throw new TypeError("Onegini: missing 'pin' argument for authenticate.providePin");
      }
      return utils.promiseOrCallbackExec('OneginiUserAuthenticationClient', 'providePin', options, successCb, failureCb);
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
      return utils.promiseOrCallbackExec('OneginiUserAuthenticationClient', 'providePin', options, successCb, failureCb);
    }
  };

  var register = {
    start: function (options, successCb, failureCb) {
      return utils.promiseOrCallbackExec('OneginiUserRegistrationClient', 'start', options, successCb, failureCb);
    },

    createPin: function (options, successCb, failureCb) {
      if (!options || !options.pin) {
        throw new TypeError("Onegini: missing 'pin' argument for register.createPin");
      }
      return utils.promiseOrCallbackExec('OneginiUserRegistrationClient', 'createPin', options, successCb, failureCb);
    }
  };

  var changePin = {
    start: function (options, successCb, failureCb) {
      if (!options || !options.pin) {
        throw new TypeError("Onegini: missing 'pin' argument for changePin.start");
      }
      return utils.promiseOrCallbackExec('OneginiChangePinClient', 'start', options, successCb, failureCb);
    },

    createPin: function (options, successCb, failureCb) {
      if (!options || !options.pin) {
        throw new TypeError("Onegini: missing 'pin' argument for changePin.createPin");
      }
      return utils.promiseOrCallbackExec('OneginiChangePinClient', 'createPin', options, successCb, failureCb);
    }
  };

  var authenticators = {
    getRegistered: function (successCb, failureCb) {
      return utils.promiseOrCallbackExec('OneginiAuthenticatorsClient', 'getRegistered', [], successCb, failureCb);
    },

    getNotRegistered: function (successCb, failureCb) {
      return utils.promiseOrCallbackExec('OneginiAuthenticatorsClient', 'getNotRegistered', [], successCb, failureCb);
    },

    setPreferred: function (options, successCb, failureCb) {
      if (!options || !options.authenticatorId) {
        throw new TypeError("Onegini: missing 'authenticatorId' argument for authenticators.setPreferred");
      }
      return utils.promiseOrCallbackExec('OneginiAuthenticatorsClient', 'setPreferred', options, successCb, failureCb);
    },

    registerNew: function (options, successCb, failureCb) {
      if (!options || !options.authenticatorId) {
        throw new TypeError("Onegini: missing 'authenticatorId' argument for authenticators.registerNew");
      }
      return utils.promiseOrCallbackExec('OneginiAuthenticatorRegistrationClient', 'registerNew', options, successCb, failureCb);
    },

    providePin: function (options, successCb, failureCb) {
      if (!options || !options.pin) {
        throw new TypeError("Onegini: missing 'pin' argument for authenticators.providePin");
      }
      return utils.promiseOrCallbackExec('OneginiAuthenticatorRegistrationClient', 'providePin', options, successCb, failureCb);
    }
  };

  var mobileAuthentication = {
    enroll: function (successCb, failureCb) {
      return utils.promiseOrCallbackExec('OneginiMobileAuthenticationClient', 'enroll', [], successCb, failureCb);
    }
  };

  function deregister(options, successCb, failureCb) {
    if (!options || !options.profileId) {
      throw new TypeError("Onegini: missing 'profileId' argument for deregister");
    }
    return utils.promiseOrCallbackExec('OneginiUserDeregistrationClient', 'deregister', options, successCb, failureCb);
  }

  function isUserRegistered(options, successCb, failureCb) {
    if (!options || !options.profileId) {
      throw new TypeError("Onegini: missing 'profileId' argument for isUserRegistered");
    }
    return utils.promiseOrCallbackExec('OneginiUserRegistrationClient', 'isUserRegistered', options, successCb, failureCb);
  }

  function getUserProfiles(successCb, failureCb) {
    return utils.promiseOrCallbackExec('OneginiUserRegistrationClient', 'getUserProfiles', [], successCb, failureCb);
  }

  function getAuthenticatedUserProfile(successCb, failureCb) {
    return utils.promiseOrCallbackExec('OneginiUserAuthenticationClient', 'getAuthenticatedUserProfile', [], successCb, failureCb);
  }

  function logout(successCb, failureCb) {
    return utils.promiseOrCallbackExec('OneginiUserAuthenticationClient', 'logout', [], successCb, failureCb);
  }

  function validatePinWithPolicy(options, successCb, failureCb) {
    if (!options || !options.pin) {
      throw new TypeError("Onegini: missing 'pin' argument for validatePinWithPolicy");
    }
    return utils.promiseOrCallbackExec('OneginiUserClient', 'validatePinWithPolicy', options, successCb, failureCb);
  }

  return {
    authenticate: authenticate,
    reauthenticate: reauthenticate,
    register: register,
    changePin: changePin,
    mobileAuthentication: mobileAuthentication,
    authenticators: authenticators,
    deregister: deregister,
    isUserRegistered: isUserRegistered,
    getUserProfiles: getUserProfiles,
    getAuthenticatedUserProfile: getAuthenticatedUserProfile,
    logout: logout,
    validatePinWithPolicy: validatePinWithPolicy
  };
})();
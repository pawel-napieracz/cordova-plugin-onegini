module.exports = (function () {
  var utils = require('./utils');

  function AuthenticationHandler(options, client) {
    var self = this;
    this.callbacks = {};

    this.callbackActions = {
      providePin: function (options) {
        options = utils.getOptionsWithDefaults(options, {}, 'pin');
        if (!options || !options.pin) {
          throw new TypeError('Onegini: missing "pin" argument for providePin');
        }

        utils.callbackExec(client, 'providePin', options, self.callbacks.onSuccess, self.callbacks.onError);
      },
      fallbackToPin: function () {
        utils.promiseOrCallbackExec(client, 'fallbackToPin');
      },
      acceptFingerprint: function () {
        utils.callbackExec(client, 'respondToFingerprintRequest', {accept: true}, self.callbacks.onSuccess, self.callbacks.onError);
      },
      denyFingerprint: function () {
        utils.callbackExec(client, 'respondToFingerprintRequest', {accept: false}, self.callbacks.onSuccess, self.callbacks.onError)
      },
      createPin: function (options) {
        options = utils.getOptionsWithDefaults(options, {}, 'pin');
        if (!options || !options.pin) {
          throw new TypeError('Onegini: missing "pin" argument for createPin');
        }

        utils.callbackExec(client, 'createPin', options, self.callbacks.onSuccess, self.callbacks.onError);
      }
    };

    function callSuccessCallback(options) {
      var method = options.authenticationMethod;
      delete options.authenticationMethod;

      self.callbacks[method](self.callbackActions, options);
    }

    function callErrorCallback(err) {
      self.callbacks.onError(err);
    }

    utils.callbackExec(client, 'start', options, callSuccessCallback, callErrorCallback)
  }

  AuthenticationHandler.prototype.onPinRequest = function (cb) {
    this.callbacks.onPinRequest = cb;
    return this;
  };

  AuthenticationHandler.prototype.onCreatePinRequest = function (cb) {
    this.callbacks.onCreatePinRequest = cb;
    return this;
  };

  AuthenticationHandler.prototype.onFingerprintRequest = function (cb) {
    this.callbacks.onFingerprintRequest = cb;
    return this;
  };

  AuthenticationHandler.prototype.onFingerprintCaptured = function (cb) {
    this.callbacks.onFingerprintCaptured = cb;
    return this;
  };

  AuthenticationHandler.prototype.onFingerprintFailed = function (cb) {
    this.callbacks.onFingerprintFailed = cb;
    return this;
  };

  AuthenticationHandler.prototype.onError = function (cb) {
    this.callbacks.onError = cb;
    return this;
  };

  AuthenticationHandler.prototype.onSuccess = function (cb) {
    this.callbacks.onSuccess = cb;
    return this;
  };

  var authenticate = function (options) {
    options = utils.getOptionsWithDefaults(options, {}, 'profileId');
    if (!options || !options.profileId) {
      throw new TypeError("Onegini: missing 'profileId' argument for user.authenticate");
    }

    return new AuthenticationHandler(options, 'OneginiUserAuthenticationClient');
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

  var changePin = function () {
    return new AuthenticationHandler(null, 'OneginiChangePinClient');
  };

  var authenticators = {
    getRegistered: function (successCb, failureCb) {
      return utils.promiseOrCallbackExec('OneginiAuthenticatorsClient', 'getRegistered', [], successCb, failureCb);
    },

    getNotRegistered: function (successCb, failureCb) {
      return utils.promiseOrCallbackExec('OneginiAuthenticatorsClient', 'getNotRegistered', [], successCb, failureCb);
    },

    setPreferred: function (options, successCb, failureCb) {
      options = utils.getOptionsWithDefaults(options, {}, 'authenticatorId');
      if (!options || !options.authenticatorId) {
        throw new TypeError("Onegini: missing 'authenticatorId' argument for authenticators.setPreferred");
      }
      return utils.promiseOrCallbackExec('OneginiAuthenticatorsClient', 'setPreferred', options, successCb, failureCb);
    },

    registerNew: function (options) {
      options = utils.getOptionsWithDefaults(options, {}, 'authenticatorId');
      if(!options || !options.authenticatorId) {
        throw new TypeError("Onegini: missing 'authenticatorId' argument for authenticators.registerNew");
      }

      return new AuthenticationHandler(options, 'OneginiAuthenticatorRegistrationClient');
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
    authenticators: authenticators,
    deregister: deregister,
    isUserRegistered: isUserRegistered,
    getUserProfiles: getUserProfiles,
    getAuthenticatedUserProfile: getAuthenticatedUserProfile,
    logout: logout,
    validatePinWithPolicy: validatePinWithPolicy
  };
})();
module.exports = (function () {
  var utils = require('./utils');

  function enroll(successCb, failureCb) {
    return utils.promiseOrCallbackExec('OneginiMobileAuthenticationClient', 'enroll', [], successCb, failureCb);
  }

  function MobileAuthenticationHandler(method) {
    var self = this;
    this.callbacks = {};

    this.callbackActions = {
      accept: function (options) {
        options = utils.getOptionsWithDefaults(options, {}, 'pin');
        options.accept = true;
        utils.promiseOrCallbackExec('OneginiMobileAuthenticationRequestClient', 'replyToChallenge', options, self.callbacks.onSuccess, self.callbacks.onError);
      },

      deny: function () {
        var options = {
          accept: false
        };

        utils.promiseOrCallbackExec('OneginiMobileAuthenticationRequestClient', 'replyToChallenge', options, self.callbacks.onSuccess, self.callbacks.onError);
      }
    };

    function callSuccessCallback(options) {
      var event = options.authenticationEvent;
      delete options.authenticationEvent;

      if (self.callbacks[event]) {
        self.callbacks[event](self.callbackActions, options);
      }
    }

    function callErrorCallback(err) {
      self.callbacks.onError(err);
    }

    utils.callbackExec('OneginiMobileAuthenticationRequestClient', 'registerChallengeReceiver', {method: method}, callSuccessCallback, callErrorCallback);
  }

  MobileAuthenticationHandler.prototype.onConfirmationRequest = function (cb) {
    this.callbacks.onConfirmationRequest = cb;
    return this;
  };

  MobileAuthenticationHandler.prototype.onPinRequest = function (cb) {
    this.callbacks.onPinRequest = cb;
    return this;
  };

  MobileAuthenticationHandler.prototype.onFingerprintRequest = function (cb) {
    this.callbacks.onFingerprintRequest = cb;
    return this;
  };


  MobileAuthenticationHandler.prototype.onFingerprintCaptured = function (cb) {
    this.callbacks.onFingerprintCaptured = cb;
    return this;
  };

  MobileAuthenticationHandler.prototype.onFingerprintFailed = function (cb) {
    this.callbacks.onFingerprintFailed = cb;
    return this;
  };

  MobileAuthenticationHandler.prototype.onError = function (cb) {
    this.callbacks.onError = cb;
    return this;
  };

  MobileAuthenticationHandler.prototype.onSuccess = function (cb) {
    this.callbacks.onSuccess = cb;
    return this;
  };

  function on(method) {
    return new MobileAuthenticationHandler(method);
  }

  return {
    enroll: enroll,
    on: on
  }
})();
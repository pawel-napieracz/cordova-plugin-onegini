module.exports = (function () {
  var utils = require('./utils');

  function enroll(successCb, failureCb) {
    return utils.promiseOrCallbackExec('OneginiMobileAuthenticationClient', 'enroll', [], successCb, failureCb);
  }

  function MobileAuthenticationHandler(method) {
    var self = this;
    this.callbacks = {};
    this.method = method;

    function acceptRequest(options) {
      options = utils.getOptionsWithDefaults(options, {}, 'pin');
      options.accept = true;
      options.method = self.method;
      utils.promiseOrCallbackExec('OneginiMobileAuthenticationRequestClient', 'replyToChallenge', options, callSuccess, self.callbacks.catch);
    }

    function rejectRequest() {
      var options = {
        accept: false,
        method: self.method
      };

      utils.promiseOrCallbackExec('OneginiMobileAuthenticationRequestClient', 'replyToChallenge', options, callSuccess, self.callbacks.catch);
    }

    function callChallengeReceiver(request) {
      if (request.mobileAuthenticationEvent) {
        var eventName = request.mobileAuthenticationEvent;
        delete request.mobileAuthenticationEvent;

        if (self.callbacks["on" + eventName]) {
          self.callbacks["on" + eventName]();
        }
      }
      else {
        self.callbacks.challengeReceiver(request, acceptRequest, rejectRequest);
      }
    }

    function callSuccess() {
      if (self.callbacks.success) {
        self.callbacks.success();
      }
    }

    utils.callbackExec('OneginiMobileAuthenticationRequestClient', 'registerChallengeReceiver', {method: self.method}, callChallengeReceiver, this.callbacks.catch);
  }

  MobileAuthenticationHandler.prototype.shouldAccept = function (cb) {
    this.callbacks.challengeReceiver = cb;
    return this;
  };

  MobileAuthenticationHandler.prototype.providePin = function (cb) {
    this.callbacks.challengeReceiver = cb;
    return this;
  };

  MobileAuthenticationHandler.prototype.onFingerprintCaptured = function (cb) {
    this.callbacks.onFingerprintCaptured = cb;
    return this;
  };

  MobileAuthenticationHandler.prototype.onFingerprintNextAttempt = function (cb) {
    this.callbacks.onFingerprintNextAttempt = cb;
    return this;
  };

  MobileAuthenticationHandler.prototype.catch = function (cb) {
    this.callbacks.catch = cb;
    return this;
  };

  MobileAuthenticationHandler.prototype.success = function (cb) {
    this.callbacks.success = cb;
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
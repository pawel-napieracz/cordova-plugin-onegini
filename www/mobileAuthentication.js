module.exports = (function () {
  var utils = require('./utils');

  function enroll(successCb, failureCb) {
    return utils.promiseOrCallbackExec('OneginiMobileAuthenticationClient', 'enroll', [], successCb, failureCb);
  }

  function MobileAuthenticationHandler(method) {
    var self = this;
    this.callbacks = {};

    function done() {
      if (self.callbacks.done) {
        self.callbacks.done();
      }
    }

    function acceptRequest() {
      utils.promiseOrCallbackExec('OneginiMobileAuthenticationRequestClient', 'replyToConfirmationChallenge', {accept: true}, done, self.callbacks.catch);
    }

    function rejectRequest() {
      utils.promiseOrCallbackExec('OneginiMobileAuthenticationRequestClient', 'replyToConfirmationChallenge', {accept: false}, done, self.callbacks.catch);
    }

    function determineAccept(request) {
      if (self.callbacks.shouldAccept) {
        self.callbacks.shouldAccept(request, acceptRequest, rejectRequest);
      }
      else {
        rejectRequest();
      }
    }

    utils.callbackExec('OneginiMobileAuthenticationRequestClient', 'registerChallengeReceiver', {method: method}, determineAccept, this.callbacks.catch);
  }

  MobileAuthenticationHandler.prototype.shouldAccept = function (shouldAcceptCb) {
    this.callbacks.shouldAccept = shouldAcceptCb;
    return this;
  };

  MobileAuthenticationHandler.prototype.catch = function (catchCb) {
    this.callbacks.catch = catchCb;
    return this;
  };

  // TODO: Rename success cb
  MobileAuthenticationHandler.prototype.done = function (doneCb) {
    this.callbacks.done = doneCb;
    return this;
  };

  function on(type) {
    return new MobileAuthenticationHandler(type.toUpperCase());
  }

  return {
    enroll: enroll,
    on: on
  }
})();
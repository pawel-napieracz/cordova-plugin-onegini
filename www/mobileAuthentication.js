module.exports = (function () {
  var utils = require('./utils');

  function enroll(successCb, failureCb) {
    return utils.promiseOrCallbackExec('OneginiMobileAuthenticationClient', 'enroll', [], successCb, failureCb);
  }

  function PushHandler() {
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

    utils.callbackExec('OneginiMobileAuthenticationRequestClient', 'registerConfirmationChallengeReceiver', [], determineAccept, this.callbacks.catch);
  }

  PushHandler.prototype.shouldAccept = function (shouldAcceptCb) {
    this.callbacks.shouldAccept = shouldAcceptCb;
  };

  PushHandler.prototype.catch = function (catchCb) {
    this.callbacks.catch = catchCb;
  };

  PushHandler.prototype.done = function (doneCb) {
    this.callbacks.done = doneCb;
  };

  function on(type) {
    if(type === "push") {
      return new PushHandler();
    }
  }

  return {
    enroll: enroll,
    on: on
  }
})();
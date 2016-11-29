/*
 * Copyright (c) 2016 Onegini B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

  MobileAuthenticationHandler.prototype.onFidoRequest = function (cb) {
    this.callbacks.onFidoRequest = cb;
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
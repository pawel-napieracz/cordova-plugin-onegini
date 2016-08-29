module.exports = (function () {
  var utils = require('./utils');

  function startRegistration(options, successCb, failureCb) {
    return utils.promiseOrCallbackExec('OneginiUserClient', 'startRegistration', options.scopes, successCb, failureCb);
  }

  function createPIN(options, successCb, failureCb) {
    if (!successCb) {
      throw new TypeError("Onegini: missing argument for createPIN: no success callback provided");
    }
    utils.promiseOrCallbackExec('OneginiUserClient', 'createPIN', options.pin, successCb, failureCb);
  }

  return {
    startRegistration: startRegistration,
    createPIN: createPIN
  };
})();
module.exports = (function () {
  var utils = require('./utils');

  function generateRegistrationChallenge(options, successCb, failureCb) {
    return utils.promiseOrCallbackExec('OneginiUserRegistrationClient', 'generateChallenge', options, successCb, failureCb);
  }

  function setPIN(options, successCb, failureCb) {
    return utils.promiseOrCallbackExec('OneginiUserRegistrationClient', 'setPIN', options, successCb, failureCb);
  }

  return {
    generateRegistrationChallenge: generateRegistrationChallenge,
    setPIN: setPIN
  };
})();
module.exports = (function () {
  var user = require('./user');
  var device = require('./device');
  var utils = require('./utils');

  function start(successCb, failureCb) {
    return utils.promiseOrCallbackExec('OneginiClient', 'start', false, successCb, failureCb);
  }

  return {
    start: start,
    user: user,
    device: device
  };
})();
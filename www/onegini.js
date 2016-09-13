module.exports = (function () {
  var user = require('./user');
  var device = require('./device');
  var utils = require('./utils');
  var resource = require('./resource');

  function start(options, successCb, failureCb) {
    var promise, callbackResult;

    if (typeof (options) === 'function') {
      failureCb = successCb;
      successCb = options;
      options = undefined;
    }

    options = options || {};

    if (!successCb) {
      promise = new Promise(function (resolve, reject) {
        successCb = resolve;
        failureCb = reject;
      });
    }

    callbackResult = utils.callbackExec('OneginiClient', 'start', [], function (config) {
          if (options.secureXhr === true) {
            resource.init(config.resourceBaseURL);
          }
          successCb();
        },
        failureCb
    );

    return promise || callbackResult;
  }

  return {
    start: start,
    user: user,
    device: device,
    resource: resource
  };
})();
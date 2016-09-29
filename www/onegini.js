module.exports = (function () {
  var user = require('./user');
  var device = require('./device');
  var utils = require('./utils');
  var resource = require('./resource');
  var mobileAuthentication = require('./mobileAuthentication');

  function start(options, successCb, failureCb) {
    var promise, callbackResult;

    options = utils.getOptionsWithDefaults(options, {
      secureXhr: false
    });

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
    resource: resource,
    mobileAuthentication: mobileAuthentication
  };
})();
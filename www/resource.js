module.exports = (function (open) {
  var utils = require('./utils');

  function fetch(options, successCb, failureCb) {
    options = options || {};

    if (typeof(options) === 'string') {
      options = {
        url: options
      }
    }

    options.method = options.method || 'GET';
    options.headers = options.headers || {};
    options.body = options.body || '';

    return utils.promiseOrCallbackExec('OneginiResourceClient', 'fetch', options, successCb, failureCb);
  }

  function init(resourceBaseURL) {
    XMLHttpRequest.prototype.open = function (method, url) {
      if (url.startsWith(resourceBaseURL)) {
        interceptXhr(this, method, url);
      }

      open.apply(this, arguments);
    };
  }

  function disable() {
    XMLHttpRequest.prototype.open = open;
  }

  function interceptXhr(xhr, method, url) {
    xhr._requestHeaders = {};
    xhr._responseHeaders = {};
    xhr._method = method;
    xhr._url = url;

    xhr.send = function (body) {
      fetch({
        method: xhr._method,
        url: xhr._url,
        headers: xhr._requestHeaders,
        body: body
      }, function (successResult) {
        populateXhrWithFetchResult(xhr, successResult);
        triggerEvent(xhr, 'load');
      }, function (failureResult) {
        populateXhrWithFetchResult(xhr, failureResult);
        triggerEvent(xhr, 'error');
      });
    };

    xhr.getAllResponseHeaders = function () {
      var headersString = '';

      for (var header in xhr._responseHeaders) {
        headersString += header + ': ' + xhr._responseHeaders[header] + '\n';
      }

      return headersString;
    };

    xhr.getResponseHeader = function (header) {
      return xhr._responseHeaders[header];
    };

    xhr.setRequestHeader = function (header, value) {
      xhr._requestHeaders[header] = value;
    };
  }

  function populateXhrWithFetchResult(xhr, result) {
    defineProperty(xhr, 'readyState', 4);
    defineProperty(xhr, 'responseText', result.body);
    defineProperty(xhr, 'status', result.status);
    defineProperty(xhr, 'statusText', result.statusText);
    xhr._responseHeaders = result.headers;
  }

  function triggerEvent(xhr, eventName) {
    if (xhr.onreadystatechange) {
      xhr.onreadystatechange();
    }

    if (xhr['on' + eventName]) {
      xhr['on' + eventName]();
    }

    var event = new Event(eventName);
    xhr.dispatchEvent(event);
  }

  function defineProperty(object, property, value) {
    Object.defineProperty(object, property, {
      writable: true,
      value: value
    });
  }

  return {
    fetch: fetch,
    init: init,
    disable: disable
  };

})(XMLHttpRequest.prototype.open);
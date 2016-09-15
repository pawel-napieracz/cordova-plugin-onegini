module.exports = (function (XMLHttpRequest) {
  var utils = require('./utils');
  var ResourceBaseURL;
  var nativeXhrProperties = [
    'onabort',
    'onerror',
    'onload',
    'onloadend',
    'onloadstart',
    'onprogress',
    'onreadystatechange',
    'ontimeout',
    'readyState',
    'response',
    'responseText',
    'responseType',
    'responseURL',
    'responseXML',
    'status',
    'statusText',
    'timeout',
    'upload',
    'XMLHttpRequestUpload',
    'withCredentials'
  ];

  var nativeXhrMethods = [
    'abort',
    'getAllResponseHeaders',
    'getResponseHeader',
    'overrideMimeType',
    'send',
    'setRequestHeader'
  ];

  function fetch(options, successCb, failureCb) {
    options = utils.getOptionsWithDefaults(options, {
      method: 'GET',
      headers: {},
      anonymous: false
    }, 'url');

    if (!options || !options.url) {
      throw new TypeError("Onegini: missing 'url' argument for fetch");
    }

    return utils.promiseOrCallbackExec('OneginiResourceClient', 'fetch', options, successCb, failureCb);
  }

  function init(url) {
    window.XMLHttpRequest = OneginiXMLHttpRequest;
    ResourceBaseURL = url;
  }

  function disable() {
    window.XMLHttpRequest = XMLHttpRequest;
  }

  function OneginiXMLHttpRequest() {
    this._eventListeners = {};
    this.xhr = new XMLHttpRequest();
  }

  OneginiXMLHttpRequest.prototype.open = function (method, url) {
    if (url.startsWith(ResourceBaseURL)) {
      setupXhrProxy(this, method, url);
    }

    this.xhr.open.apply(this.xhr, arguments);
  };

  OneginiXMLHttpRequest.prototype.addEventListener = function (type, listener, options) {
    this._eventListeners[type] = this._eventListeners[type] || [];
    this._eventListeners[type].push(listener);
    return this.xhr.addEventListener.apply(this.xhr, arguments);
  };

  OneginiXMLHttpRequest.prototype.removeEventListener = function (type, listener) {
    var stack = this._eventListeners[type];

    if (stack) {
      for (var i = 0, l = stack.length; i < l; i++) {
        if (stack[i] === listener) {
          stack.splice(i, 1);
          return this.removeEventListener(type, listener);
        }
      }
    }

    return this.xhr.removeEventListener.apply(this.xhr, arguments);
  };

  OneginiXMLHttpRequest.prototype.dispatchEvent = function (event) {
    var stack = this._eventListeners[event.type];
    var dispatchNative = true;

    if (this['on' + event.type]) {
      dispatchNative = false;
      this['on' + event.type].call(this);
    }

    if (stack && stack.length !== 0) {
      dispatchNative = false;
      event.target = this;
      for (var i = 0, l = stack.length; i < l; i++) {
        stack[i].call(this, event);
      }
    }

    if (dispatchNative) {
      return this.xhr.dispatchEvent.apply(this.xhr, arguments);
    } else if (this.onreadystatechange) {
      this.onreadystatechange();
    }
  };

  nativeXhrProperties.forEach(function (property) {
    linkXhrPropertyWithOneginiXhr(property);
  });

  nativeXhrMethods.forEach(function (method) {
    linkXhrMethodWithOneginiXhr(method);
  });

  function linkXhrPropertyWithOneginiXhr(key) {
    Object.defineProperty(OneginiXMLHttpRequest.prototype, key, {
      get: function () {
        return this.xhr[key];
      },
      set: function (value) {
        return this.xhr[key] = value;
      }
    });
  }

  function linkXhrMethodWithOneginiXhr(name) {
    Object.defineProperty(OneginiXMLHttpRequest.prototype, name, {
      value: function () {
        return this.xhr[name].apply(this.xhr, arguments);
      }
    });
  }

  function setupXhrProxy(xhr, method, url) {
    xhr._requestHeaders = {};
    xhr._responseHeaders = {};
    xhr._method = method;
    xhr._url = url;

    defineProperty(xhr, 'send', function (body) {
      fetch({
        method: xhr._method,
        url: xhr._url,
        headers: xhr._requestHeaders,
        body: body
      }, function (successResult) {
        populateXhrWithFetchResult(xhr, successResult);
        xhr.dispatchEvent(new Event('load'));
      }, function (errorResult) {
        populateXhrWithFetchResult(xhr, errorResult);
        xhr.dispatchEvent(new Event('error'));
      });
    });

    defineProperty(xhr, 'getAllResponseHeaders', function () {
      var headersString = '';

      for (var header in xhr._responseHeaders) {
        headersString += header + ': ' + xhr._responseHeaders[header] + '\n';
      }

      return headersString;
    });

    defineProperty(xhr, 'getResponseHeader', function (header) {
      return xhr._responseHeaders[header];
    });

    defineProperty(xhr, 'setRequestHeader', function (header, value) {
      xhr._requestHeaders[header] = value;
    });
  }

  function populateXhrWithFetchResult(xhr, result) {
    defineProperty(xhr, 'readyState', 4);
    defineProperty(xhr, 'responseText', result.body);
    defineProperty(xhr, 'response', result.body);
    defineProperty(xhr, 'status', result.status);
    defineProperty(xhr, 'statusText', result.statusText);
    xhr._responseHeaders = result.headers;
  }

  function defineProperty(object, property, value) {
    Object.defineProperty(object, property, {
      configurable: true,
      value: value
    });
  }

  return {
    fetch: fetch,
    init: init,
    disable: disable
  };

})(XMLHttpRequest);
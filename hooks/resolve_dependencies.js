/*
 * Copyright (c) 2017 Onegini B.V.
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

const fs = require('fs');
const url = require('url');
const https = require('https');
const exec = require('child_process').exec;
const execSync = require('child_process').execSync;
const debug = require('debug')('resolve_dependencies');

const sdkDownloadPathEnv = process.env.SDK_DOWNLOAD_PATH;
const sdkVersion = '8.0.0';

const libOneginiSDKiOS = 'https://repo.onegini.com/artifactory/onegini-sdk/com/onegini/mobile/sdk/ios/libOneginiSDKiOS/' + sdkVersion + '/libOneginiSDKiOS-' + sdkVersion + '.a';
const libOneginiSDKiOSHeaders = 'https://repo.onegini.com/artifactory/onegini-sdk/com/onegini/mobile/sdk/ios/libOneginiSDKiOS/' + sdkVersion + '/libOneginiSDKiOS-' + sdkVersion + '-headers.zip';
const libName = libOneginiSDKiOS.substring(libOneginiSDKiOS.lastIndexOf('/') + 1);
const headersName = libOneginiSDKiOSHeaders.substring(libOneginiSDKiOSHeaders.lastIndexOf('/') + 1);

module.exports = function (context) {
  const pluginId = 'cordova-plugin-onegini';
  const deferral = context.requireCordovaModule('q').defer();
  const platform = context.opts.plugin.platform;

  // We only want to invoke the plugin for the iOS platform since it doesn't make any sense to resolve the iOS SDK dependencies when
  // you only have the Android platform installed.
  if (platform !== 'ios') {
    return;
  }

  console.log(`${pluginId}: Resolving Onegini iOS SDK dependencies...`);

  createSDKDirectory(context);

  return checkDownloadedSDKFile(context);
};

function downloadSDK(context) {
  var libPromise = addLibPromise(context);
  var headersPromise = addHeadersPromise(context);

  return Promise.all([libPromise, headersPromise]);
}

function addLibPromise(context) {
  return new Promise(function() {
    requestPromise(libOneginiSDKiOS, context)
      .then(copyAndRenameSDK(context, libName));
  });
}

function addHeadersPromise(context) {
  return new Promise(function() {
    requestPromise(libOneginiSDKiOSHeaders, context)
      .then(function () {
        unzipAndRenameHeadersPromise(context, headersName)
      });
  });
}

function checkDownloadedSDKFile(context) {
    var libFilePath = sdkDownloadPathEnv + '/' + libName;
    var headersFilePath = sdkDownloadPathEnv + '/' + headersName;

    if (fs.existsSync(libFilePath) && fs.existsSync(headersFilePath)) {
      debug('Validation downloaded SDK files');
        var checkLibHash = checkLibHashPromise(context, libFilePath);
        var checkHeadersHash = checkHeadersHashPromise(context, headersFilePath);

        return Promise.all([checkLibHash, checkHeadersHash]);
    } else {
      return downloadSDK(context);
    }
}

function checkLibHashPromise(context, libFilePath) {
  return new Promise(function() {
    debug('Static library validation');
    var libOneginiSDKiOSSHA256 = libOneginiSDKiOS + '.sha256';
    requestPromise(libOneginiSDKiOSSHA256, context)
      .then(function () {
        generateSHA256(libFilePath)
          .then(function (generatedLibHash) {
            var librarySHA256File = libFilePath + '.sha256';
            var libResult = compareHashes(generatedLibHash, librarySHA256File);
            if (libResult) {
              debug('Static library validation succeed');
              return copyAndRenameSDK(context, libName);
            } else {
              debug('Static library validation failed');
              return addLibPromise(context);
            }
          });
      });
  });
}

function checkHeadersHashPromise(context, headersFilePath) {
  debug('Header files validation');
  return new Promise(function() {
    var libOneginiSDKiOSHeadersSHA256 = libOneginiSDKiOSHeaders + '.sha256';
    requestPromise(libOneginiSDKiOSHeadersSHA256, context)
      .then(function () {
        generateSHA256(headersFilePath)
          .then(function (generatedHeadersHash) {
            var headersSHA256File = headersFilePath + '.sha256';
            var headersResult = compareHashes(generatedHeadersHash, headersSHA256File);
            if (headersResult) {
              debug('Header files validation succeed');
              return unzipAndRenameHeadersPromise(context, headersName);
            } else {
              debug('Header files validation failed');
              return addHeadersPromise(context);
            }
          });
      });
  });
}

function compareHashes(hash, hashFile) {
  var downlandedHash = fs.readFileSync(hashFile).toString();
  debug('Comparing hashes: ' + downlandedHash + ' / ' + hash);
  return downlandedHash === hash;
}

function generateSHA256(filepath) {
  return new Promise(function(resolve) {
    debug('Generating sha256 from: ' + filepath);
    var result = execSync('openssl sha256 ' + filepath);
    var hash = result.toString().substring(result.toString().lastIndexOf('=') + 1);
    resolve(hash.trim());
  });
}

function requestPromise(fileURL, context) {
  return new Promise(function(resolve, reject) {
    var username = process.env.ARTIFACTORY_USERNAME;
    var password = process.env.ARTIFACTORY_PASSWORD;
    var sdkDownloadPath = (sdkDownloadPathEnv !== 'undefined') ? sdkDownloadPathEnv : context.opts.plugin.pluginInfo.dir;
    if (username === 'undefined' || password === 'undefined' ) {
      console.error('You must configure the artifactory_user and artifactory_password properties in your project before you can build it.');
      reject();
    }
    var filename = fileURL.substring(fileURL.lastIndexOf('/') + 1);
    debug('Downloading: ' + filename);
    var auth = Buffer.from(username + ':' + password).toString();
    var filePath = sdkDownloadPath + '/' + filename;
    var file = fs.createWriteStream(filePath);
    var options = {
      protocol: url.parse(fileURL).protocol,
      host: url.parse(fileURL).host,
      path: url.parse(fileURL).path,
      auth: auth
    };
    https.get(options, function(response) {
      if (response.statusCode === 200) {
        response.pipe(file);
        file.on('finish', function () {
          file.close();
          debug('File downloaded: ' + filename);
          var fileFormat = filename.substring(filename.lastIndexOf('.') + 1);
          if (fileFormat !== 'sha256') {
            debug(filename + ' validation');
            checkHashDownloadedFile(options, sdkDownloadPath, filename).then(function () {
              resolve();
            });
          } else {
            resolve();
          }
        });
      } else if(response.statusCode == 404) {
        console.error(filename + ' not found in artifactory repository')
      } else if(response.statusCode == 401) {
        console.error(username + ' is unauthorized')
      } else {
        console.error('Downloading' + filename + 'went wrong: ' + response.statusCode);
        fs.unlinkSync(filePath);
      }

    }).on('error', function(err) {
      fs.unlinkSync(filePath);
      console.error('Downloading' + filename + 'went wrong: ' + err);
    });
  });
}

function downloadHashFile(options, sdkDownloadPath, fileName) {
  return new Promise(function(resolve) {
    var downloadedFilePath = options.path;
    options.path = downloadedFilePath + '.sha256';
    var filename = fileName + '.sha256';
    debug('Downloading: ' + filename);
    var filePath = sdkDownloadPath + '/' + filename;
    var file = fs.createWriteStream(filePath);

    https.get(options, function (response) {
      if (response.statusCode === 200) {
        response.pipe(file);
        file.on('finish', function () {
          debug('File downloaded: ' + filename);
          file.close();
          resolve();
        });
      } else if(response.statusCode == 404){
        console.error(filename + ' not found in artifactory repository')
      } else if(response.statusCode == 401) {
        console.error(username + ' is unauthorized')
      } else {
        console.error('Downloading' + filename + 'went wrong: ' + response.statusCode);
        fs.unlinkSync(filePath);
      }

    }).on('error', function (err) {
      fs.unlinkSync(filePath);
      console.error('Downloading' + filename + 'went wrong: ' + err);
    });
  });
}

function checkHashDownloadedFile(options, sdkDownloadPath, fileName) {
  var downloadedFilePath = sdkDownloadPath + '/' + fileName;
  return new Promise(function(resolve) {
    downloadHashFile(options, sdkDownloadPath, fileName)
      .then(function () {
        generateSHA256(downloadedFilePath)
          .then(function (generatedHash) {
            var result = compareHashes(generatedHash, downloadedFilePath + '.sha256');
            if (result) {
              debug('Validation succeed');
              resolve();
            } else {
              debug('Downloaded file is damaged. Please try again.');
            }
          });
      });
    });
}

function createSDKDirectory(context) {
  var sdkDir = context.opts.plugin.pluginInfo.dir + '/src/ios/OneginiSDKiOS';
  debug('Create SDK directory : ' + sdkDir);
  var headersDir = sdkDir + '/Headers';
  fs.mkdirSync(sdkDir);
  fs.mkdirSync(headersDir);
}

function copyAndRenameSDK(context, filename) {
  debug('Copying and renaming SDK library');
  var pluginDir = context.opts.plugin.pluginInfo.dir;
  var downloadedPluginDir = (sdkDownloadPathEnv !== 'undefined') ? sdkDownloadPathEnv : pluginDir;
  fs.createReadStream(downloadedPluginDir + '/' + filename).pipe(fs.createWriteStream(pluginDir + '/src/ios/OneginiSDKiOS/libOneginiSDKiOS.a'));
}

function unzipAndRenameHeadersPromise(context, filename) {
  return new Promise(function(resolve) {
    debug('Unzipping and renaming headers');

    var pluginDir = context.opts.plugin.pluginInfo.dir;
    var downloadedPluginDir = (sdkDownloadPathEnv !== 'undefined') ? sdkDownloadPathEnv : pluginDir;
    var newDir = pluginDir + '/src/ios/OneginiSDKiOS/Headers';
    try {
      execSync('unzip ' + downloadedPluginDir + '/' + filename + ' -d ' + newDir);
      resolve();
    } catch (ex) {
      console.error('Extracting archive failed.');
    }
  });
}

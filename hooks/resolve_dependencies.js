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
const path = require('path');
const process = require('process');
const crypto = require('crypto');
const url = require('url');
const https = require('https');
const execSync = require('child_process').execSync;
const debug = require('debug')('resolve_dependencies');

const pluginId = 'cordova-plugin-onegini';

const envVariables = {
  artifactoryUser: 'ARTIFACTORY_USER',
  artifactoryPassword: 'ARTIFACTORY_PASSWORD',
  sdkDownloadPath: 'ONEGINI_SDK_IOS_DOWNLOAD_PATH'
};

const sdkVersion = '8.0.0';

const baseArtifactoryUrl = `https://repo.onegini.com/artifactory/onegini-sdk/com/onegini/mobile/sdk/ios/libOneginiSDKiOS/${sdkVersion}`;
const libOneginiSdkIos = `${baseArtifactoryUrl}/libOneginiSDKiOS-${sdkVersion}.a`;
const libOneginiSdkIosHeaders = `${baseArtifactoryUrl}/libOneginiSDKiOS-${sdkVersion}-headers.zip`;

const libName = libOneginiSdkIos.substring(libOneginiSdkIos.lastIndexOf('/') + 1);
const headersName = libOneginiSdkIosHeaders.substring(libOneginiSdkIosHeaders.lastIndexOf('/') + 1);

const iosSdkPathCordova = 'src/ios/OneginiSDKiOS';
const iosSdkLibPathCordova = path.join(iosSdkPathCordova, 'libOneginiSDKiOS.a');
const iosSdkHeadersPathCordova = path.join(iosSdkPathCordova, 'Headers');

let sdkDownloadPath;

module.exports = function (context) {
  const platform = context.opts.plugin.platform;

  // We only want to invoke the plugin for the iOS platform since it doesn't make any sense to resolve the iOS SDK dependencies when
  // you only have the Android platform installed.
  if (platform !== 'ios') {
    return;
  }

  fetchSdkDownloadPath(context);
  prepareSdkDirectories(context);

  writeToStdOut(`${pluginId}: Resolving Onegini iOS SDK dependencies...`);

  return new Promise((resolve, reject) => {
    // Downloading & verifying the SDK lib
    checkSdkLibExistsOnFs()
      .then(result => downloadFile(result, libOneginiSdkIos))
      .then(() => checkDownloadedFileIntegrity(libOneginiSdkIos))
      .then(() => copyAndRenameSdkLib(context))

      // Downloading & verifying the headers ZIP
      .then(() => checkHeadersFileExistsOnFs())
      .then((result) => downloadFile(result, libOneginiSdkIosHeaders))
      .then(() => checkDownloadedFileIntegrity(libOneginiSdkIosHeaders))
      .then(() => unzipAndRenameHeaders(context))
      .then(() => {
        writeToStdOut('Success!\n');
        resolve();
      })
      .catch((err) => {
        reject(err);
      });
  });
};

function fetchSdkDownloadPath(context) {
  const pluginDir = context.opts.plugin.pluginInfo.dir;
  const sdkDownloadPathVar = envVariables.sdkDownloadPath;
  const sdkDownloadPathFromEnv = process.env[sdkDownloadPathVar];

  if (sdkDownloadPathFromEnv) {
    log(`Downloading the Onegini iOS SDK to: '${sdkDownloadPathFromEnv}' set in '${sdkDownloadPathVar}'`);
    sdkDownloadPath = sdkDownloadPathFromEnv;
  }
  else {
    log(`Downloading the Onegini iOS SDK to: '${pluginDir}'`);
    sdkDownloadPath = path.join(pluginDir, 'ios-sdk');
  }
}

function checkSdkLibExistsOnFs() {
  return new Promise(resolve => {
    writeToStdOut('.');
    const libFilePath = path.join(sdkDownloadPath, libName);
    if (fs.existsSync(libFilePath)) {
      debug('SDK lib is already downloaded');
      resolve(true);
    }
    else {
      debug('SDK lib is not downloaded yet');
      resolve(false);
    }
  });
}

function checkHeadersFileExistsOnFs() {
  return new Promise(resolve => {
    writeToStdOut('.');
    const headersFilePath = path.join(sdkDownloadPath, headersName);
    if (fs.existsSync(headersFilePath)) {
      debug('Headers ZIP is already downloaded');
      resolve(true);
    }
    else {
      debug('Headers ZIP is not downloaded yet');
      resolve(false);
    }
  });
}

function calculateSha256(filepath) {
  return new Promise(resolve => {
    debug(`Generating sha256 from: ${filepath}`);
    const shasum = crypto.createHash('sha256');

    let readStream = fs.createReadStream(filepath);
    readStream.on('data', (chunk) => {
      shasum.update(chunk);
    });
    readStream.on('end', () => {
      let hash = shasum.digest('hex');
      debug(`hashing done: ${hash}`);
      resolve(hash);
    })
  });
}

function downloadFile(fileExists, fileUrl) {
  return new Promise((resolve, reject) => {
    writeToStdOut('.');
    const username = process.env[envVariables.artifactoryUser];
    const password = process.env[envVariables.artifactoryPassword];
    if (!username || !password) {
      reject('You must set the "ARTIFACTORY_USER" and "ARTIFACTORY_PASSWORD" environment variables in order to resolve the iOS SDK dependency.');
      return;
    }
    const filename = fileUrl.substring(fileUrl.lastIndexOf('/') + 1);

    // Check if the file was already downloaded previously.
    if (fileExists) {
      resolve();
      return;
    }

    debug(`Downloading: ${filename}`);
    const auth = Buffer.from(`${username}:${password}`).toString();
    const filePath = path.join(sdkDownloadPath, filename);
    const file = fs.createWriteStream(filePath);
    const options = {
      protocol: url.parse(fileUrl).protocol,
      host: url.parse(fileUrl).host,
      path: url.parse(fileUrl).path,
      auth: auth
    };
    https.get(options, (response) => {
      if (response.statusCode === 200) {
        response.pipe(file);
        file.on('finish', function () {
          file.close();
          debug(`File downloaded: ${filename}`);
          resolve();
        });
      }
      else if (response.statusCode === 404) {
        reject(`${filename} not found in Onegini Artifactory repository`)
      }
      else if (response.statusCode === 401) {
        reject(`${username} is unauthorized`)
      }
      else {
        fs.unlinkSync(filePath);
        reject(`Downloading ${filename} went wrong: ${response.statusCode}`);
      }

    }).on('error', function (err) {
      fs.unlinkSync(filePath);
      reject(`Downloading ${filename} went wrong: ${err}`);
    });
  });
}

function checkDownloadedFileIntegrity(fileUrl) {
  writeToStdOut('.');
  const filename = fileUrl.substring(fileUrl.lastIndexOf('/') + 1);
  const downloadedFilePath = path.join(sdkDownloadPath, filename);
  const downloadedSha256FilePath = path.join(sdkDownloadPath, `${filename}.sha256`);
  const sha256FileUrl = `${fileUrl}.sha256`;
  return new Promise((resolve, reject) => {
    downloadFile(false, sha256FileUrl)
      .then(() => calculateSha256(downloadedFilePath))
      .then(calculatedHash => {
        const downloadedHash = fs.readFileSync(downloadedSha256FilePath).toString();
        debug(`Comparing hashes:`);
        debug(`downloaded hash: ${downloadedHash}`);
        debug(`calculated hash: ${calculatedHash}`);

        if (downloadedHash === calculatedHash) {
          debug('Hashes match!');
          resolve();
        }
        else {
          reject(`The file (${downloadedFilePath}) is damaged. Please remove and add the Onegini Cordova plugin and clean the download directory (if you have specified the ${envVariables.sdkDownloadPath}).`);
        }
      })
      .catch((err) => {
        reject(err)
      });
  });
}

function prepareSdkDirectories(context) {
  const pluginDir = context.opts.plugin.pluginInfo.dir;
  const sdkDir = path.join(pluginDir, iosSdkPathCordova);
  const headersDir = path.join(pluginDir, iosSdkHeadersPathCordova);

  if (!fs.existsSync(sdkDir)) {
    debug(`Create SDK directory: ${sdkDir}`);
    fs.mkdirSync(sdkDir);
  }

  if (!fs.existsSync(headersDir)) {
    debug(`Create headers directory: ${headersDir}`);
    fs.mkdirSync(headersDir);
  }

  if (!fs.existsSync(sdkDownloadPath)) {
    debug(`Create SDK download : ${sdkDownloadPath}`);
    fs.mkdirSync(sdkDownloadPath);
  }

  deleteFilesFromDirs([sdkDir, headersDir]);
}

function deleteFilesFromDirs(directories) {
  directories.forEach(dir => {
    fs.readdir(dir, (err, files) => {
      if (err) {
        console.error(err);
        console.error(`An error occurred while reading files from directory: ${dir}`)
      }

      for (const file of files) {
        if (file.endsWith('.a') || file.endsWith('.h')) {
          fs.unlink(path.join(dir, file), err => {
            if (err) {
              console.error(err);
              console.error(`An error occurred while deleting files from directory: ${dir}`)
            }
          });
        }
      }
    });
  });
}

function copyAndRenameSdkLib(context) {
  return new Promise((resolve, reject) => {
    writeToStdOut('.');
    debug('Copying and renaming SDK library');
    const pluginDir = context.opts.plugin.pluginInfo.dir;
    let targetSdkLibPath = path.join(pluginDir, iosSdkLibPathCordova);
    let downloadedSdkLibPath = path.join(sdkDownloadPath, libName);
    if (fs.existsSync(downloadedSdkLibPath)) {
      fs.createReadStream(downloadedSdkLibPath)
        .pipe(fs.createWriteStream(targetSdkLibPath));
      resolve();
    }
    else {
      reject(`Onegini iOS SDK library not found at path: ${downloadedSdkLibPath}`)
    }
  });
}

function unzipAndRenameHeaders(context) {
  return new Promise((resolve) => {
    writeToStdOut('.');
    debug('Unzipping and renaming headers');

    const pluginDir = context.opts.plugin.pluginInfo.dir;
    const newDir = path.join(pluginDir, iosSdkHeadersPathCordova);
    execSync('unzip ' + sdkDownloadPath + '/' + headersName + ' -d ' + newDir);
    resolve();
  });
}

function log(line) {
  console.log(`${pluginId}: ${line}`)
}

function writeToStdOut(output) {
  process.stdout.write(output);
}
